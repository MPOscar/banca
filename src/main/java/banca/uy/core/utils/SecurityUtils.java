package banca.uy.core.utils;

import banca.uy.core.resources.dto.UsuarioJwt;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static UsuarioJwt getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return (UsuarioJwt) securityContext.getAuthentication().getPrincipal();
    }
}
