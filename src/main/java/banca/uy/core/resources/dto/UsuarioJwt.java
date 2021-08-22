package banca.uy.core.resources.dto;

import banca.uy.core.entity.Usuario;

public class UsuarioJwt implements UsuarioInterface {

	private String id;
	private Usuario usuario;

	public UsuarioJwt() {
		
	}

	public UsuarioJwt(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public UsuarioJwt(String id, Usuario usuario) {
		this.id = id;
		this.usuario = usuario;
	}
	
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return usuario.getNombre();
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}

}
