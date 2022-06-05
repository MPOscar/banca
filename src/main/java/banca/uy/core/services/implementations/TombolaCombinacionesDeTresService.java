package banca.uy.core.services.implementations;

import banca.uy.core.db.TombolaCombinacionesDeTresDAO;
import banca.uy.core.db.TombolaDAO;
import banca.uy.core.entity.Tombola;
import banca.uy.core.entity.TombolaCombinacionesDeTres;
import banca.uy.core.services.interfaces.ITombolaCombinacionesDeTresService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TombolaCombinacionesDeTresService implements ITombolaCombinacionesDeTresService {

	@Autowired
	private TombolaDAO tombolaDAO;

	@Autowired
	TombolaCombinacionesDeTresDAO tombolaCombinacionesDeTresDAO;

	public TombolaCombinacionesDeTresService() {
	}

	@Override
	public HashMap<String, Integer> obtenerTodasLasJugadasDeTresTombola() {
		HashMap<String, Integer> jugadasDeTres = obtenerTodasLasCombinacionesPosiblesDeTresTombola();
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas();
		for (Tombola tombola: jugadas) {
			List<String> combinaciones = obtenerTodasLasCombinacionesPosiblesDeTresTombola(tombola.getSorteo());
			for (String combinacion: combinaciones) {
				jugadasDeTres.put(combinacion, jugadasDeTres.get(combinacion) + 1);
			}
		}
		return jugadasDeTres;
	}

	@Override
	public void inicializarTodasLasJugadasDeTresTombola() {
		tombolaCombinacionesDeTresDAO.eliminarCombinacionesDeTres();
		HashMap<String, Integer> jugadasDeTres = obtenerTodasLasJugadasDeTresTombola();
		DateTime fechaUltimaTirada = tombolaDAO.obtenerUltimaFechaDeTirada();
		for (Map.Entry<String, Integer> entry : jugadasDeTres.entrySet()) {
			TombolaCombinacionesDeTres tombolaCombinacionesDeTres = new TombolaCombinacionesDeTres(
					entry.getKey(),
					entry.getValue(),
					fechaUltimaTirada
			);
			tombolaCombinacionesDeTresDAO.save(tombolaCombinacionesDeTres);
		}
	}

	@Override
	public void actualizarTodasLasJugadasDeTresTombola() {
		DateTime ultimaFechaDeActualizacion = tombolaCombinacionesDeTresDAO.obtenerUltimaFechaDeActualizacion();
		List<Tombola> jugadas = tombolaDAO.obtenerTodasLasJugadas(ultimaFechaDeActualizacion);
		for (Tombola tombola: jugadas) {
			List<String> combinaciones = obtenerTodasLasCombinacionesPosiblesDeTresTombola(tombola.getSorteo());
			for (String combinacion: combinaciones) {
				TombolaCombinacionesDeTres tombolaCombinacionesDeTres = tombolaCombinacionesDeTresDAO.findFirstByCombinacion(combinacion);
				tombolaCombinacionesDeTres.setNumeroDeVecesQueHaSalido(tombolaCombinacionesDeTres.getNumeroDeVecesQueHaSalido() + 1);
				tombolaCombinacionesDeTres.setFechaTirada(tombola.getFechaTirada());
				tombolaCombinacionesDeTresDAO.save(tombolaCombinacionesDeTres);
			}
		}
	}

	public List<List<Integer>> obtenerTodasLasCombinacionesDeTresTombola() {
		List<List<Integer>> combinaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					List<Integer> combinacion = new ArrayList<>();
					combinacion.add(a);
					combinacion.add(b);
					combinacion.add(c);
					combinaciones.add(combinacion);
				}
			}
		}
		return combinaciones;
	}

	public HashMap<String, Integer> obtenerTodasLasCombinacionesPosiblesDeTresTombola() {
		HashMap<String, Integer> combinaciones = new HashMap<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					String combinacion = a + "-" + b + "-" + c;
					combinaciones.put(combinacion, 0);
				}
			}
		}
		return combinaciones;
	}

	public List<String> obtenerTodasLasCombinacionesPosiblesDeTresTombola(List<Integer> sorteo) {
		List<String> combinaciones = new ArrayList<>();
		for (int a = 0; a < sorteo.size(); a++) {
			for (int b = a + 1; b < sorteo.size(); b++) {
				for (int c = b + 1; c < sorteo.size(); c++) {
					String combinacion = sorteo.get(a) + "-" + sorteo.get(b) + "-" + sorteo.get(c);
					combinaciones.add(combinacion);
				}
			}
		}
		return combinaciones;
	}

	public List<List<Integer>> obtenerTodasLasCombinacionesDeCuatroTombola() {
		List<List<Integer>> combinaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						List<Integer> combinacion = new ArrayList<>();
						combinacion.add(a);
						combinacion.add(b);
						combinacion.add(c);
						combinacion.add(d);
						combinaciones.add(combinacion);
					}
				}
			}
		}
		return combinaciones;
	}

	public List<List<Integer>> obtenerTodasLasCombinacionesDeCincoTombola() {
		List<List<Integer>> combinaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						for (int e = d + 1; e <= numero; e++) {
							List<Integer> combinacion = new ArrayList<>();
							combinacion.add(a);
							combinacion.add(b);
							combinacion.add(c);
							combinacion.add(d);
							combinacion.add(e);
							combinaciones.add(combinacion);
						}
					}
				}
			}
		}
		return combinaciones;
	}

	public List<List<Integer>> obtenerTodasLasCombinacionesDeSeisTombola() {
		List<List<Integer>> combinaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						for (int e = d + 1; e <= numero; e++) {
							for (int f = e + 1; f <= numero; f++) {
								List<Integer> combinacion = new ArrayList<>();
								combinacion.add(a);
								combinacion.add(b);
								combinacion.add(c);
								combinacion.add(d);
								combinacion.add(e);
								combinacion.add(f);
								combinaciones.add(combinacion);
							}
						}
					}
				}
			}
		}
		return combinaciones;
	}

	public List<List<Integer>> obtenerTodasLasCombinacionesDeSieteTombola() {
		List<List<Integer>> combinaciones = new ArrayList<>();
		int numero = 99;
		for (int a = 0; a <= numero; a++) {
			for (int b = a + 1; b <= numero; b++) {
				for (int c = b + 1; c <= numero; c++) {
					for (int d = c + 1; d <= numero; d++) {
						for (int e = d + 1; e <= numero; e++) {
							for (int f = e + 1; f <= numero; f++) {
								for (int g = f + 1; g <= numero; g++) {
									List<Integer> combinacion = new ArrayList<>();
									combinacion.add(a);
									combinacion.add(b);
									combinacion.add(c);
									combinacion.add(d);
									combinacion.add(e);
									combinacion.add(f);
									combinacion.add(g);
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
