package banca.uy.core.entity;

import banca.uy.core.utils.serializer.CustomDateTimeDeserializer;
import banca.uy.core.utils.serializer.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "CincoDeOro")
public class CincoDeOro extends Entidad {

	private String dia;

	private Set<Integer> diurna = new HashSet<>();

	private Set<Integer> nocturna = new HashSet<>();

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	protected DateTime fecha;

	public CincoDeOro() {
		super();
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
