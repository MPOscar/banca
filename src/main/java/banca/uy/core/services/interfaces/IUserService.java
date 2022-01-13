package banca.uy.core.services.interfaces;

import banca.uy.core.exceptions.ServiceException;
import banca.uy.core.resources.dto.LoginResponse;
import banca.uy.core.resources.dto.UsuarioBasic;
import banca.uy.core.resources.dto.UsuarioPrincipal;
import banca.uy.core.entity.Usuario;
import org.jose4j.lang.JoseException;

public interface IUserService {

	public Usuario Register(Usuario u) throws ServiceException;

	public LoginResponse Confirm(String code) throws ServiceException, JoseException;

	void enviarEmailConfirmacion(Usuario user) throws ServiceException;

	public Usuario FinishRegister(Usuario usuario) throws ServiceException;

	public void SendPasswordReset(UsuarioPrincipal existingUser, Usuario toReset) throws ServiceException, Exception;

	public LoginResponse ChangePassword(String code, String contrasena) throws ServiceException, Exception;

	public void SendPasswordReset(UsuarioBasic usuario) throws ServiceException, Exception;

	public void Modify(UsuarioPrincipal existingUser, Usuario usuario) throws ServiceException;

	void DeleteAdmin(String id) throws ServiceException;
}
