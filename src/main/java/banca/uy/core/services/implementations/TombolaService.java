package banca.uy.core.services.implementations;

import banca.uy.core.db.TombolaCombinacionesDeTresDAO;
import banca.uy.core.dto.EstadisticaTombola;
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
	TombolaCombinacionesDeTresDAO tombolaCombinacionesDeTresDAO;

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
	public List<EstadisticaTombola> estadisticasUltimaJugada(int page, int limit) throws InterruptedException {
		List<Tombola> jugadas = tombolaDAO.obtenerUltimasJugadas(page, limit);
		List<EstadisticaTombola> estadisticas = new ArrayList<>();
		for (Tombola tombola : jugadas) {
			Optional<Tombola> optionalTombola = tombolaDAO.findById(tombola.getId());
			if (optionalTombola.isPresent()) {
				List<Tombola> jugadasAnteriores = tombolaDAO.obtenerJugadasAnteriones(optionalTombola.get(), 10);
				List<Integer> numeros = obtenerNumeros(
						jugadasAnteriores.get(0).getSorteo(),
						1
				);
				List<Integer> numerosMenos = obtenerNumeros(
						jugadasAnteriores.get(1).getSorteo(),
						1
				);
				List<Integer> numerosPosiblesTombola = obtenerNumeroPosibles();
				List<Integer> jugadasRepetidas1 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(1).getSorteo());
				List<Integer> jugadasRepetidas2 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(2).getSorteo());
				List<Integer> jugadasRepetidas3 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(3).getSorteo());
				List<Integer> jugadasRepetidas4 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(4).getSorteo());
				List<Integer> jugadasRepetidas5 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(5).getSorteo());
				List<Integer> jugadasRepetidas6 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(6).getSorteo());
				List<Integer> jugadasRepetidas7 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(7).getSorteo());
				List<Integer> jugadasRepetidas8 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(8).getSorteo());
				List<Integer> jugadasRepetidas9 = obtenerJugadasRepetidas(jugadasAnteriores.get(0).getSorteo(), jugadasAnteriores.get(9).getSorteo());

				List<Integer> jugadasRepetidas10 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(2).getSorteo());
				List<Integer> jugadasRepetidas11 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(3).getSorteo());
				List<Integer> jugadasRepetidas12 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(4).getSorteo());
				List<Integer> jugadasRepetidas13 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(5).getSorteo());
				List<Integer> jugadasRepetidas14 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(6).getSorteo());
				List<Integer> jugadasRepetidas15 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(7).getSorteo());
				List<Integer> jugadasRepetidas16 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(8).getSorteo());
				List<Integer> jugadasRepetidas17 = obtenerJugadasNoRepetidas(jugadasAnteriores.get(1).getSorteo(), jugadasAnteriores.get(8).getSorteo());

				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas1);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas2);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas3);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas4);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas5);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas6);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas7);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas8);
				eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasRepetidas9);

				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(0).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(1).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(2).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(3).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(4).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(5).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(6).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(7).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, jugadasAnteriores.get(0).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, numeros);
				//(numerosPosiblesTombola, jugadasAnteriores.get(3).getSorteo());
				//eliminarNumerosRepetidos(numerosPosiblesTombola, numerosMenos);
				//numerosPosiblesTombola = eliminarNumerosConsecutivos(numerosPosiblesTombola);
				//(numerosPosiblesTombola, numerosMenos);
				int numeroDeCoincidencias = buscarNumeroDeCoincidencias(tombola.getSorteo(), jugadasAnteriores.get(2).getSorteo());
				EstadisticaTombola estadisticaTombola = new EstadisticaTombola(
						optionalTombola.get(),
						numeroDeCoincidencias,
						new ArrayList<>()
				);
				estadisticas.add(estadisticaTombola);
				estadisticaTombola.setTotalDeNumerosPosibles(numerosPosiblesTombola.size());
				/*if (jugadasAnteriores.size() > 4) {
					List<Integer> numerosPosibles = eliminarNumerosRepetidos(jugadasAnteriores);
					int numeroDeCoincidencias = buscarNumeroDeCoincidencias(tombola.getSorteo(), numerosPosibles);
					EstadisticaTombola estadisticaTombola = new EstadisticaTombola(optionalTombola.get(), numeroDeCoincidencias, numerosPosibles);
					estadisticas.add(estadisticaTombola);
				}*/
			}
		}
		return estadisticas;
	}

	public List<Integer> obtenerJugadasRepetidas(List<Integer> sorteo, List<Integer> jugadas) {
		List<Integer> jugadasRepetidas = new ArrayList<>();
		for (Integer numero: jugadas) {
			if (sorteo.indexOf(numero) > -1) {
				jugadasRepetidas.add(numero);
			}
		}
		return jugadasRepetidas;
	}

	public List<Integer> obtenerJugadasNoRepetidas(List<Integer> sorteo, List<Integer> jugadas) {
		List<Integer> jugadasRepetidas = new ArrayList<>();
		for (Integer numero: jugadas) {
			if (sorteo.indexOf(numero) == -1) {
				jugadasRepetidas.add(numero);
			}
		}
		return jugadasRepetidas;
	}

	public List<Integer> obtenerNumeros(List<Integer> sorteo, int numeroASumar) {
		List<Integer> numeros = new ArrayList<>();
		for (Integer numero: sorteo) {
			double numeroNuevo = numero + 3.14159;
			if (numeroNuevo > 100) {
				numeroNuevo = numeroNuevo - 100;
			} else if (numeroNuevo < 0) {
				numeroNuevo = 100 - numeroNuevo;
			}
			numeros.add((int) numeroNuevo);
		}
		return numeros;
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

	public List<Integer> eliminarNumerosRepetidos(List<Integer> numerosPosiblesTombola, List<Integer> jugadasAnteriores) {
		for (Integer numero: jugadasAnteriores) {
			numerosPosiblesTombola.remove(numero);
		}
		return numerosPosiblesTombola;
	}

	public List<Integer> eliminarNumerosConsecutivos(List<Integer> numerosPosiblesTombola) {
		List<Integer> numeros = new ArrayList<>();
		for (int i = 0; i < numerosPosiblesTombola.size() -1; i++) {
			if(numerosPosiblesTombola.get(i) + 1 != numerosPosiblesTombola.get(i + 1)) {
				numeros.add(numerosPosiblesTombola.get(i));
			} else {
				i += 2;
			}
		}
		return numeros;
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

}
