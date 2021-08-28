package banca.uy.core.entity;

import banca.uy.core.utils.serializer.CustomDateTimeDeserializer;
import banca.uy.core.utils.serializer.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "CincoDeOro")
public class CincoDeOro extends Entidad {

	private List<Integer> cincoDeOro = new ArrayList<>();

	private List<Integer> rebancha = new ArrayList<>();

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	@Indexed(direction = IndexDirection.ASCENDING, unique = true)
	protected DateTime fechaTirada;

	public CincoDeOro() {
		super();
	}

	public CincoDeOro(DateTime fechaTirada) {
		this.fechaTirada = fechaTirada;
	}

	public List<Integer> getCincoDeOro() {
		return cincoDeOro;
	}

	public void setCincoDeOro(List<Integer> cincoDeOro) {
		this.cincoDeOro = cincoDeOro;
	}

	public List<Integer> getRebancha() {
		return rebancha;
	}

	public void setRebancha(List<Integer> rebancha) {
		this.rebancha = rebancha;
	}

	public DateTime getFechaTirada() {
		return fechaTirada;
	}

	public void setFechaTirada(DateTime fechaTirada) {
		this.fechaTirada = fechaTirada;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CincoDeOro other = (CincoDeOro) obj;
		if (other.getId().equals(this.getId()))
			return true;
		return false;
	}
}
