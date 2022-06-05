package banca.uy.core.services.implementations;

import banca.uy.core.db.TombolaCombinacionesDeSieteDAO;
import banca.uy.core.db.TombolaDAO;
import banca.uy.core.entity.Tombola;
import banca.uy.core.entity.TombolaCombinacionesDeSiete;
import banca.uy.core.services.interfaces.ITombolaCombinacionesDeSieteService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TombolaCombinacionesDeSieteService implements ITombolaCombinacionesDeSieteService {

	@Autowired
	private TombolaDAO tombolaDAO;

	@Autowired
	TombolaCombinacionesDeSieteDAO tombolaCombinacionesDeSieteDAO;

	public TombolaCombinacionesDeSieteService() {
	}

	@Override
	public void inicializarTodasLasJugadasDeSieteTombola() {
		tombolaCombinacionesDeSieteDAO.eliminarCombinacionesDeSiete();
		HashMap<String, Integer> jugadasDeSiete = obtenerTodasLasJugadasDeSieteTombola();
		DateTime fechaUltimaTirada = tombolaDAO.obtenerUltimaFechaDeTirada();
		for (Map.Entry<String, Integer> entry : jugadasDeSiete.entrySet()) {
			TombolaCombinacionesDeSiete tombolaCombinacionesDeSiete = new TombolaCombinacionesDeSiete(
					entry.getKey(),
					entry.getValue(),
					fechaUltimaTirada
			);
			tombolaCombinacionesDeSieteDAO.save(tombolaCombinacionesDeSiete);
		}
	}

	@Override
	public void actualizarTodasLasJugadasDeSieteTombola() {
		DateTime ultimaFechaDeActualizacion = tombolaCombinacionesDeSieteDAO.obtenerUltimaFechaDeActualizacion();
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas(ultimaFechaDeActualizacion);
		for (Tombola tombola: jugadas) {
			List<String> combinaciones = obtenerTodasLasCombinacionesDeSieteTombola(tombola.getSorteo());
			for (String combinacion: combinaciones) {
				TombolaCombinacionesDeSiete tombolaCombinacionesDeSiete = tombolaCombinacionesDeSieteDAO.findFirstByCombinacion(combinacion);
				tombolaCombinacionesDeSiete.setNumeroDeVecesQueHaSalido(tombolaCombinacionesDeSiete.getNumeroDeVecesQueHaSalido() + 1);
				tombolaCombinacionesDeSiete.setFechaTirada(tombola.getFechaTirada());
				tombolaCombinacionesDeSieteDAO.save(tombolaCombinacionesDeSiete);
			}
		}
	}

	@Override
	public HashMap<String, Integer> obtenerTodasLasJugadasDeSieteTombola() {
		HashMap<String, Integer> jugadasDeTres = obtenerTodasLasCombinacionesDeSieteTombola();
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas();
		for (Tombola tombola: jugadas) {
			List<String> combinaciones = obtenerTodasLasCombinacionesDeSieteTombola(tombola.getSorteo());
			for (String combinacion: combinaciones) {
				jugadasDeTres.put(combinacion, jugadasDeTres.get(combinacion) + 1);
			}
		}
		return jugadasDeTres;
	}

	public HashMap<String, Integer> obtenerTodasLasCombinacionesDeSieteTombola() {
		HashMap<String, Integer> combinaciones = new HashMap<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						for (int e = d + 1; e <= numero; e++) {
							for (int f = e + 1; f <= numero; f++) {
								for (int g = f + 1; g <= numero; g++) {
									String combinacion = a + "-" + b + "-" + c + "-" + d + "-" + e + "-" + f + "-" + g;
									combinaciones.put(combinacion, 0);
								}
							}
						}
					}
				}
			}
		}
		return combinaciones;
	}

	public List<String> obtenerTodasLasCombinacionesDeSieteTombola(List<Integer> sorteo) {
		List<String> combinaciones = new ArrayList<>();
		for (int a = 0; a < sorteo.size(); a++) {
			for (int b = a + 1; b < sorteo.size(); b++) {
				for (int c = b + 1; c < sorteo.size(); c++) {
					for (int d = c + 1; d < sorteo.size(); d++) {
						for (int e = d + 1; e < sorteo.size(); e++) {
							for (int f = e + 1; f < sorteo.size(); f++) {
								for (int g = f + 1; g < sorteo.size(); g++) {
									String combinacion = sorteo.get(a) + "-" + sorteo.get(b) + "-" + sorteo.get(c) + "-" + sorteo.get(d) + "-" + sorteo.get(e) + "-" + sorteo.get(f) + "-" + sorteo.get(g);
									combinaciones.add(combinacion);
								}
							}
						}
					}
				}
			}
		}
		return combinaciones;
	}

}
