package banca.uy.core.services.implementations;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import banca.uy.core.db.CodigosUsuariosDAO;
import banca.uy.core.db.ParamsDAO;
import banca.uy.core.db.ReseteoContrasenaDAO;
import banca.uy.core.db.UsuariosDAO;
import banca.uy.core.entity.CodigoUsuario;
import banca.uy.core.entity.Param;
import banca.uy.core.entity.ReseteoContrasena;
import banca.uy.core.entity.Usuario;
import banca.uy.core.exceptions.ServiceException;
import banca.uy.core.resources.dto.UsuarioPrincipal;
import banca.uy.core.resources.dto.UsuarioBasic;
import banca.uy.core.resources.dto.LoginResponse;
import banca.uy.core.security.jwt.JwtTokenProvider;
import banca.uy.core.services.interfaces.ILoginService;
import banca.uy.core.services.interfaces.IUserService;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;

@Service
public class UserService implements IUserService {

	@Autowired
	private UsuariosDAO userRepository;
	@Autowired
	private CodigosUsuariosDAO userCodesRepository;
	@Autowired
	private ReseteoContrasenaDAO passwordResetRepository;
	@Autowired
	private ParamsDAO paramsDAO;
	@Autowired
    JwtTokenProvider jwtTokenProvider;

	private ILoginService loginService;

	public UserService(UsuariosDAO usuariosDAO, CodigosUsuariosDAO codigosUsuariosDAO,
			ParamsDAO paramsDAO, ILoginService loginService, ReseteoContrasenaDAO passwordResetDAO
	) {
		this.userRepository = usuariosDAO;
		this.userCodesRepository = codigosUsuariosDAO;
		this.paramsDAO = paramsDAO;
		this.loginService = loginService;
		this.passwordResetRepository = passwordResetDAO;
	}

	@Override
	public Usuario Register(Usuario u) throws ServiceException {
		u.setParametersForRegister();
		this.userRepository.usuarioNoEstaRepetido(u);
		this.userRepository.save(u);
		this.sendValidationEmail(u);
		return u;
	}

	@Override
	public void enviarEmailConfirmacion(Usuario user) throws ServiceException {
		Optional<Usuario> existingEmailUser = this.userRepository.findByEmail(user.getEmail());

		existingEmailUser.filter(u -> !u.getValidado())
				.orElseThrow(() -> new ServiceException("Este Usuario ya fué validado o no existe"));
		this.sendValidationEmail(existingEmailUser.get());
	}

	@Transactional
	@Override
	public LoginResponse Confirm(String code) throws ServiceException, JoseException {
		Optional<CodigoUsuario> codigoOpt = this.userCodesRepository.findByCodigo(code);
		if (codigoOpt.isPresent()) {
			CodigoUsuario codigo = codigoOpt.get();
			Usuario u = this.userRepository.findById(codigo.getUsuario().getId());
			if (codigo.fueUsado()) {
				u.setValidado(true);
				this.userRepository.save(u);
				throw new ServiceException("Este usuario ya está confirmado");
			} else if (codigo.estaExpirado()) {
				this.sendValidationEmail(u);
				throw new ServiceException("Este código ya expiró. Por favor revise su email nuevamente");
			} else {
				codigo.usar();
				this.userCodesRepository.update(codigo);
				u.setValidado(true);
				this.userRepository.save(u);
			}
			Param jwtKey = paramsDAO.findByNombre("JWT_SECRET_KEY");
			Param expTime = paramsDAO.findByNombre("TOKEN_EXP_TIME");
			List<String> allRoles = new ArrayList<String>();
			JsonWebSignature jws;

			jws = jwtTokenProvider.buildToken(u, jwtKey, Float.parseFloat(expTime.getValor()));

			String jwt = jws.getCompactSerialization();

			JsonWebEncryption jwe = encryptToken(jwt, jwtKey);
			String token = jwe.getCompactSerialization();
			if (u.esAdministradorSistema())
				allRoles.add("systemAdmin");
			return new LoginResponse(token, allRoles, u);
		} else {
			throw new ServiceException("Este código no existe, por favor verifique su email");
		}
	}

	private JsonWebEncryption encryptToken(String jwt, Param jwtKey) {
		JsonWebEncryption jwe = new JsonWebEncryption();

		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(new HmacKey(jwtKey.getValor().getBytes()));
		jwe.setContentTypeHeaderValue("JWT");
		jwe.setPayload(jwt);

		return jwe;
	}

	private void sendValidationEmail(Usuario usuario) throws ServiceException {
		CodigoUsuario userCode = new CodigoUsuario(usuario);
		this.userCodesRepository.save(userCode);
	}

	private CodigoUsuario createUserCode(String userId, String businessId) {
		Usuario usuario = userRepository.findById(userId);
		CodigoUsuario toReturn = new CodigoUsuario(usuario);
		toReturn.setSusuario(usuario.getId());
		return toReturn;
	}

	@Override
	public Usuario FinishRegister(Usuario usuario) throws ServiceException {
		Optional<Usuario> existing = this.userRepository.findByEmail(usuario.getEmail());
		existing.filter((e) -> {
			return e.getContrasena() == null;
		}).orElseThrow(() -> new ServiceException("No existe un Usuario no registrado con el mail especificado"));

		if (usuario.getUsuario() != null && !usuario.getUsuario().isEmpty()) {
			Optional<String> existingByUser = Optional.ofNullable(usuario.getUsuario());
			existingByUser.flatMap((u) -> {
				return this.userRepository.findByUsuario(u);
			}).filter(u -> u.getId().equals(existing.get().getId())).orElseThrow(() -> {
				return new ServiceException("Ya existe un Usuario con el nombre de Usuario especificado");
			});
		}

		Usuario existingUser = existing.get();

		Optional<String> usuarioNombreOptional = Optional.ofNullable(usuario.getUsuario());
		String usuarioNombre = usuarioNombreOptional.orElse(
				existing.get().getUsuario() == null || existing.get().getUsuario().isEmpty() ? existing.get().getEmail()
						: existing.get().getUsuario());

		existingUser.setUsuario(usuarioNombre);
		String sha256hex = Hashing.sha256().hashString(usuario.getContrasena(), StandardCharsets.UTF_8).toString();
		existingUser.setContrasena(sha256hex);
		existingUser.setNombre(usuario.getNombre());
		existingUser.setApellido(usuario.getApellido());
		this.userRepository.save(existingUser);
		return existingUser;
	}

	@Transactional
	@Override
	public void SendPasswordReset(UsuarioPrincipal existingUser, Usuario toReset) throws ServiceException, Exception {
		if (toReset.getId() == null) {
			Optional<Usuario> optUser = this.userRepository.findByEmail(toReset.getEmail());
			if (optUser.isPresent())
				toReset = optUser.get();
			else
				throw new ServiceException("No hay usuario con este email");
		}
		Usuario user = this.userRepository.findById(toReset.getId());
		boolean isAdminForUser = false;

		if (isAdminForUser) {
			sendPasswordReset(user);
		} else
			throw new ServiceException(
					"Solo un Administrador puede solicitar un reseteo de contraseña para alguien de su Empresa");

	}

	private void sendPasswordReset(Usuario usuario) throws ServiceException {
		ReseteoContrasena passwordReset = new ReseteoContrasena(usuario);
		this.passwordResetRepository.save(passwordReset);
	}

	@Override
	public LoginResponse ChangePassword(String code, String contrasena) throws ServiceException, Exception {
		Optional<ReseteoContrasena> passwordResetOpt = this.passwordResetRepository.findByCodigo(code);
		if (passwordResetOpt.isPresent()) {
			ReseteoContrasena passwordReset = passwordResetOpt.get();
			/*
			 * if(passwordReset.fueUsado()) { throw new
			 * ServiceException("Este código ya fue utilizado"); }
			 */
			Date created = passwordReset.getFechaCreacion().toDate();
			Date now = new Date();

			if (passwordReset.estaExpirado() || now.getTime() - created.getTime() >= 30 * 60 * 1000) {
				throw new ServiceException("La solicitud expiró, debes repetir el procedimiento");
			}
			Usuario usuario = this.userRepository.findById(passwordReset.getUsuario().getId());
			usuario.encryptAndSetContrasena(contrasena);
			this.userRepository.save(usuario);
			Optional<String> optId = Optional.empty();
			passwordReset.usar();
			this.passwordResetRepository.save(passwordReset);
			LoginResponse response = this.loginService.Login(usuario, optId);
			return response;

		}
		throw new ServiceException("No se encuentra la solicitud para este usuario");
	}

	@Override
	public void SendPasswordReset(UsuarioBasic usuario) throws ServiceException, Exception {
		Optional<Usuario> optUser = this.userRepository.findByEmail(usuario.getEmail());
		if (optUser.isPresent()) {
			sendPasswordReset(optUser.get());
		} else
			throw new ServiceException("No hay usuario con este email");

	}

	@Override
	public void Modify(UsuarioPrincipal existingUser, Usuario usuario) throws ServiceException {
		Usuario existingDb = this.userRepository.findById(existingUser.getUsuario().getId());
		if (!usuario.getEmail().equals(existingDb.getEmail())) {
			Optional<Usuario> existingEmail = this.userRepository.findByEmail(usuario.getEmail());
			if (existingEmail.isPresent())
				throw new ServiceException("El email " + existingEmail.get().getEmail() + " ya existe");
		}

		if (usuario.getUsuario() != null && !usuario.getUsuario().equals(existingDb.getUsuario())) {
			Optional<Usuario> existingUsuario = this.userRepository.findByUsuario(usuario.getUsuario());
			if (existingUsuario.isPresent())
				throw new ServiceException("El Usuario " + existingUsuario.get().getUsuario() + " ya existe");
		}
		existingDb.setEmail(usuario.getEmail());
		if(usuario.getUsuario() != null)
		existingDb.setUsuario(usuario.getUsuario());
		existingDb.setNombre(usuario.getNombre());
		existingDb.setApellido(usuario.getApellido());
		this.userRepository.save(existingDb);
	}

	@Override
	public void DeleteAdmin(String id) throws ServiceException {
		try {
			Usuario toDelete = this.userRepository.findById(id);
			if (toDelete != null) {
				if (toDelete.esAdministradorSistema()) {
					toDelete.eliminar();
					this.userRepository.save(toDelete);
				} else
					throw new ServiceException("Este usuario no es administrador");
			} else
				throw new ServiceException("No hay administrador para este id");
		} catch (NumberFormatException ex) {
			throw new ServiceException("El id del administrador a eliminar debe ser numérico");
		}

	}

}
