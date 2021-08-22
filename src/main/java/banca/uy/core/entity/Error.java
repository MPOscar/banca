
package banca.uy.core.entity;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Error")
public class Error extends Entidad {

	private String userId;

	private String message;

	private String stackTrace;

	public Error(String message) {
		this.message = message;
		this.userId = "0";
	}

	public Error(String message, Usuario usuario) {
		this.message = message;
		this.stackTrace = stackTrace;
		this.userId = usuario.getId();
	}

	public Error(String message, String stackTrace) {
		this.message = message;
		this.stackTrace = stackTrace;
		this.userId = "";
	}
}