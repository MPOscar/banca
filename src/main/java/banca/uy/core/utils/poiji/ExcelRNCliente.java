package banca.uy.core.utils.poiji;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.poiji.annotation.ExcelCellName;

public class ExcelRNCliente {
    @ExcelCellName("Código EAN del cliente")
    private BigDecimal eanCode = new BigDecimal("-1");
    @ExcelCellName("*")
    private String eliminar = "";
    @ExcelCellName("Nombre del cliente")
    private String nombre = "";
    @ExcelCellName("Lista de grupos separados por coma")
    private String visiblePor = "";

    public ExcelRNCliente() {
    }

    public ExcelRNCliente(BigDecimal eanCode, String nombre, String visiblePor) {
        this.eanCode = eanCode;
        this.nombre = nombre;
        this.visiblePor = visiblePor;
    }

    public BigDecimal getEanCode() {
        return this.eanCode;
    }

    public void setEan(BigDecimal eanCode) {
        this.eanCode = eanCode;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVisiblePor() {
        return this.visiblePor;
    }

    public void setVisiblePor(String visiblePor) {
        this.visiblePor = visiblePor;
    }

    public ExcelRNCliente eanCode(BigDecimal eanCode) {
        this.eanCode = eanCode;
        return this;
    }

    public ExcelRNCliente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public List<String> listadoGrupos() {

        return Arrays.asList(this.visiblePor.split(",")).stream()

                .map(e -> e.trim())

                .collect(Collectors.toList());
    }

    public String getEliminar() {
        return this.eliminar;
    }

    public void setEliminar(String eliminar) {
        this.eliminar = eliminar;
    }

    public Boolean eliminar() {
        return this.eliminar.equals("*");
    }

    @Override
    public String toString() {
        return "{" + " eanCode='" + getEanCode() + "'" + ", nombre='" + getNombre() + "'" + ", visiblePor='"
                + getVisiblePor() + "'" + "}";
    }

}