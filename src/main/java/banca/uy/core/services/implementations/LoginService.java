package banca.uy.core.services.implementations;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import banca.uy.core.db.ParamsDAO;
import banca.uy.core.entity.Param;
import banca.uy.core.resources.dto.UsuarioBasic;
import banca.uy.core.services.interfaces.ILoginService;
import com.google.common.hash.Hashing;

import banca.uy.core.db.UsuariosDAO;
import banca.uy.core.exceptions.ServiceException;
import banca.uy.core.resources.dto.LoginResponse;
import banca.uy.core.resources.dto.UsuarioPrincipal;
import banca.uy.core.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import banca.uy.core.entity.Rol;
import banca.uy.core.entity.Usuario;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService implements ILoginService {
	@Autowired
	private ParamsDAO paramsDAO;
	@Autowired
	private UsuariosDAO usuariosDAO;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
    JwtTokenProvider jwtTokenProvider;

	@Override
	public LoginResponse LoginBasic(UsuarioBasic usuario) throws ServiceException, Exception {
		Usuario u = authenticate(usuario.getUsuario(), usuario.getContrasena());
		Optional<String> opt = Optional.empty();
		LoginResponse loginResponse = this.Login(u, opt);
		return loginResponse;
	}

	@Override
	public LoginResponse Login(Usuario usuario, Optional<String> id) throws ServiceException, Exception {
		Optional<Usuario> optUsuario = Optional.empty();
		if(usuario.getUsuario() != null) {
			optUsuario = this.usuariosDAO.findByUsuario(usuario.getUsuario());
		}
		else if (!optUsuario.isPresent() && usuario.getEmail() != null) {
			optUsuario = this.usuariosDAO.findByEmail(usuario.getEmail());
		}else{
			throw new ServiceException("No hay usuario con este nombre");
		}
		if (optUsuario.isPresent()) {
			Usuario u = optUsuario.get();
			Param jwtKey = paramsDAO.findByNombre("JWT_SECRET_KEY");
			Param expTime = paramsDAO.findByNombre("TOKEN_EXP_TIME");
			List<String> allRoles = new ArrayList<String>();
			JsonWebSignature jws;

			jws = jwtTokenProvider.buildToken(u, jwtKey, Float.parseFloat(expTime.getValor()));

			String jwt = jws.getCompactSerialization();

			JsonWebEncryption jwe = jwtTokenProvider.encryptToken(jwt, jwtKey);

			String token = jwe.getCompactSerialization();

			if (u.esAdministradorSistema() != null && u.esAdministradorSistema())
				allRoles.add("systemAdmin");
			return new LoginResponse(token, allRoles, u);
		} else {
			throw new ServiceException("No hay usuario con este nombre");
		}

	}

	private Usuario authenticate(String username, String password) throws ServiceException {
		Optional<Usuario> result;
		try {
			result = usuariosDAO.findByUsuario(username);
			if (!result.isPresent())
				result = usuariosDAO.findByEmail(username);

			if (result.isPresent() && result.get().getActivo() && result.get().getValidado()) {
				String sha256hex = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
				if (sha256hex.toUpperCase().equals((result.get().getContrasena().toUpperCase()))) {
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
					return result.get();
				}
				throw new ServiceException("Contraseña incorrecta para usuario");
			}

			throw new ServiceException("Este usuario no está validado");
		} catch (Exception e) {
			throw new ServiceException("Ingreso no autorizado. Mensaje " + e.getMessage());
		}
	}

}
