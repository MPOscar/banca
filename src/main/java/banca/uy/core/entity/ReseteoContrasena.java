package banca.uy.core.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import banca.uy.core.utils.Helpers;
import banca.uy.core.utils.serializer.CustomDateTimeDeserializer;
import banca.uy.core.utils.serializer.CustomDateTimeSerializer;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ReseteoContrasena")
public class ReseteoContrasena extends Entidad {

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private DateTime fechaUso;

	@DBRef(lazy = true)
	private Usuario usuario;

	private String susuario;

	private String sempresa;

	private String codigo;

	private Boolean usado;

	private Boolean expirado;

	public ReseteoContrasena() {
		super();
	}

	public ReseteoContrasena(ReseteoContrasena reseteoContrasena) {
		super();
		this.oldId = reseteoContrasena.getOldId();
		this.fechaUso = reseteoContrasena.getFechaUso();
		this.codigo = reseteoContrasena.getCodigo();
		this.usado = reseteoContrasena.getUsado();
		this.expirado = reseteoContrasena.getExpirado();
		this.fechaCreacion = reseteoContrasena.getFechaCreacion();
		this.fechaEdicion = reseteoContrasena.getFechaEdicion();
		this.eliminado = reseteoContrasena.getEliminado();
	}

	public ReseteoContrasena(Usuario usuario) {
		super();
		this.usuario = usuario;
		this.setSusuario(usuario.getId());
		this.codigo = Helpers.randomString(8);
		this.usado = false;
		this.expirado = false;
	}

	public void usar() {
		this.usado = true;
		this.fechaUso = new DateTime();
	}

	public void expirar() {
		this.expirado = true;
	}

	public String getCodigo() {
		return codigo;
	}

	public Boolean getUsado() {
		return usado;
	}

	public void setUsado(Boolean usado) {
		this.usado = usado;
	}

	public Boolean getExpirado() {
		return expirado;
	}

	public void setExpirado(Boolean expirado) {
		this.expirado = expirado;
	}

	public DateTime getFechaUso() {
		return fechaUso;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setFechaUso(DateTime fechaUso) {
		this.fechaUso = fechaUso;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSusuario() {
		return susuario;
	}

	public void setSusuario(String susuario) {
		this.susuario = susuario;
	}

	public String getSempresa() {
		return sempresa;
	}

	public void setSempresa(String sempresa) {
		this.sempresa = sempresa;
	}

	public boolean estaExpirado(){
		return expirado;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReseteoContrasena other = (ReseteoContrasena) obj;
		if (other.getId().equals(this.getId()))
			return true;
		return false;
	}
}