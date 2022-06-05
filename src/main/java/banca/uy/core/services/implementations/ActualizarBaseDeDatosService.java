package banca.uy.core.services.implementations;

import banca.uy.core.db.*;
import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.entity.Param;
import banca.uy.core.entity.Quiniela;
import banca.uy.core.entity.Tombola;
import banca.uy.core.repository.ICincoDeOroRepository;
import banca.uy.core.repository.IQuinielaRepository;
import banca.uy.core.repository.ITombolaRepository;
import banca.uy.core.services.interfaces.IActualizarBaseDeDatosService;
import banca.uy.core.services.interfaces.IEnviarPeticionApiDeLaBancaService;
import banca.uy.core.utils.Meses;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ActualizarBaseDeDatosService implements IActualizarBaseDeDatosService {

	@Autowired
	private ITombolaRepository tombolaRepository;

	@Autowired
	private IQuinielaRepository quinielaRepository;

	@Autowired
	private ICincoDeOroRepository cincoDeOroRepository;

	@Autowired
	private TombolaDAO tombolaDAO;

	@Autowired
	private QuinielaDAO quinielaDAO;

	@Autowired
	private CincoDeOroDAO cincoDeOroDAO;

	@Autowired
	TombolaCombinacionesDeTresDAO tombolaCombinacionesDeTresDAO;

	@Autowired
    IEnviarPeticionApiDeLaBancaService enviarPeticionApiDeLaBancaService;

	@Autowired
	ParamsDAO paramsDAO;

	private static final String ulrTombola = "/resultados/tombola/renderizar_info_sorteo";

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");

	public ActualizarBaseDeDatosService() {
	}

	@Override
	@Scheduled(cron = "${cronExpressionActualizarBaseDeDatos}")
	public void actualizarBaseDeDatos() throws InterruptedException, ParseException {
		Param ultimaFechaDeActualizacion = paramsDAO.findByNombre("ULTIMA_FECHA_DE_ACTUALIZACION");
		String sdate = ultimaFechaDeActualizacion.getValor();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");
		DateTime fechaParada = formatter.parseDateTime(sdate);
		actualizarHastaFechaSeleccionada(fechaParada);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		ultimaFechaDeActualizacion.setValor(formatter.print(new DateTime(calendar)));
		paramsDAO.save(ultimaFechaDeActualizacion);
	}

	public void actualizarHastaFechaSeleccionada(DateTime fechaParada) throws InterruptedException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaParada.toDate());
		DateTime fechaActual = new DateTime();
		while(new DateTime(calendar).isBefore(fechaActual)) {
			DateTime fechaTirada = new DateTime(calendar);
			obtenerSorteos(fechaTirada);
			TimeUnit.MILLISECONDS.sleep(200);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public void obtenerSorteos(
			DateTime fechaTirada
	){
		try {
			String fechaDelSorteo = "?vdia=" + fechaTirada.getDayOfMonth() +
					"&vmes=" + fechaTirada.getMonthOfYear() +
					"&vano=" + fechaTirada.getYear();
			String respuesta = enviarPeticionApiDeLaBancaService.enviarPeticionApiDeLaLoteria(fechaDelSorteo);
			if (!respuesta.contains("En el día de la fecha no se han realizado sorteos")) {
				String mensaje = respuesta.substring(respuesta.indexOf("Quiniela y Tómbola"));
				if (respuesta.contains("LOTERIAS/2011/cabezal_quinielas_vespertina.png")) {
					mensaje = mensaje.substring(mensaje.indexOf("<td><div align=\"center\" class=\"text_azul_3\">"));
					String quinielaDiurna = mensaje.substring(0, mensaje.indexOf("</table></td>"));
					List<Integer> quinielaDiurnaJugada = obtenerNumerosDeLaJugada(quinielaDiurna);
					salvarTiradaQuiniela(fechaTirada, quinielaDiurnaJugada, true);

					mensaje = mensaje.substring(mensaje.indexOf("</table></td>"));
					mensaje = mensaje.substring(mensaje.indexOf("<td><div align=\"center\" class=\"text_azul_3\">"));
					String tombolaDiurna = mensaje.substring(0, mensaje.indexOf("</table></td>"));
					List<Integer> tombolaDiurnaJugada = obtenerNumerosDeLaJugada(tombolaDiurna);
					salvarTiradaTombola(fechaTirada, tombolaDiurnaJugada, true);
					mensaje = mensaje.substring(mensaje.indexOf("</table></td>"));
				}

				if (respuesta.contains("LOTERIAS/2011/cabezal_quinielas_nocturno.png")) {
					mensaje = mensaje.substring(mensaje.indexOf("<div align=\"center\" class=\"text_azul_3\">"));
					String quinielaNocturna = mensaje.substring(0, mensaje.indexOf("</table></td>"));
					List<Integer> quinielaNocturnaJugada = obtenerNumerosDeLaJugada(quinielaNocturna);
					salvarTiradaQuiniela(fechaTirada, quinielaNocturnaJugada, false);

					mensaje = mensaje.substring(mensaje.indexOf("</table></td>"));
					mensaje = mensaje.substring(mensaje.indexOf("<div align=\"center\" class=\"text_azul_3\">"));
					String tombolaNocturna = mensaje.substring(0, mensaje.indexOf("</table></td>"));
					List<Integer> tombolaNocturnaJugada = obtenerNumerosDeLaJugada(tombolaNocturna);
					salvarTiradaTombola(fechaTirada, tombolaNocturnaJugada, false);
					mensaje = mensaje.substring(mensaje.indexOf("</table></td>"));
				}

				if (respuesta.contains("LOTERIAS/2011/logo_5deoro.png")) {
					mensaje = mensaje.substring(mensaje.indexOf("LOTERIAS/2011/logo_5deoro.png"));
					mensaje = mensaje.substring(mensaje.indexOf("</table></td>"));
					mensaje = mensaje.substring(mensaje.indexOf("<div align=\"center\" class=\"text_azul_3\">"));
					String cincoDeOro = mensaje.substring(0, mensaje.indexOf("</table></td>"));
					List<Integer> cincoDeOroJugada = obtenerNumerosDeLaJugada(cincoDeOro);

					mensaje = mensaje.substring(mensaje.indexOf("</table></td>"));
					String resultados = mensaje.substring(0, mensaje.indexOf("<td><img src=\"LOTERIAS/2011/gif_2.gif\""));
					resultados = eliminarCaracteres(resultados);
					String resultadosCincoDeOro = resultados.substring(0, resultados.toLowerCase().lastIndexOf("pozo"));
					String resultadosPozoPlata = resultados.substring(resultados.toLowerCase().lastIndexOf("pozo"));
					List<String> cincoDeOroResultados = obtenerResultadoDelCincoDeOro(resultadosCincoDeOro);
					List<String> pozoDePlataResultados = obtenerResultadoDelCincoDeOro(resultadosPozoPlata);

					mensaje = mensaje.substring(mensaje.indexOf("<td><img src=\"LOTERIAS/2011/gif_2.gif\""));

					String sorteoRevancha = mensaje.substring(0, mensaje.indexOf("</table></td>"));
					List<Integer> sorteoRevanchaJugada = obtenerNumerosDeLaJugada(sorteoRevancha);

					mensaje = mensaje.substring(mensaje.indexOf("</table></td>"));
					mensaje = mensaje.substring(mensaje.indexOf("</td>"));
					String resultadosRebancha = mensaje.substring(0, mensaje.indexOf("</table></td>"));
					resultadosRebancha = eliminarCaracteres(resultadosRebancha);
					List<String> sorteoRevanchaResultados = obtenerResultadoDelCincoDeOro(resultadosRebancha);
					salvarTiradaCincoDeOro(
							fechaTirada,
							cincoDeOroJugada,
							sorteoRevanchaJugada,
							cincoDeOroResultados,
							pozoDePlataResultados,
							sorteoRevanchaResultados
					);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String eliminarCaracteres(String mensaje) {
		mensaje = mensaje.replaceAll("<img.*?/>", "");
		mensaje = mensaje.replaceAll("<img.*?>", "");
		mensaje = mensaje.replaceAll("<td.*?>", "");
		mensaje = mensaje.replaceAll("<div align=\"center\" class=\"text_azul_3\">", "");
		mensaje = mensaje.replaceAll("</td>", "");
		mensaje = mensaje.replaceAll("<tr valign=\"top\">", "");
		mensaje = mensaje.replaceAll("</div>", "");
		mensaje = mensaje.replaceAll("&nbsp;", "");
		mensaje = mensaje.replaceAll("</tr>", "");
		mensaje = mensaje.replaceAll("<tr>", "");
		mensaje = mensaje.replaceAll("<table.*?>", "");
		mensaje = mensaje.replaceAll("</table>", "");
		mensaje = mensaje.replaceAll("<strong>", "");
		mensaje = mensaje.replaceAll("</strong>", "");
		mensaje = mensaje.replaceAll("</form>", "");
		mensaje = mensaje.replaceAll("\\r", "");
		mensaje = mensaje.replaceAll("\\t", "");
		return mensaje;
	}

	public List<Integer> obtenerNumerosDeLaJugada(String mensaje) {
		mensaje = eliminarCaracteres(mensaje);
		String [] numeros = mensaje.split("\\n");
		List<Integer> numerosEnLaJugada = obtenerNumeros(numeros);
		return numerosEnLaJugada;
	}

	public List<String> obtenerResultadoDelCincoDeOro(String mensaje) {
		mensaje = eliminarCaracteres(mensaje);
		String [] numeros = mensaje.split("\\n");
		List<String> resultados = new ArrayList<>();
		int numeroDeAciertos = 0;
		for (String numero: numeros) {
			String numeroSinEspacios = numero.replace(" ", "");
			if (!numeroSinEspacios.isEmpty() && numero.contains("$")) {
				numero = numeroSinEspacios.substring(numeroSinEspacios.indexOf("$") + 1);
				resultados.add(numero);
			} else if (
					!numeroSinEspacios.isEmpty() &&
					!numeroSinEspacios.contains("$") &&
					!numeroSinEspacios.toLowerCase().contains("sinacierto")
			) {
				numeroDeAciertos += obtenerNumeroDeAciertos(numero);
			}
		}
		resultados.add(String.valueOf(numeroDeAciertos));
		return resultados;
	}

	public int obtenerNumeroDeAciertos(String numero) {
		String[] boletosGanadores = numero.split(" ");
		int numeroDeAciertos = 0;
		for (String boleto: boletosGanadores) {
			boleto = boleto.replace(" ", "");
			if(!boleto.isEmpty()) {
				numeroDeAciertos ++;
			}
		}
		return numeroDeAciertos;
	}

	public List<Integer> obtenerNumeros(String [] numeros) {
		List<Integer> numerosEnLaJugada = new ArrayList<>();
		for (String numero: numeros) {
			numero = numero.replace(" ", "");
			if(!numero.isEmpty()) {
				try {
					Integer numeroEnLaJugada = Integer.parseInt(numero);
					numerosEnLaJugada.add(numeroEnLaJugada);
				} catch (Exception exception) {
					// no hacer nada
				}
			}
		}
		return numerosEnLaJugada;
	}

	public Tombola salvarTiradaTombola(
			DateTime fechaDeTirada,
			List<Integer> numerosTiradaSalvar,
			boolean diurna
	){
		DateTimeFormatter formatterFechaDeTirada = DateTimeFormat.forPattern("dd/MM/YYYY");
		String fechaTiradaToParse = formatterFechaDeTirada.print(fechaDeTirada);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse + (diurna ? " 15:00:00" : " 21:00:00"));
		Tombola tombola = tombolaRepository.findFirstByFechaTirada(fechaTirada);
		if (tombola == null) {
			tombola = new Tombola(fechaTirada);
		}
		tombola.setEsDiurno(diurna);
		tombola.setSorteo(numerosTiradaSalvar);
		return this.tombolaDAO.save(tombola);
	}

	public Quiniela salvarTiradaQuiniela(
			DateTime fechaDeTirada,
			List<Integer> numerosTiradaSalvar,
			boolean diurna
	) {
		DateTimeFormatter formatterFechaDeTirada = DateTimeFormat.forPattern("dd/MM/YYYY");
		String fechaTiradaToParse = formatterFechaDeTirada.print(fechaDeTirada);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse + (diurna ? " 15:00:00" : " 21:00:00"));
		Quiniela quiniela = quinielaRepository.findFirstByFechaTirada(fechaTirada);
		if (quiniela == null) {
			quiniela = new Quiniela(fechaTirada);
		}
		quiniela.setEsDiurno(diurna);
		quiniela.setSorteo(numerosTiradaSalvar);
		return this.quinielaDAO.save(quiniela);
	}

	public void salvarTiradaCincoDeOro(
			DateTime fechaDeTirada,
			List<Integer> numerosCincoDeOro,
			List<Integer> numerosCincoDeOroRebancha,
			List<String> resultadosCincoDeOro,
			List<String> pozoDePlataResultados,
			List<String> resultadosSorteoRebancha
	) {
		DateTimeFormatter formatterFechaDeTirada = DateTimeFormat.forPattern("dd/MM/YYYY");
		String fechaTiradaToParse = formatterFechaDeTirada.print(fechaDeTirada);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse);

		CincoDeOro cincoDeOro = cincoDeOroRepository.findFirstByFechaTirada(fechaTirada);
		if (cincoDeOro == null) {
			cincoDeOro = new CincoDeOro(fechaTirada);
		}
		cincoDeOro.setCincoDeOro(numerosCincoDeOro);
		cincoDeOro.setRebancha(numerosCincoDeOroRebancha);

		cincoDeOro.setPozoDeOro(resultadosCincoDeOro.get(0));
		cincoDeOro.setNumeroAciertosPozoDeOro(resultadosCincoDeOro.get(1));

		cincoDeOro.setPozoDePlata(pozoDePlataResultados.get(0));
		cincoDeOro.setNumeroAciertosPozoDePlata(pozoDePlataResultados.get(1));

		cincoDeOro.setPozoDeRevancha(resultadosSorteoRebancha.get(0));
		cincoDeOro.setNumeroAciertosPozoRevancha(resultadosSorteoRebancha.get(1));

		cincoDeOroDAO.save(cincoDeOro);
	}



	public String formatearFecha(String fecha){
		String [] numeros = fecha.split(" ");
		return numeros[1] + "/" + Meses.mesesDelAño.get(numeros[3].toLowerCase()) + "/" + numeros[5];
	}

}
