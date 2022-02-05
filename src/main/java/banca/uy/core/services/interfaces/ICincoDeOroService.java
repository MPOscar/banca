package banca.uy.core.services.interfaces;

import banca.uy.core.entity.CincoDeOro;

import java.util.HashMap;
import java.util.List;

public interface ICincoDeOroService {

	void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException;

	void actualizarBaseDeDatos() throws InterruptedException;

    CincoDeOro obtenerUltimaJugada() throws InterruptedException;

    HashMap<Integer, List<CincoDeOro>> obtenerJugadasCincoDeOroConMayorNumeroDeCoincidencias(int coincidencias) throws InterruptedException;
}
