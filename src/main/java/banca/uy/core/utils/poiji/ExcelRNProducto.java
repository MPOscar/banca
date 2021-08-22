package banca.uy.core.utils.poiji;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellRange;

public class ExcelRNProducto {
    @ExcelCell(0)
    private String cpp = "";
    @ExcelCell(1)
    private String eliminar = "";
    @ExcelCell(2)
    private String descripcion = "";
    @ExcelCell(3)
    private String marca = "";
    @ExcelCell(4)
    private String division = "";
    @ExcelCell(5)
    private String linea = "";
    @ExcelCellRange
    private ExcelRNCampoPresentacion presentacion;

    @ExcelCell(9)
    private String gtin13 = "";

    @ExcelCellRange
    private ExcelRNCampoUnidadDespacho unidadesDeDespacho;

    @ExcelCell(14)
    private String pais = "";
    @ExcelCell(15)
    private String pideUnidad = "";
    @ExcelCell(16)
    private String esPromo = "";

    @ExcelCellRange
    private ExcelRNCampoSuspendido suspendido;

    @ExcelCell(19)
    private String cppPromo = "";

    @ExcelCellRange
    private ExcelRNCampoVisiblePor visiblePor;

    public ExcelRNProducto() {
    }

    public ExcelRNProducto(String cpp, String eliminar, String descripcion, String marca, String division, String linea,
            ExcelRNCampoPresentacion presentacion, String gtin13, ExcelRNCampoUnidadDespacho unidadesDeDespacho,
            String pais, String pideUnidad, String esPromo, ExcelRNCampoSuspendido suspendido, String cppPromo,
            ExcelRNCampoVisiblePor visiblePor) {
        this.cpp = cpp;
        this.eliminar = eliminar;
        this.descripcion = descripcion;
        this.marca = marca;
        this.division = division;
        this.linea = linea;
        this.presentacion = presentacion;
        this.gtin13 = gtin13;
        this.unidadesDeDespacho = unidadesDeDespacho;
        this.pais = pais;
        this.pideUnidad = pideUnidad;
        this.esPromo = esPromo;
        this.suspendido = suspendido;
        this.cppPromo = cppPromo;
        this.visiblePor = visiblePor;
    }

    public String getCpp() {
        return this.cpp;
    }

    public void setCpp(String cpp) {
        this.cpp = cpp;
    }

    public String getEliminar() {
        return this.eliminar;
    }

    public void setEliminar(String eliminar) {
        this.eliminar = eliminar;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDivision() {
        return this.division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getLinea() {
        return this.linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public ExcelRNCampoPresentacion getPresentacion() {
        return this.presentacion;
    }

    public void setPresentacion(ExcelRNCampoPresentacion presentacion) {
        this.presentacion = presentacion;
    }

    public String getGtin13() {
        return this.gtin13;
    }

    public void setGtin13(String gtin13) {
        this.gtin13 = gtin13;
    }

    public ExcelRNCampoUnidadDespacho getUnidadesDeDespacho() {
        return this.unidadesDeDespacho;
    }

    public void setUnidadesDeDespacho(ExcelRNCampoUnidadDespacho unidadesDeDespacho) {
        this.unidadesDeDespacho = unidadesDeDespacho;
    }

    public String getPais() {
        return this.pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPideUnidad() {
        return this.pideUnidad;
    }

    public void setPideUnidad(String pideUnidad) {
        this.pideUnidad = pideUnidad;
    }

    public String getEsPromo() {
        return this.esPromo;
    }

    public void setEsPromo(String esPromo) {
        this.esPromo = esPromo;
    }

    public ExcelRNCampoSuspendido getSuspendido() {
        return this.suspendido;
    }

    public void setSuspendido(ExcelRNCampoSuspendido suspendido) {
        this.suspendido = suspendido;
    }

    public String getCppPromo() {
        return this.cppPromo;
    }

    public void setCppPromo(String cppPromo) {
        this.cppPromo = cppPromo;
    }

    public ExcelRNCampoVisiblePor getVisiblePor() {
        return this.visiblePor;
    }

    public void setVisiblePor(ExcelRNCampoVisiblePor visiblePor) {
        this.visiblePor = visiblePor;
    }

    public ExcelRNProducto cpp(String cpp) {
        this.cpp = cpp;
        return this;
    }

    public ExcelRNProducto eliminar(String eliminar) {
        this.eliminar = eliminar;
        return this;
    }

    public ExcelRNProducto descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public ExcelRNProducto marca(String marca) {
        this.marca = marca;
        return this;
    }

    public ExcelRNProducto division(String division) {
        this.division = division;
        return this;
    }

    public ExcelRNProducto linea(String linea) {
        this.linea = linea;
        return this;
    }

    public ExcelRNProducto presentacion(ExcelRNCampoPresentacion presentacion) {
        this.presentacion = presentacion;
        return this;
    }

    public ExcelRNProducto gtin13(String gtin13) {
        this.gtin13 = gtin13;
        return this;
    }

    public ExcelRNProducto unidadesDeDespacho(ExcelRNCampoUnidadDespacho unidadesDeDespacho) {
        this.unidadesDeDespacho = unidadesDeDespacho;
        return this;
    }

    public ExcelRNProducto pais(String pais) {
        this.pais = pais;
        return this;
    }

    public ExcelRNProducto pideUnidad(String pideUnidad) {
        this.pideUnidad = pideUnidad;
        return this;
    }

    public ExcelRNProducto esPromo(String esPromo) {
        this.esPromo = esPromo;
        return this;
    }

    public ExcelRNProducto suspendido(ExcelRNCampoSuspendido suspendido) {
        this.suspendido = suspendido;
        return this;
    }

    public ExcelRNProducto cppPromo(String cppPromo) {
        this.cppPromo = cppPromo;
        return this;
    }

    public ExcelRNProducto visiblePor(ExcelRNCampoVisiblePor visiblePor) {
        this.visiblePor = visiblePor;
        return this;
    }

    public List<String> listadoGrupos() {
        String[] listaGrupos = this.visiblePor.getVisiblePorGrupos().split(",");
        return Arrays.asList(listaGrupos).stream()

                .map(e -> e.trim())

                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "{" + " cpp='" + getCpp() + "'" + ", eliminar='" + getEliminar() + "'" + ", descripción='"
                + getDescripcion() + "'" + ", marca='" + getMarca() + "'" + ", division='" + getDivision() + "'"
                + ", linea='" + getLinea() + "'" + ", presentacion='" + getPresentacion() + "'" + ", gtin13='"
                + getGtin13() + "'" + ", unidadesDeDespacho='" + getUnidadesDeDespacho() + "'" + ", pais='" + getPais()
                + "'" + ", pideUnidad='" + getPideUnidad() + "'" + ", esPromo='" + getEsPromo() + "'" + ", suspendido='"
                + getSuspendido() + "'" + ", cppPromo='" + getCppPromo() + "'" + ", visiblePor='" + getVisiblePor()
                + "'" + "}";
    }

}