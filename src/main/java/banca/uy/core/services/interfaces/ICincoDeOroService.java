package banca.uy.core.services.interfaces;

public interface ICincoDeOroService {
	public void completarBaseDeDatos(String tirada);
	void actualizarBaseDeDatos(String fechaActualizacion) throws InterruptedException;
}
