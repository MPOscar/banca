package banca.uy.core.services.interfaces;

import banca.uy.core.entity.CincoDeOro;

import java.util.HashMap;
import java.util.List;

public interface ICincoDeOroService {

	void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException;

	void actualizarBaseDeDatos() throws InterruptedException;

    CincoDeOro obtenerUltimaJugada() throws InterruptedException;

    List<CincoDeOro> obtenerUltimasJugadas(int page, int size) throws InterruptedException;

    List<CincoDeOro> obtenerJugadasAnteriores(CincoDeOro cincoDeOro, int page, int size);

    List<CincoDeOro> obtenerJugadasPosteriores(CincoDeOro cincoDeOro, int page, int size);

    HashMap<Integer, List<CincoDeOro>> obtenerJugadasCincoDeOroConMayorNumeroDeCoincidencias(int coincidencias) throws InterruptedException;
}
