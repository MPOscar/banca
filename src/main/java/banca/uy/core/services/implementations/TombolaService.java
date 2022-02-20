package banca.uy.core.services.implementations;

import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.repository.ITombolaRepository;
import banca.uy.core.services.interfaces.IEnviarPeticionApiDeLaBancaService;
import banca.uy.core.db.TombolaDAO;
import banca.uy.core.entity.Tombola;
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
public class TombolaService implements ITombolaService {

	@Autowired
	private ITombolaRepository tombolaRepository;

	@Autowired
	private TombolaDAO tombolaDAO;

	@Autowired
    IEnviarPeticionApiDeLaBancaService enviarPeticionApiDeLaBancaService;

	private static final String ulrTombola = "/resultados/tombola/renderizar_info_sorteo";

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");

	public TombolaService(ITombolaRepository tombolaRepository) {
		this.tombolaRepository = tombolaRepository;
	}

	public void obtenerTiradaYGuardarEnBaseDeDatos(String fechaDeTirada){
		String respuesta = enviarPeticionApiDeLaBancaService.enviarPeticionApiDeLaBanca(fechaDeTirada, ulrTombola);
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

	public Tombola salvarTirada(String tirada){
		String fechaTiradaToParse = formatearFecha(tirada.substring(0,tirada.indexOf("\\n")));
		String tiradaTipoNumeros = tirada.substring(tirada.indexOf("\\n") + 2);
		String tipoTirada = tiradaTipoNumeros.substring(0, tiradaTipoNumeros.indexOf("\\n"));
		String numerosTirada = tiradaTipoNumeros.substring(tiradaTipoNumeros.indexOf("\\n") + 2);
		numerosTirada = numerosTirada.replace( " ", "");
		numerosTirada = numerosTirada.replace( "\\n", " ");
		String [] numeros = numerosTirada.split(" ");
		List<Integer> numerosTiradaSalvar = Arrays.asList(numeros).stream().map(Integer::valueOf).collect(Collectors.toList());

		boolean diurna = tipoTirada.indexOf("Vespertino") > -1 ? true : false;

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse + (diurna ? " 12:00:00" : " 18:00:00"));

		Tombola tombola = tombolaRepository.findFirstByFechaTirada(fechaTirada);
		if(tombola == null){
			tombola = new Tombola(fechaTirada);
		}
		tombola.setEsDiurno(diurna);
		tombola.setSorteo(numerosTiradaSalvar);

		return this.tombolaDAO.save(tombola);
	}


	@Override
	public void inicializarBaseDeDatos(String fechaActualizacion) throws InterruptedException {
		DateTime fechaParada = formatter.parseDateTime(fechaActualizacion);
		actualizarHastaFechaSeleccionada(fechaParada);
	}

	@Override
	public void actualizarBaseDeDatos() throws InterruptedException {
		Tombola tombola = tombolaDAO.obtenerUltimaJugadaCompleta();
		DateTime fechaParada = tombola.getFechaTirada();
		actualizarHastaFechaSeleccionada(fechaParada);
	}

	public void actualizarHastaFechaSeleccionada(DateTime fechaParada) throws InterruptedException {
		Calendar calendar = Calendar.getInstance();
		while(new DateTime(calendar).isAfter(fechaParada)){
			DateTime fehaTirada = new DateTime(calendar);
			String parametro = formatter.print(fehaTirada);
			String tiradaVespertina = parametro + "-15:00";
			String tiradaNocturna = parametro + "-21:00";
			obtenerTiradaYGuardarEnBaseDeDatos(tiradaVespertina);
			TimeUnit.MILLISECONDS.sleep(200);
			obtenerTiradaYGuardarEnBaseDeDatos(tiradaNocturna);
			TimeUnit.MILLISECONDS.sleep(200);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	@Override
	public Tombola obtenerUltimaJugada() throws InterruptedException {
		Tombola tombola = tombolaDAO.obtenerUltimaJugadaCompleta();
		return tombola;
	}

	@Override
	public List<Tombola> obtenerJugadasAnteriores(Tombola tombola, int page, int size){
		List<Tombola> jugadasAnteriores = tombolaDAO.obtenerJugadasAnterioresCincoDeOro(tombola, page, size);
		return jugadasAnteriores;
	}

	@Override
	public List<Tombola> obtenerJugadasPosteriores(Tombola tombola, int page, int size){
		List<Tombola> jugadasAnteriores = tombolaDAO.obtenerJugadasPosterioresCincoDeOro(tombola, page, size);
		return jugadasAnteriores;
	}

	@Override
	public List<Tombola> obtenerUltimasJugadas(int page, int size) throws InterruptedException {
		List<Tombola> ultimasJugadas = tombolaDAO.obtenerUltimasJugadas(page, size);
		return ultimasJugadas;
	}

	@Override
	public HashMap<Integer, List<Tombola>> obtenerJugadasTombolaConMayorNumeroDeCoincidencias(int coincidencias) throws InterruptedException {
		Tombola ultimaJugada = obtenerUltimaJugada();
		HashMap<Integer, List<Tombola>> jugadasConMayorNumeroDeCoincidencias = new HashMap<>();
		List<Tombola> jugadasTombolaConCoincidencias = tombolaDAO.obtenerJugadasTombolaConCoincidencias(ultimaJugada);
		for (Tombola tombola: jugadasTombolaConCoincidencias) {
			int numeroDeCoincidencias = buscarNumeroDeCoincidencias(ultimaJugada, tombola);
			if (numeroDeCoincidencias >= coincidencias) {
				if (jugadasConMayorNumeroDeCoincidencias.get(numeroDeCoincidencias) != null) {
					jugadasConMayorNumeroDeCoincidencias.get(numeroDeCoincidencias).add(tombola);
				} else {
					List<Tombola> jugadasConCoincidencias = new ArrayList<>();
					jugadasConCoincidencias.add(tombola);
					jugadasConMayorNumeroDeCoincidencias.put(numeroDeCoincidencias, jugadasConCoincidencias);
				}
			}
		}
		return jugadasConMayorNumeroDeCoincidencias;
	}

	public int buscarNumeroDeCoincidencias(Tombola ultimaJugada, Tombola tombola) {
		int numeroDeCoincidencias = 0;
		for (Integer numero: tombola.getSorteo()) {
			if (ultimaJugada.getSorteo().indexOf(numero) > -1) {
				numeroDeCoincidencias ++;
			}
		}
		return numeroDeCoincidencias;
	}

	public String formatearFecha(String fecha){
		String [] numeros = fecha.split(" ");
		return numeros[1] + "/" + Meses.mesesDelAño.get(numeros[3].toLowerCase()) + "/" + numeros[5];
	}
}
