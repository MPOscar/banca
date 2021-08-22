package banca.uy.core.services.interfaces;

import banca.uy.core.entity.Tombola;

import java.util.List;
import java.util.Set;

public interface ITombolaService {
	public Tombola saveTirada(String tirada);
	public Set<Integer> getJugada(String fecha);
	public List<String> getJugadaRepetidas(String fecha);
}
