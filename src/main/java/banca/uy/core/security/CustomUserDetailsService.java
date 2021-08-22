package banca.uy.core.security;

import banca.uy.core.entity.Rol;
import banca.uy.core.entity.Usuario;
import banca.uy.core.repository.IUserRepository;
import banca.uy.core.resources.dto.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameUsuarioEmpresa) {
        String username = usernameUsuarioEmpresa;
        List<String> roles = new ArrayList<String>();
        Optional<Usuario> usuario = this.userRepository.findByEmail(username);
        if (!usuario.isPresent()){
            usuario = this.userRepository.findByUsuario(username);
        }
        if(!usuario.isPresent()) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }

        if (usuario.get().esAdministradorSistema() != null && usuario.get().esAdministradorSistema())
            roles.add("systemAdmin");

        UsuarioPrincipal usuarioPrincipal = new UsuarioPrincipal(usuario.get(), roles);
        return usuarioPrincipal;
    }
}
