package banca.uy.core.security;

import banca.uy.core.resources.dto.UsuarioPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UsuarioPrincipal getPrincipalAuth() {
        Authentication authentication = getAuthentication();
        return (UsuarioPrincipal) authentication.getPrincipal();
    }
}
