package banca.uy.core.services.interfaces;

import banca.uy.core.entity.Usuario;

public interface IErrorService {
	public void Log(String message, Usuario usuario);
	public void Log(String string);
	public void Log(String message, String stackTrace);
}
