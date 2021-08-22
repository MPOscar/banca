package banca.uy.core.utils.poiji;

import com.poiji.annotation.ExcelCellName;

public class ExcelRNCampoPresentacion {

    @ExcelCellName("Tipo")
    private String tipo = "";
    @ExcelCellName("Cant")
    private String cantidad = "";
    @ExcelCellName("Unid")
    private String unidad = "";

    public ExcelRNCampoPresentacion() {
    }

    public ExcelRNCampoPresentacion(String tipo, String cantidad, String unidad) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return this.unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public ExcelRNCampoPresentacion tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public ExcelRNCampoPresentacion cantidad(String cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public ExcelRNCampoPresentacion unidad(String unidad) {
        this.unidad = unidad;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " tipo='" + getTipo() + "'" + ", cantidad='" + getCantidad() + "'" + ", unidad='" + getUnidad()
                + "'" + "}";
    }

}