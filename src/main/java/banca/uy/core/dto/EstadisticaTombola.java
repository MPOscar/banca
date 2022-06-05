package banca.uy.core.dto;

import banca.uy.core.entity.Tombola;
import banca.uy.core.utils.serializer.CustomDateTimeDeserializer;
import banca.uy.core.utils.serializer.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EstadisticaTombola {

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    protected DateTime fechaTirada;

    private int numeroCoincidencias;

    private List<Integer> sorteo = new ArrayList<>();

    private int totalDeNumerosPosibles;

    private List<Integer> numerosPosibles = new ArrayList<>();

    private HashMap<Integer, Integer> jugadasRepetidas;

    public EstadisticaTombola(Tombola tombola, HashMap<Integer, Integer> jugadasRepetidas) {
        this.fechaTirada = tombola.getFechaTirada();
        this.sorteo = tombola.getSorteo();
        this.jugadasRepetidas = jugadasRepetidas;
    }

    public EstadisticaTombola(Tombola tombola, int numeroCoincidencias, List<Integer> numerosPosibles) {
        this.fechaTirada = tombola.getFechaTirada();
        this.numeroCoincidencias = numeroCoincidencias;
        this.sorteo = tombola.getSorteo();
        this.numerosPosibles = numerosPosibles;
    }

    public DateTime getFechaTirada() {
        return fechaTirada;
    }

    public void setFechaTirada(DateTime fechaTirada) {
        this.fechaTirada = fechaTirada;
    }

    public int getNumeroCoincidencias() {
        return numeroCoincidencias;
    }

    public void setNumeroCoincidencias(int numeroCoincidencias) {
        this.numeroCoincidencias = numeroCoincidencias;
    }

    public List<Integer> getSorteo() {
        return sorteo;
    }

    public void setSorteo(List<Integer> sorteo) {
        this.sorteo = sorteo;
    }

    public List<Integer> getNumerosPosibles() {
        return numerosPosibles;
    }

    public void setNumerosPosibles(List<Integer> numerosPosibles) {
        this.numerosPosibles = numerosPosibles;
    }

    public HashMap<Integer, Integer> getJugadasRepetidas() {
        return jugadasRepetidas;
    }

    public void setJugadasRepetidas(HashMap<Integer, Integer> jugadasRepetidas) {
        this.jugadasRepetidas = jugadasRepetidas;
    }

    public int getTotalDeNumerosPosibles() {
        return totalDeNumerosPosibles;
    }

    public void setTotalDeNumerosPosibles(int totalDeNumerosPosibles) {
        this.totalDeNumerosPosibles = totalDeNumerosPosibles;
    }
}
