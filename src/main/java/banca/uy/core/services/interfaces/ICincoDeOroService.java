package banca.uy.core.services.interfaces;

public interface ICincoDeOroService {

	void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException;

	void actualizarBaseDeDatos() throws InterruptedException;

}
