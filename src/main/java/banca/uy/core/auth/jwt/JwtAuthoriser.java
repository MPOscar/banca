package banca.uy.core.auth.jwt;

import banca.uy.core.entity.Usuario;
import banca.uy.core.resources.dto.UsuarioJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.auth.Authorizer;

public class JwtAuthoriser implements Authorizer<UsuarioJwt> {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthoriser.class);

	@Override
	public boolean authorize(UsuarioJwt user, String requiredRole) {

		if (user == null) {
			LOGGER.warn("msg=User object was null");
			return false;
		}

		if(requiredRole.equals(Roles.SYSTEM_ADMIN)) {
			Usuario u = user.getUsuario();
			if(u.esAdministradorSistema()) {
				return true;
			}
			return false;
		}
		
		if(requiredRole.equals(Roles.NEW_USER)) {
			return true;
		}

		return false;
	}
}