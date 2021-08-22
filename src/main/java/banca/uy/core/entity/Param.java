package banca.uy.core.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Param")
@CompoundIndexes({
        @CompoundIndex(name = "param_nombre", def = "{ 'nombre': 1 }", unique = true)
})
public class Param extends Entidad {

    private String nombre;

    private String valor;

    public Param() {
        super();
    }

    public Param(Param param) {
        super();
        this.nombre = param.getNombre();
        this.valor = param.getValor();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
