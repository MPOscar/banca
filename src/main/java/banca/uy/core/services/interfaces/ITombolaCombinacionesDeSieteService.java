package banca.uy.core.services.interfaces;

import java.util.HashMap;

public interface ITombolaCombinacionesDeSieteService {
	HashMap<String, Integer> obtenerTodasLasJugadasDeSieteTombola();

	void inicializarTodasLasJugadasDeSieteTombola();

	void actualizarTodasLasJugadasDeSieteTombola();
}
