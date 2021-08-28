package banca.uy.core.services.implementations;

import banca.uy.core.db.CincoDeOroDAO;
import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.repository.ICincoDeOroRepository;
import banca.uy.core.services.interfaces.ICincoDeOroService;
import banca.uy.core.services.interfaces.IEnviarPeticionApiDeLaBancaService;
import banca.uy.core.utils.Meses;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CincoDeOroService implements ICincoDeOroService {

	@Autowired
	private ICincoDeOroRepository cincoDeOroRepository;

	@Autowired
	IEnviarPeticionApiDeLaBancaService enviarPeticionApiDeLaBancaService;

	@Autowired
	private CincoDeOroDAO cincoDeOroDAO;

	private static final String ulrCincoDeOro = "/resultados/cincodeoro/renderizar_info_sorteo";

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");

	public CincoDeOroService(ICincoDeOroRepository cincoDeOroRepository) {
		this.cincoDeOroRepository = cincoDeOroRepository;
	}

	public void obtenerTiradaYGuardarEnBaseDeDatos(String fechaDeTirada){
		String respuesta = enviarPeticionApiDeLaBancaService.enviarPeticionApiDeLaBanca(fechaDeTirada, ulrCincoDeOro);
		if(!respuesta.contains("No se encontró información del sorteo para la fecha seleccionada")){
			String mensaje = respuesta.substring(respuesta.indexOf("<h2>"));
			String fechaCincoDeOro = mensaje.substring(0, mensaje.indexOf("<\\/h2>")).replace("<h2>", "");
			List<Integer> numerosCincoDeOro = obtenerNumerosCincoDeOro(mensaje);
			salvarTirada(fechaCincoDeOro, numerosCincoDeOro);
		}
	}

	public List<Integer> obtenerNumerosCincoDeOro(String mensaje){
		List<Integer> numerosCincoDeOro = new ArrayList<>();
		for (int i = 0; i < 11; i++){
			int index = mensaje.indexOf("alt=\\\"") + 5;
			String numero = mensaje.substring(index, index + 3);
			numero = numero.replace("\"", "");
			numero = numero.replace("\\", "");
			numerosCincoDeOro.add(obtenerNumero(numero));
			mensaje = mensaje.substring(index + 3);
		}
		return numerosCincoDeOro;
	}

	public int obtenerNumero(String numeroString){
		int numero = 0;
		try {
			numero = Integer.parseInt(numeroString);
		} catch (NumberFormatException e) {
			// no hacer nada
		}
		return numero;
	}

	public void salvarTirada(String fecha, List<Integer> numeros){
		String fechaTiradaToParse = formatearFecha(fecha);

		List<Integer> numerosCincoDeOro = numeros.subList(0, 6);
		List<Integer> numerosCincoDeOroRebancha = numeros.subList(6, 11);

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse);

		CincoDeOro cincoDeOro = cincoDeOroRepository.findFirstByFechaTirada(fechaTirada);
		if(cincoDeOro == null){
			cincoDeOro = new CincoDeOro(fechaTirada);
		}
		cincoDeOro.setCincoDeOro(numerosCincoDeOro);
		cincoDeOro.setRebancha(numerosCincoDeOroRebancha);
		cincoDeOroDAO.save(cincoDeOro);
	}

	@Override
	public void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException {
		DateTime fechaParada = formatter.parseDateTime(fechaActualizacion);
		actualizarHastaFechaSeleccionada(fechaParada);
	}

	@Override
	public void actualizarBaseDeDatos() throws InterruptedException {
		CincoDeOro cincoDeOro = cincoDeOroDAO.obtenerUltimaJugadaCompleta();
		DateTime fechaParada = cincoDeOro.getFechaTirada();
		actualizarHastaFechaSeleccionada(fechaParada);
	}

	public void actualizarHastaFechaSeleccionada(DateTime fechaParada) throws InterruptedException {
		Calendar calendar = Calendar.getInstance();
		while(new DateTime(calendar).isAfter(fechaParada)){
			DateTime fehaTirada = new DateTime(calendar);
			String parametro = formatter.print(fehaTirada);
			String tirada = parametro + "-22:00";
			obtenerTiradaYGuardarEnBaseDeDatos(tirada);
			TimeUnit.SECONDS.sleep(1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	public String formatearFecha(String fecha){
		String [] numeros = fecha.split(" ");
		return numeros[1] + "/" + Meses.mesesDelAño.get(numeros[3].toLowerCase()) + "/" + numeros[5];
	}
}
