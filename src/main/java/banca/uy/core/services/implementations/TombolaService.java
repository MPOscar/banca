package banca.uy.core.services.implementations;

import banca.uy.core.db.TombolaDAO;
import banca.uy.core.entity.Tombola;
import banca.uy.core.repository.ITombolaRepository;
import banca.uy.core.services.interfaces.ITombolaService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TombolaService implements ITombolaService {

	@Autowired
	private ITombolaRepository tombolaRepository;

	@Autowired
	private TombolaDAO tombolaDAO;

	public TombolaService(ITombolaRepository tombolaRepository) {
		this.tombolaRepository = tombolaRepository;
	}

	public Tombola saveTirada(String tirada){
		String fechaTiradaToParse = tirada.substring(0,tirada.indexOf("\r\n"));
		String tiradaTipoNumeros = tirada.substring(tirada.indexOf("\r\n") + 2);
		String tipoTirada = tiradaTipoNumeros.substring(0, tiradaTipoNumeros.indexOf("\r\n"));
		String numerosTirada = tiradaTipoNumeros.substring(tiradaTipoNumeros.indexOf("\r\n") + 2);
		String [] numeros = numerosTirada.split("\r\n");
		List<String> numerosList = Arrays.asList(numeros);
		Set<Integer> numerosTiradaSalvar = new HashSet<>();
		for (String numeroString: numerosList) {
			int numero = Integer.parseInt(numeroString);
			numerosTiradaSalvar.add(numero);
		}

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

		return this.tombolaRepository.save(tombola);
	}

	public Set<Integer> getJugada(String fecha){
		DateTime fechaTirada = new DateTime();
		if(!fecha.equals("")) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
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
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
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
}
