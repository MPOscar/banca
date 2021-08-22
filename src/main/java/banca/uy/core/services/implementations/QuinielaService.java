package banca.uy.core.services.implementations;

import banca.uy.core.db.QuinielaDAO;
import banca.uy.core.db.TombolaDAO;
import banca.uy.core.entity.Quiniela;
import banca.uy.core.entity.Tombola;
import banca.uy.core.repository.IQuinielaRepository;
import banca.uy.core.services.interfaces.IQuinielaService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuinielaService implements IQuinielaService {

	@Autowired
	private IQuinielaRepository quinielaRepository;

	@Autowired
	private QuinielaDAO quinielaDAO;

	public QuinielaService(IQuinielaRepository quinielaRepository) {
		this.quinielaRepository = quinielaRepository;
	}

	public Quiniela saveTirada(String tirada){
		String fechaTiradaToParse = tirada.substring(0,tirada.indexOf("\r\n"));
		String tiradaTipoNumeros = tirada.substring(tirada.indexOf("\r\n") + 2);
		String tipoTirada = tiradaTipoNumeros.substring(0, tiradaTipoNumeros.indexOf("\r\n"));
		String numerosTirada = tiradaTipoNumeros.substring(tiradaTipoNumeros.indexOf("\r\n") + 2);
		String [] numeros = numerosTirada.split("\r\n");
		List<String> numerosList = Arrays.asList(numeros);
		Set<Integer> numerosTiradaSalvar = new HashSet<>();
		for (String numeroString: numerosList) {
			int numero = Integer.parseInt(numeroString.substring(numeroString.indexOf(".") + 1));
			numerosTiradaSalvar.add(numero);
		}

		boolean diurna = tipoTirada.indexOf("Vespertino") > -1 ? true : false;

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
		DateTime fechaTirada = formatter.parseDateTime(fechaTiradaToParse);

		Quiniela quiniela = quinielaRepository.findFirstByFechaTirada(fechaTirada);
		if(quiniela == null){
			quiniela = new Quiniela(fechaTirada);
		}

		if(diurna){
			quiniela.setSorteoVespertino(numerosTiradaSalvar);
		} else {
			quiniela.setSorteoNocturno(numerosTiradaSalvar);
		}

		return this.quinielaRepository.save(quiniela);
	}

	public Set<Integer> getJugada(){
		Set<Integer> jugada = new HashSet<>();
		List<Quiniela> ultimosSorteos = quinielaDAO.findAllSortByFechaTirada();
		Set<Integer> numerosFinal = new HashSet<>();
		for (Quiniela quiniela: ultimosSorteos) {
			for (Integer numero: quiniela.getSorteoNocturno()) {
				if(numerosFinal.size() < 100){
					numerosFinal.add(numero);
				}else{
					break;
				}
			}

			for (Integer numero: quiniela.getSorteoVespertino()) {
				if(numerosFinal.size() < 100){
					numerosFinal.add(numero);
				}else{
					break;
				}
			}

			if(numerosFinal.size() == 90){
				break;
			}
		}

		Set<Integer> menosPosibles = new HashSet<>();
		for (Integer integer: numerosFinal) {
			if(menosPosibles.size() < 90) {
				menosPosibles.add(integer);
				menosPosibles.add(101 - integer);
			}else{
				break;
			}
		}

		for (int i = 1; i <= 100; i ++) {
			if(!menosPosibles.contains(i)){
				jugada.add(i);
			}
		}
		return jugada;
	}

	public Set<Integer> getNumerosSinSalir(String fecha){
		DateTime fechaTirada = new DateTime();
		if(!fecha.equals("")) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY");
			fechaTirada = formatter.parseDateTime(fecha);
		}
		Set<Integer> numerosSinSalir = new HashSet<>();
		Set<Integer> numerosYaSalieron = new HashSet<>();
		Set<Integer> numerosYaSalieronRepetidos = new HashSet<>();
		List<Quiniela> ultimosSorteos = quinielaDAO.findAllSortByFechaTirada(fechaTirada);

		for (Quiniela quiniela: ultimosSorteos) {
			for (Integer numero: quiniela.getSorteoNocturno()) {
				if(numerosYaSalieron.size() < 980){
					if(numerosYaSalieron.contains(numero)){
						numerosYaSalieronRepetidos.add(numero);
					}else{
						numerosYaSalieron.add(numero);
					}

				}else{
					break;
				}
			}

			for (Integer numero: quiniela.getSorteoVespertino()) {
				if(numerosYaSalieron.size() < 980){
					if(numerosYaSalieron.contains(numero)){
						numerosYaSalieronRepetidos.add(numero);
					}else{
						numerosYaSalieron.add(numero);
					}

				}else{
					break;
				}
			}

			if(numerosYaSalieron.size() == 980){
				break;
			}
		}

		for(int i = 0; i < 1000; i++){
			if(!numerosYaSalieron.contains(i)){
				numerosSinSalir.add(i);
			}
		}

		return numerosYaSalieronRepetidos;
	}
}
