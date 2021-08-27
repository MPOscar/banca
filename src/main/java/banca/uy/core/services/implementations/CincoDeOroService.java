package banca.uy.core.services.implementations;

import banca.uy.core.db.TombolaDAO;
import banca.uy.core.entity.Tombola;
import banca.uy.core.repository.ITombolaRepository;
import banca.uy.core.services.interfaces.ICincoDeOroService;
import banca.uy.core.services.interfaces.IEnviarPeticionApiDeLaBancaService;
import banca.uy.core.services.interfaces.ITombolaService;
import banca.uy.core.utils.Meses;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CincoDeOroService implements ICincoDeOroService {

	@Autowired
	private ITombolaRepository tombolaRepository;

	@Autowired
	IEnviarPeticionApiDeLaBancaService enviarPeticionApiDeLaBancaService;

	private static final String ulrCincoDeOro = "/resultados/cincodeoro/renderizar_info_sorteo";

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");

	public CincoDeOroService(ITombolaRepository tombolaRepository) {
		this.tombolaRepository = tombolaRepository;
	}

	public void completarBaseDeDatos(String fechaDeTirada){
		String respuesta = enviarPeticionApiDeLaBancaService.enviarPeticionApiDeLaBanca(fechaDeTirada, ulrCincoDeOro);
		if(!respuesta.contains("No se encontró información del sorteo para la fecha seleccionada")){
			String mensaje = respuesta.substring(respuesta.indexOf("<h2>"));
			mensaje = mensaje.substring(0, mensaje.indexOf("<div class=\\\"clear\\\">"));
			mensaje = mensaje.replace("<h2>", "");
			mensaje = mensaje.replace("<\\/h2>", "");
			mensaje = mensaje.replace("<h3>", "");
			mensaje = mensaje.replace("<\\/h3>", "");
			mensaje = mensaje.replace("<li>", "");
			mensaje = mensaje.replace("<\\/li>", "");
			mensaje = mensaje.replace("<ul class=\\\"results-column\\\">", "");
			mensaje = mensaje.replace("<ul>", "");
			mensaje = mensaje.replace("<\\/ul>", "");
			mensaje = mensaje.replace(" \\n", "");
			mensaje = mensaje.replace("\t\t\\n", "");
			salvarTirada(mensaje);
		}
	}

	public void salvarTirada(String tirada){
		String fechaTiradaToParse = formatearFecha(tirada.substring(0,tirada.indexOf("\\n")));
		String tiradaTipoNumeros = tirada.substring(tirada.indexOf("\\n") + 2);
		String tipoTirada = tiradaTipoNumeros.substring(0, tiradaTipoNumeros.indexOf("\\n"));
		String numerosTirada = tiradaTipoNumeros.substring(tiradaTipoNumeros.indexOf("\\n") + 2);
		numerosTirada = numerosTirada.replace( " ", "");
		numerosTirada = numerosTirada.replace( "\\n", " ");
		String [] numeros = numerosTirada.split(" ");
		List<Integer> numerosTiradaSalvar = Arrays.asList(numeros).stream().map(Integer::valueOf).collect(Collectors.toList());

		boolean diurna = tipoTirada.indexOf("Vespertino") > -1 ? true : false;

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse);

		/*Tombola tombola = tombolaRepository.findFirstByFechaTirada(fechaTirada);
		if(tombola == null){
			tombola = new Tombola(fechaTirada);
		}

		if(diurna){
			tombola.setSorteoVespertino(numerosTiradaSalvar);
		} else {
			tombola.setSorteoNocturno(numerosTiradaSalvar);
		}*/
	}


	@Override
	public void actualizarBaseDeDatos(String fechaActualizacion) throws InterruptedException {
		Calendar calendar = Calendar.getInstance();
		DateTime fechaParada = formatter.parseDateTime(fechaActualizacion);
		while(new DateTime(calendar).isAfter(fechaParada)){
			DateTime fehaTirada = new DateTime(calendar);
			String parametro = formatter.print(fehaTirada);
			String tiradaVespertina = parametro + "-15:00";
			String tiradaNocturna = parametro + "-21:00";
			completarBaseDeDatos(tiradaVespertina);
			TimeUnit.SECONDS.sleep(1);
			completarBaseDeDatos(tiradaNocturna);
			TimeUnit.SECONDS.sleep(1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	public String formatearFecha(String fecha){
		String [] numeros = fecha.split(" ");
		return numeros[1] + "/" + Meses.mesesDelAño.get(numeros[3].toLowerCase()) + "/" + numeros[5];
	}
}
