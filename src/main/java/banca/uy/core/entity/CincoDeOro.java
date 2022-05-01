package banca.uy.core.entity;

import banca.uy.core.utils.serializer.CustomDateTimeDeserializer;
import banca.uy.core.utils.serializer.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "CincoDeOro")
@CompoundIndexes({
		@CompoundIndex(name = "fechaTirada", def = "{ 'fechaTirada': 1 }", unique = true)
})
public class CincoDeOro extends Entidad {

	private List<Integer> cincoDeOro = new ArrayList<>();

	private List<Integer> rebancha = new ArrayList<>();

	private String pozoDeOro;

	private String pozoDePlata;

	private String pozoDeRevancha;

	private String numeroAciertosPozoDeOro;

	private String numeroAciertosPozoDePlata;

	private String numeroAciertosPozoRevancha;

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

	public String getPozoDeOro() {
		return pozoDeOro;
	}

	public void setPozoDeOro(String pozoDeOro) {
		this.pozoDeOro = pozoDeOro;
	}

	public String getPozoDePlata() {
		return pozoDePlata;
	}

	public void setPozoDePlata(String pozoDePlata) {
		this.pozoDePlata = pozoDePlata;
	}

	public String getPozoDeRevancha() {
		return pozoDeRevancha;
	}

	public void setPozoDeRevancha(String pozoDeRevancha) {
		this.pozoDeRevancha = pozoDeRevancha;
	}

	public String getNumeroAciertosPozoDeOro() {
		return numeroAciertosPozoDeOro;
	}

	public void setNumeroAciertosPozoDeOro(String numeroAciertosPozoDeOro) {
		this.numeroAciertosPozoDeOro = numeroAciertosPozoDeOro;
	}

	public String getNumeroAciertosPozoDePlata() {
		return numeroAciertosPozoDePlata;
	}

	public void setNumeroAciertosPozoDePlata(String numeroAciertosPozoDePlata) {
		this.numeroAciertosPozoDePlata = numeroAciertosPozoDePlata;
	}

	public String getNumeroAciertosPozoRevancha() {
		return numeroAciertosPozoRevancha;
	}

	public void setNumeroAciertosPozoRevancha(String numeroAciertosPozoRevancha) {
		this.numeroAciertosPozoRevancha = numeroAciertosPozoRevancha;
	}
}
