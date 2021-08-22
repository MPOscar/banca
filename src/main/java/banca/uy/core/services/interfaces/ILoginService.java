package banca.uy.core.services.interfaces;

import java.util.Optional;

import banca.uy.core.resources.dto.UsuarioBasic;
import banca.uy.core.entity.Usuario;
import banca.uy.core.exceptions.ServiceException;
import banca.uy.core.resources.dto.LoginResponse;
import banca.uy.core.resources.dto.UsuarioPrincipal;

public interface ILoginService {
	LoginResponse LoginBasic(UsuarioBasic usuario) throws ServiceException, Exception;

	LoginResponse Login(Usuario usuario, Optional<String> id) throws ServiceException, Exception;
}
