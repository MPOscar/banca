package banca.uy.core.entity;

import banca.uy.core.utils.serializer.CustomDateTimeDeserializer;
import banca.uy.core.utils.serializer.CustomDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(value = { "target" })
public abstract class Entidad implements Serializable {

	@Id
	protected String id;

	protected String sid;

	protected Long oldId;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	protected DateTime fechaCreacion;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	protected DateTime fechaEdicion;

	protected Boolean eliminado;

	public Entidad() {
		this.eliminado = false;
		setFechaCreacion();
		setFechaEdicion();
	}

	public void eliminar() {
		setFechaEdicion();
		this.eliminado = true;
	}

	public void setFechaEdicion() {
		this.fechaEdicion = new DateTime();
	}

	public void setFechaCreacion() {
		DateTimeZone uruguay = DateTimeZone.forID("America/Argentina/Buenos_Aires");
		this.fechaCreacion = new DateTime();
	}

	public void setFechaEdicion(DateTime date) {
		this.fechaEdicion = date;
	}

	public void setFechaCreacion(DateTime date) {
		this.fechaCreacion = date;
	}

	public DateTime getFechaEdicion() {
		if (this.fechaEdicion == null)
			this.fechaEdicion = new DateTime();
		return this.fechaEdicion;
	}

	public DateTime getFechaCreacion() {
		if (this.fechaCreacion == null)
			this.fechaCreacion = new DateTime();
		return this.fechaCreacion;
	}

	public String getId() {
		return id;
	}

	public void setId(String paramId) {
		this.id = paramId;
	}

	@JsonIgnore
	public long getOldId() {
		return oldId;
	}

	public void setOldId(long oldId) {
		this.oldId = oldId;
	}

	public void noEliminado() {
		this.eliminado = false;
	}

	public Boolean getEliminado() {
		return eliminado;
	}

	public void inicializarLinkData() {
	}

	public String getSId() {
		return sid;
	}

	public void setSId(String sId) {
		this.sid = sId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
