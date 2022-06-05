package banca.uy.core.services.interfaces;

import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;

public interface IActualizarBaseDeDatosService {

	@Scheduled(cron = "${cronExpressionActualizarBaseDeDatos}")
	void actualizarBaseDeDatos() throws InterruptedException, ParseException;
}
