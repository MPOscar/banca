package banca.uy.core.resources.dto;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

/**
 * Esta clase representa el resultado de un <b>paginado</b> solicitado mediante
 * un {@link PaginacionRequest}. Sus atributos siguen el siguiente
 * comportamiento:
 * <p>
 * <b>pagina</b>: Define la página a la que pertencen los datos paginados. Por
 * defecto 1
 * </p>
 * <p>
 * <b>cantidad</b>: Define la cantidad de elementos mostrados por página. Por
 * defecto: 100. Si es -1 se asume que se muestran todos los elementos que se
 * hallaron en el origen de los datos
 * </p>
 * <p>
 * <b>total</b>: La cantidad total de elementos en el origen de los datos. Si
 * tiene valor -1 es que no se cuenta con esta información
 * </p>
 * <p>
 * <b>links</b>: Contiene los {@link Link} que se generan a partir de los datos
 * actuales del paginador.
 * <p>
 * - self: Referencia a la página actual
 * </p>
 * <p>
 * - first: Referencia a la primera página
 * </p>
 * <p>
 * - prev: Referencia a la página previa si existe
 * </p>
 * <p>
 * - next: Referencia a la página siguiente si existe
 * </p>
 * <p>
 * - last: Referencia a la página final si existe
 * </p>
 * </p>
 * 
 * @param <T>
 */
public class PaginadoResponse<T> {

    private Long pagina = new Long(1);
    private Long cantidad = new Long(100);
    private Long total = new Long(-1);
    private List<Link> links = new LinkedList<Link>();
    private List<Long> productosEmpresa = new ArrayList<>();
    private String filtros = "";
    private T elementos;

    public PaginadoResponse() {
    }

    public PaginadoResponse(Long pagina, Long cantidad, Long total) {
        this.pagina = pagina > 0 ? pagina : this.pagina;
        this.cantidad = cantidad > 0 ? cantidad : this.cantidad;
        this.total = total >= 0 ? total : this.total;
    }

    public PaginadoResponse(Long pagina, Long cantidad, Long total, T elementos) {
        this(pagina, cantidad, total);
        this.elementos = elementos;
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

    public PaginadoResponse<T> pagina(Long pagina) {
        this.pagina = pagina;
        return this;
    }

    public PaginadoResponse<T> cantidad(Long cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public PaginadoResponse<T> total(Long total) {
        this.total = total;
        return this;
    }

    public T getElementos() {
        return this.elementos;
    }

    public void setElementos(T elementos) {
        this.elementos = elementos;
    }

    public PaginadoResponse<T> elementos(T elementos) {
        this.elementos = elementos;
        return this;
    }

    public PaginadoResponse(List<Link> links) {
        this.links = links;
    }

    public void setProductosEmpresa(List<Long> productosEmpresa) {
        this.productosEmpresa = productosEmpresa;
    }

    public List<Long> getProductosEmpresa() {
        return this.productosEmpresa;
    }


    /**
     * Al pasar un dato distinto de null por parámetros se asume un nuevo registro
     * de url y por tanto se regenerarán primero y luego se devolverán el listado de
     * los {@link Link} generados
     * 
     * @param url
     * @return Listado de {@link Link} para obtener los datos paginados
     */
    public List<Link> inicializarLink(String url) {
        if (url != null)
            inicializarFiltros(url);
        //this.regenerarLinks(url);
        return this.links;
    }

    /**    
     * @param url
     * @return Obtener los campos por los que se puede realizar el filtrado
     */
    public String inicializarFiltros(String url) {
        if(url.contains("/productos")) {
            this.filtros = "{ cpp: 'p.cpp', descripcion: 'p.descripcion', marca: 'p.marca', division: 'p.categoria.nombre', linea: 'p.categoria.padre.nombre', usar: 'filters=Garoto&filters=sdsdf', sortAsc: sort='p.marca', sortDesc: 'sort=-p.marca' }";
        }            
        return this.filtros;
    }

    /**
     * Al pasar un dato distinto de null por parámetros se asume un nuevo registro
     * de url y por tanto se regenerarán primero y luego se devolverán el listado de
     * los {@link Link} generados
     * 
     * @param url
     * @return Listado de {@link Link} para obtener los datos paginados
     */
    public List<Link> getLinks(String url) {
        return this.inicializarLink(url);
    }

    public List<Link> getLinks() {
        return this.links;
    }

    /**    
     * @param url
     * @return Obtener los campos por los que se puede hacer el filtrado
     */
    public String getFiltros(String url) {
        return this.inicializarFiltros(url);
    }

    public String getFiltros() {
        return this.filtros;
    }

    private void regenerarLinks(String url) {
        self(url);
        first(url);
        last(url);
        prev(url);
        next(url);
    }

    private void self(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        builder.queryParam("page", this.getPagina());
        builder.queryParam("limit", this.getCantidad());
        String uriString = builder.build().toUriString();
        /*URI uri = builder.build().toUri();
        UriBuilder b = UriBuilder.fromUri(url);
        b.queryParam("page", this.getPagina());
        b.queryParam("limit", this.getCantidad());*/
        Link self = Link.fromUri(builder.build().toUri()).rel("self").title("Obtener página actual").type("GET").build();
        this.links.add(self);
    }

    private void first(String url) {
        UriBuilder b = UriBuilder.fromUri(url);
        b.queryParam("page", 1);
        b.queryParam("limit", this.getCantidad());
        Link first = Link.fromUri(b.build()).rel("first").title("Obtener primera página").type("GET").build();
        this.links.add(first);
    }

    private void last(String url) {
        UriBuilder b = UriBuilder.fromUri(url);
        Double ultimaPagina = Math.ceil(this.total / this.cantidad);
        ultimaPagina = ultimaPagina > 0 ? ultimaPagina : 1;
        b.queryParam("page", ultimaPagina.intValue());
        b.queryParam("limit", this.getCantidad());
        Link first = Link.fromUri(b.build()).rel("last").title("Obtener última página").type("GET").build();
        this.links.add(first);
    }

    private void prev(String url) {
        if (this.existePrevio() == null)
            return;

        UriBuilder b = UriBuilder.fromUri(url);
        b.queryParam("page", this.existePrevio());
        b.queryParam("limit", this.getCantidad());
        Link first = Link.fromUri(b.build()).rel("prev").title("Obtener página previa").type("GET").build();
        this.links.add(first);
    }

    private void next(String url) {
        if (this.existeSiguiente() == null)
            return;

        UriBuilder b = UriBuilder.fromUri(url);
        b.queryParam("page", this.existeSiguiente());
        b.queryParam("limit", this.getCantidad());
        Link first = Link.fromUri(b.build()).rel("next").title("Obtener página siguiente").type("GET").build();
        this.links.add(first);
    }

    public Long existeSiguiente() {
        if (total <= 0 || this.cantidad == 0)
            return null;
        Double ultimaPagina = Math.ceil(this.total / this.cantidad);
        if (this.pagina < ultimaPagina)
            return this.getPagina() + 1;
        return null;
    }

    public Long existePrevio() {
        if (total <= 0 || this.cantidad == 0)
            return null;
        Double ultimaPagina = Math.ceil(this.total / this.cantidad);
        if (ultimaPagina > 1 && this.pagina > 1)
            return this.getPagina() - 1;
        return null;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public PaginadoResponse<T> links(List<Link> links) {
        this.links = links;
        return this;
    }

}
