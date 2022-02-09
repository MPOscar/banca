package banca.uy.core.services.interfaces;

import banca.uy.core.entity.Tombola;

import java.util.HashMap;
import java.util.List;

public interface ITombolaService {

	void obtenerTiradaYGuardarEnBaseDeDatos(String tirada);

	void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException;

	void actualizarBaseDeDatos() throws InterruptedException;

	Tombola obtenerUltimaJugada() throws InterruptedException;

	List<Tombola> obtenerJugadasAnteriores(Tombola tombola, int page, int size);

	List<Tombola> obtenerJugadasPosteriores(Tombola tombola, int page, int size);

	List<Tombola> obtenerUltimasJugadas(int page, int size) throws InterruptedException;

	HashMap<Integer, List<Tombola>> obtenerJugadasTombolaConMayorNumeroDeCoincidencias(int coincidencias) throws InterruptedException;
}
