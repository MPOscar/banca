package banca.uy.core.services.implementations;

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

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse);

		Tombola tombola = tombolaRepository.findFirstByFechaTirada(fechaTirada);
		if(tombola == null){
			tombola = new Tombola(fechaTirada);
		}

		if(diurna){
			tombola.setSorteoVespertino(numerosTiradaSalvar);
		} else {
			tombola.setSorteoNocturno(numerosTiradaSalvar);
		}
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
			TimeUnit.SECONDS.sleep(1);
			obtenerTiradaYGuardarEnBaseDeDatos(tiradaNocturna);
			TimeUnit.SECONDS.sleep(1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	public Set<Integer> getJugada(String fecha){
		DateTime fechaTirada = new DateTime();
		if(!fecha.equals("")) {
			fechaTirada = formatter.parseDateTime(fecha);
		}
		Set<Integer> jugada = new HashSet<>();
		List<Tombola> ultimosSorteos = tombolaDAO.findAllSortByFechaTirada(fechaTirada);
		List<Integer> numerosFinal = new ArrayList<>();
		List<Integer> numerosPosiblesNoSalen = new ArrayList<>();
		for (Tombola tombola: ultimosSorteos) {
			for (Integer numero: tombola.getSorteoNocturno()) {
				if(numerosFinal.size() < 40){
					numerosFinal.add(numero);
				}else{
					break;
				}
			}

			for (Integer numero: tombola.getSorteoVespertino()) {
				if(numerosFinal.size() < 40){
					numerosFinal.add(numero);
				}else{
					break;
				}
			}

			if(numerosFinal.size() == 40){
				break;
			}
		}
		for (Integer numero: numerosFinal) {
			numerosPosiblesNoSalen.add(numero);
			numerosPosiblesNoSalen.add(101 - numero);
		}
		for (int i = 0; i < 100; i ++) {
			if(!numerosPosiblesNoSalen.contains(i)){
				jugada.add(i);
			}
		}
		return jugada;
	}

	public List<String> getJugadaRepetidas(String fecha){
		DateTime fechaTirada = new DateTime();
		if(!fecha.equals("")) {
			fechaTirada = formatter.parseDateTime(fecha);
		}
		List<Tombola> ultimosSorteos = tombolaDAO.findAllSortByFechaTirada(fechaTirada);
		HashMap<String, Integer> cantidadDeVecesRepetido = new HashMap<>();
		for (Tombola tombola: ultimosSorteos) {
			for (Integer numero : tombola.getSorteoNocturno()) {
				if (cantidadDeVecesRepetido.containsKey(numero.toString())) {
					cantidadDeVecesRepetido.put(numero.toString(), (cantidadDeVecesRepetido.get(numero.toString()) + 1));
				} else {
					cantidadDeVecesRepetido.put(numero.toString(), 1);
				}
			}

			for (Integer numero : tombola.getSorteoVespertino()) {
				if (cantidadDeVecesRepetido.containsKey(numero.toString())) {
					cantidadDeVecesRepetido.put(numero.toString(), (cantidadDeVecesRepetido.get(numero.toString()) + 1));
				} else {
					cantidadDeVecesRepetido.put(numero.toString(), 1);
				}
			}
		}
		List<String> cantidadDeVecesRepetidosOrdenados = new ArrayList<>();
		for(int i = 0; i < 100; i++){
			if(cantidadDeVecesRepetido.containsKey(Integer.toString(i))){
				cantidadDeVecesRepetidosOrdenados.add(i, (Integer.toString(i) + " ---> "  +cantidadDeVecesRepetido.get(Integer.toString(i))));
			}else{
				cantidadDeVecesRepetidosOrdenados.add(i, Integer.toString(i) + " ---> 0");
			}
		}
		return cantidadDeVecesRepetidosOrdenados;
	}

	public String formatearFecha(String fecha){
		String [] numeros = fecha.split(" ");
		return numeros[1] + "/" + Meses.mesesDelAño.get(numeros[3].toLowerCase()) + "/" + numeros[5];
	}
}
