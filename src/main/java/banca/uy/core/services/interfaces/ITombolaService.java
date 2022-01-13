package banca.uy.core.services.interfaces;

import java.util.List;
import java.util.Set;

public interface ITombolaService {

	public void obtenerTiradaYGuardarEnBaseDeDatos(String tirada);

	void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException;

	void actualizarBaseDeDatos() throws InterruptedException;

	public Set<Integer> getJugada(String fecha);

	public List<String> getJugadaRepetidas(String fecha);

}
