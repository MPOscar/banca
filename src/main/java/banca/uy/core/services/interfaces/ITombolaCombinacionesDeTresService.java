package banca.uy.core.services.interfaces;

import java.util.HashMap;

public interface ITombolaCombinacionesDeTresService {

	HashMap<String, Integer> obtenerTodasLasJugadasDeTresTombola();

	void inicializarTodasLasJugadasDeTresTombola();

	void actualizarTodasLasJugadasDeTresTombola();
}
