package banca.uy.core.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "CincoDeOroCombinacion")
public class CincoDeOroCombinacion extends Entidad {

	private List<Integer> cincoDeOroCombinacion = new ArrayList<>();

	private boolean fueSorteado;

	public CincoDeOroCombinacion() {
	}

	public CincoDeOroCombinacion(List<Integer> cincoDeOroCombinacion, boolean fueSorteado) {
		this.cincoDeOroCombinacion = cincoDeOroCombinacion;
		this.fueSorteado = fueSorteado;
	}

	public List<Integer> getCincoDeOroCombinacion() {
		return cincoDeOroCombinacion;
	}

	public void setCincoDeOroCombinacion(List<Integer> cincoDeOroCombinacion) {
		this.cincoDeOroCombinacion = cincoDeOroCombinacion;
	}

	public boolean isFueSorteado() {
		return fueSorteado;
	}

	public void setFueSorteado(boolean fueSorteado) {
		this.fueSorteado = fueSorteado;
	}
}
