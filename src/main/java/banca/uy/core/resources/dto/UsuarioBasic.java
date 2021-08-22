package banca.uy.core.resources.dto;

public class UsuarioBasic implements UsuarioInterface {

	private Long id;
	private String usuario;
	private String email;
	private String contrasena;
	private String empresa;
	private String roles;
	
	public UsuarioBasic() {
		
	}

	public UsuarioBasic(Long id, String usuario, String roles) {
		this.id = id;
		this.usuario = usuario;
		this.roles = roles;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	public String getContrasena() {
		return contrasena;
	}
	
	public String getEmpresa() {
		return empresa;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String toString() {
		return "UsuarioBasic [id=" + id + ", usuario=" + usuario + ", contrasena=" + contrasena + ", empresa=" + empresa
				+ ", roles=" + roles + "]";
	}

}
