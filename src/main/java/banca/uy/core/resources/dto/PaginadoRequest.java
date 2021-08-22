package banca.uy.core.resources.dto;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;


public class PaginadoRequest {

    @QueryParam("page")
    @DefaultValue("1")
    private Long pagina = new Long(1);
    @QueryParam("limit")
    @DefaultValue("10000")
    private Long cantidad = new Long(100);
    @QueryParam("filters")
    @DefaultValue("")
    private List<String> filters = new ArrayList<String>();
    @QueryParam("marcas")
    @DefaultValue("")
    private List<String> marcas = new ArrayList<String>();
    @QueryParam("lineas")
    @DefaultValue("")
    private List<String> lineas = new ArrayList<String>();
    @QueryParam("divisiones")
    @DefaultValue("")
    private List<String> divisiones = new ArrayList<String>();
    @QueryParam("sort")
    @DefaultValue("")
    private String sort = new String();
    private Long total = new Long(-1);
    private UriInfo uriInfo = null;
    @QueryParam("division")
    @DefaultValue("")
    private String division = new String();
    @QueryParam("orderBy")
    @DefaultValue("marca")
    private String orderBy;
    private Boolean discontinuados;
    private Boolean suspendidos;
    private Boolean ocultarDiscontinuados;
    private Boolean ocultarSuspendidos;

    public PaginadoRequest() {
    }

    public PaginadoRequest(Long pagina, Long cantidad) {
        this.pagina = pagina > 0 ? pagina : 1;
        this.cantidad = cantidad > 0 ? cantidad : this.cantidad;
    }

    public PaginadoRequest(Long pagina, Long cantidad, Long total) {
        this.pagina = pagina > 0 ? pagina : 1;
        this.cantidad = cantidad > 0 ? cantidad : this.cantidad;
        this.total = total >= 0 ? total : -1;
    }

    public Long getPagina() {
        return this.pagina;
    }

    public void setPagina(Long pagina) {
        this.pagina = pagina;
    }

    public Long getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<String> getFilters() {
        return this.filters;
    }
       
    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<String> getMarcas() {
        return this.marcas;
    }
       
    public void setMarcas(List<String> marcas) {
        this.marcas = marcas;
    }

    public List<String> getLineas() {
        return this.lineas;
    }
       
    public void setLineas(List<String> lineas) {
        this.lineas = lineas;
    }

    public List<String> getDivisiones() {
        return this.divisiones;
    }
       
    public void setDivisiones(List<String> divisiones) {
        this.divisiones = divisiones;
    }

    public PaginadoRequest pagina(Long pagina) {
        this.pagina = pagina;
        return this;
    }

    public PaginadoRequest cantidad(Long cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public PaginadoRequest total(Long total) {
        this.total = total;
        return this;
    }

    public String getDivision() {
        return this.division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Optional<UriInfo> getUriInfo() {
        return Optional.ofNullable(this.uriInfo);
    }

    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public Boolean getDiscontinuados() {
        return this.discontinuados;
    }

    public void setDiscontinuados(Boolean discontinuados) {
        this.discontinuados = discontinuados;
    }

    public Boolean getSuspendidos() {
        return this.suspendidos;
    }

    public void setSuspendidos(Boolean suspendidos) {
        this.suspendidos = suspendidos;
    }

    public Boolean getOcultarDiscontinuados() {
        return this.ocultarDiscontinuados;
    }

    public void setOcultarDiscontinuados(Boolean ocultarDiscontinuados) {
        this.ocultarDiscontinuados = ocultarDiscontinuados;
    }

    public Boolean getOcultarSuspendidos() {
        return this.ocultarSuspendidos;
    }

    public void setOcultarSuspendidos(Boolean ocultarSuspendidos) {
        this.ocultarSuspendidos = ocultarSuspendidos;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
