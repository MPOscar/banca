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

@Document(collection = "Quiniela")
public class Quiniela extends Entidad {

	private List<Integer> sorteo = new ArrayList<>();

	private boolean esDiurno;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	@Indexed(direction = IndexDirection.ASCENDING, unique = true)
	protected DateTime fechaTirada;

	public Quiniela(DateTime fechaTirada) {
		this.fechaTirada = fechaTirada;
	}

	public List<Integer> getSorteo() {
		return sorteo;
	}

	public void setSorteo(List<Integer> sorteo) {
		this.sorteo = sorteo;
	}

	public boolean isEsDiurno() {
		return esDiurno;
	}

	public void setEsDiurno(boolean esDiurno) {
		this.esDiurno = esDiurno;
	}

	public DateTime getFechaTirada() {
		return fechaTirada;
	}

	public void setFechaTirada(DateTime fechaTirada) {
		this.fechaTirada = fechaTirada;
	}
}
