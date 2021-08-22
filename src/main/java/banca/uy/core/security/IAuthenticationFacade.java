package banca.uy.core.security;

import banca.uy.core.resources.dto.UsuarioPrincipal;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    UsuarioPrincipal getPrincipalAuth();
}
