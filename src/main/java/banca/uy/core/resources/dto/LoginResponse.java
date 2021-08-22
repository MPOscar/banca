package banca.uy.core.resources.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import banca.uy.core.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

	@JsonProperty("token")
	public String token;
	
	@JsonProperty("roles")
	public List<String> roles;
	
	@JsonProperty("user")
	public Usuario user;

	public LoginResponse(String token) {
		this.token = token;
	}

	
	public LoginResponse(String token, List <String>roles, Usuario u) {
		this.token = token;
		this.roles = roles;
		this.user = u;
	}
	
}