package banca.uy.core.services.interfaces;

import banca.uy.core.entity.CincoDeOro;

public interface ICincoDeOroService {

	void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException;

	void actualizarBaseDeDatos() throws InterruptedException;

    CincoDeOro obtenerUltimaJugada() throws InterruptedException;
}
