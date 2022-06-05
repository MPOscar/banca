package banca.uy.core.entity;

import banca.uy.core.utils.serializer.CustomDateTimeDeserializer;
import banca.uy.core.utils.serializer.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TombolaCombinacionesDeSiete")
@CompoundIndexes({
		@CompoundIndex(name = "combinacion", def = "{ 'combinacion': 1 }", unique = true)
})
public class TombolaCombinacionesDeSiete extends Entidad {

	String combinacion;

	int numeroDeVecesQueHaSalido;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	protected DateTime fechaTirada;

	public TombolaCombinacionesDeSiete() {
	}

	public TombolaCombinacionesDeSiete(String combinacion, int numeroDeVecesQueHaSalido, DateTime fechaTirada) {
		this.combinacion = combinacion;
		this.numeroDeVecesQueHaSalido = numeroDeVecesQueHaSalido;
		this.fechaTirada = fechaTirada;
	}

	public TombolaCombinacionesDeSiete(DateTime fechaTirada) {
		this.fechaTirada = fechaTirada;
	}

	public String getCombinacion() {
		return combinacion;
	}

	public void setCombinacion(String combinacion) {
		this.combinacion = combinacion;
	}

	public int getNumeroDeVecesQueHaSalido() {
		return numeroDeVecesQueHaSalido;
	}

	public void setNumeroDeVecesQueHaSalido(int numeroDeVecesQueHaSalido) {
		this.numeroDeVecesQueHaSalido = numeroDeVecesQueHaSalido;
	}

	public DateTime getFechaTirada() {
		return fechaTirada;
	}

	public void setFechaTirada(DateTime fechaTirada) {
		this.fechaTirada = fechaTirada;
	}
}
