package banca.uy.core.services.implementations;

import banca.uy.core.dto.EstadisticaTombola;
import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.entity.CincoDeOroCombinacion;
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
import org.springframework.scheduling.annotation.Scheduled;
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
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse + (diurna ? " 15:00:00" : " 21:00:00"));

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
	@Scheduled(cron = "${cronExpressionActualizarBaseDeDatos}")
	public void actualizarBaseDeDatos() throws InterruptedException {
		Tombola tombola = tombolaDAO.obtenerUltimaJugadaCompleta();
		DateTime fechaParada = tombola != null ? tombola.getFechaTirada(): new DateTime();
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
			int numeroDeCoincidencias = buscarNumeroDeCoincidencias(ultimaJugada.getSorteo(), tombola.getSorteo());
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

	@Override
	public HashMap<String, Integer> estadisticas() throws InterruptedException {
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas();
		HashMap<String, Integer> estadisticas = new HashMap<>();
		for (Tombola tombola : jugadas) {
			Optional<Tombola> optionalTombola = tombolaDAO.findById(tombola.getId());
			if (optionalTombola.isPresent()) {
				List<Tombola> jugadasAnteriores = tombolaDAO.obtenerJugadasAnteriones(optionalTombola.get(), 3);
				List<Integer> numerosPosibles = eliminarNumerosRepetidos(jugadasAnteriores);
				int numeroDeCoincidencias = buscarNumeroDeCoincidencias(tombola.getSorteo(), numerosPosibles);
				estadisticas.put(optionalTombola.get().getFechaTirada().toString(), numeroDeCoincidencias);
			}
		}
		return estadisticas;
	}

	@Override
	public List<EstadisticaTombola> estadisticasJugadas() throws InterruptedException {
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas();
		List<EstadisticaTombola> estadisticas = new ArrayList<>();
		for (Tombola tombola : jugadas) {
			Optional<Tombola> optionalTombola = tombolaDAO.findById(tombola.getId());
			if (optionalTombola.isPresent()) {
				List<Tombola> jugadasAnteriores = tombolaDAO.obtenerJugadasAnteriones(optionalTombola.get(), 6);
				if (jugadasAnteriores.size() > 2) {
					List<Integer> numerosPosibles = eliminarNumerosRepetidos(jugadasAnteriores);
					int numeroDeCoincidencias = buscarNumeroDeCoincidencias(tombola.getSorteo(), numerosPosibles);
					EstadisticaTombola estadisticaTombola = new EstadisticaTombola(optionalTombola.get(), numeroDeCoincidencias, numerosPosibles);
					estadisticas.add(estadisticaTombola);
				}
			}
		}
		return estadisticas;
	}

	@Override
	public List<EstadisticaTombola> estadisticasJugadasMayorNumeroCoincidencias() throws InterruptedException {
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas();
		List<EstadisticaTombola> estadisticas = new ArrayList<>();
		for (Tombola tombola : jugadas) {
			Optional<Tombola> optionalTombola = tombolaDAO.findById(tombola.getId());
			if (optionalTombola.isPresent()) {
				List<Tombola> jugadasAnteriores = tombolaDAO.obtenerJugadasAnteriones(optionalTombola.get(), 6);
				if (jugadasAnteriores.size() > 4) {
					List<Integer> numerosPosibles = eliminarNumerosRepetidos(jugadasAnteriores);
					int numeroDeCoincidencias = buscarNumeroDeCoincidencias(tombola.getSorteo(), numerosPosibles);
					EstadisticaTombola estadisticaTombola = new EstadisticaTombola(optionalTombola.get(), numeroDeCoincidencias, numerosPosibles);
					estadisticas.add(estadisticaTombola);
				}
			}
		}
		return estadisticas;
	}

	@Override
	public List<EstadisticaTombola> estadisticasJugadasMayorNumeroCoincidenciasRepetidas() throws InterruptedException {
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas();
		List<EstadisticaTombola> estadisticas = new ArrayList<>();
		for (Tombola tombola : jugadas) {
			Optional<Tombola> optionalTombola = tombolaDAO.findById(tombola.getId());
			if (optionalTombola.isPresent()) {
				List<Tombola> jugadasAnteriores = tombolaDAO.obtenerJugadasAnteriones(optionalTombola.get(), 6);
				if (jugadasAnteriores.size() > 4) {
					HashMap<Integer, Integer> estadisticasJugadasRepetidas = buscarNumerosRepetidos(jugadasAnteriores);
					EstadisticaTombola estadisticaTombola = new EstadisticaTombola(optionalTombola.get(), estadisticasJugadasRepetidas);
					estadisticas.add(estadisticaTombola);
				}
			}
		}
		return estadisticas;
	}

	public List<Integer> eliminarNumerosRepetidos(List<Tombola> jugadasAnteriores) {
		List<Integer> numerosPosiblesTombola = obtenerNumeroPosibles();
		for (Tombola tombola: jugadasAnteriores) {
			for (Integer numero: tombola.getSorteo()) {
				numerosPosiblesTombola.remove(numero);
			}
		}
		return numerosPosiblesTombola;
	}

	public HashMap<Integer, Integer> buscarNumerosRepetidos(List<Tombola> jugadasAnteriores) {
		HashMap<Integer, Integer> estadisticasJugadasRepetidas = new HashMap<>();
		for (Tombola tombola: jugadasAnteriores) {
			for (Integer numero: tombola.getSorteo()) {
				if (estadisticasJugadasRepetidas.get(numero) != null) {
					estadisticasJugadasRepetidas.put(numero, estadisticasJugadasRepetidas.get(numero) + 1);
				} else {
					estadisticasJugadasRepetidas.put(numero, 1);
				}
			}
		}
		return estadisticasJugadasRepetidas;
	}

	public List<Integer> obtenerNumeroPosibles() {
		List<Integer> jugadasPosibles = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			jugadasPosibles.add(i);
		}
		return jugadasPosibles;
	}

	public int buscarNumeroDeCoincidencias(List<Integer> ultimaJugada, List<Integer> tombola) {
		int numeroDeCoincidencias = 0;
		for (Integer numero: tombola) {
			if (ultimaJugada.indexOf(numero) > -1) {
				numeroDeCoincidencias ++;
			}
		}
		return numeroDeCoincidencias;
	}

	public String formatearFecha(String fecha){
		String [] numeros = fecha.split(" ");
		return numeros[1] + "/" + Meses.mesesDelAño.get(numeros[3].toLowerCase()) + "/" + numeros[5];
	}

	@Override
	public List<List<Integer>> obtenerTodasLasCombinacionesDeTresTombola() {
		List<List<Integer>> permutaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					List<Integer> permutacion = new ArrayList<>();
					permutacion.add(a);
					permutacion.add(b);
					permutacion.add(c);
					permutaciones.add(permutacion);
				}
			}
		}
		return permutaciones;
	}

	@Override
	public List<List<Integer>> obtenerTodasLasCombinacionesDeCuatroTombola() {
		List<List<Integer>> permutaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						List<Integer> permutacion = new ArrayList<>();
						permutacion.add(a);
						permutacion.add(b);
						permutacion.add(c);
						permutacion.add(d);
						permutaciones.add(permutacion);
					}
				}
			}
		}
		return permutaciones;
	}

	@Override
	public List<List<Integer>> obtenerTodasLasCombinacionesDeCincoTombola() {
		List<List<Integer>> permutaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						for (int e = d + 1; e <= numero; e++) {
							List<Integer> permutacion = new ArrayList<>();
							permutacion.add(a);
							permutacion.add(b);
							permutacion.add(c);
							permutacion.add(d);
							permutacion.add(e);
							permutaciones.add(permutacion);
						}
					}
				}
			}
		}
		return permutaciones;
	}

	@Override
	public List<List<Integer>> obtenerTodasLasCombinacionesDeSeisTombola() {
		List<List<Integer>> permutaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						for (int e = d + 1; e <= numero; e++) {
							for (int f = e + 1; f <= numero; f++) {
								List<Integer> permutacion = new ArrayList<>();
								permutacion.add(a);
								permutacion.add(b);
								permutacion.add(c);
								permutacion.add(d);
								permutacion.add(e);
								permutacion.add(f);
								permutaciones.add(permutacion);
							}
						}
					}
				}
			}
		}
		return permutaciones;
	}

	@Override
	public List<List<Integer>> obtenerTodasLasCombinacionesDeSieteTombola() {
		List<List<Integer>> permutaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						for (int e = d + 1; e <= numero; e++) {
							for (int f = e + 1; f <= numero; f++) {
								for (int g = f + 1; g <= numero; g++) {
									List<Integer> permutacion = new ArrayList<>();
									permutacion.add(a);
									permutacion.add(b);
									permutacion.add(c);
									permutacion.add(d);
									permutacion.add(e);
									permutacion.add(f);
									permutacion.add(g);
									permutaciones.add(permutacion);
								}
							}
						}
					}
				}
			}
		}
		return permutaciones;
	}
}
