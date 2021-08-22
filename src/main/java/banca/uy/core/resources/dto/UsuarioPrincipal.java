package banca.uy.core.resources.dto;

import banca.uy.core.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.*;

public class UsuarioPrincipal implements UserDetails {

	private Long id;
	private Usuario usuario;

	public UsuarioPrincipal() {
	}

	public UsuarioPrincipal(Usuario usuario, List<String> roles) {
		this.usuario = usuario;
		this.roles = roles;
	}

	public UsuarioPrincipal(Long id, Usuario usuario) {
		this.id = id;
		this.usuario = usuario;
	}

	public Long getId() {
		return this.id;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> roles = new ArrayList<>();

	@Override
	public Set<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
		return authorities;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	@JsonIgnore
	public String getPassword() {
		return this.usuario.getPassword();
	}

	@JsonIgnore
	public String getUsername() {
		return this.usuario.getUsuario();
	}
}
