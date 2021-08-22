package banca.uy.core.resources.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Representacion<T> {
	private long code;

	private List<Link> links = new LinkedList<Link>();
	private List<Long> productosEmpresa = new ArrayList<>();

	private T data;

	private Long page = null;
	private Long limit = null;
	private Long total = null;

	public Representacion() {
	}

	public Representacion(long code, T data) {
		this.code = code;
		this.data = data;
	}

	public Representacion(long code, PaginadoResponse<T> pr) {
		this.code = code;
		this.links = pr.getLinks() != null ? pr.getLinks() : this.links;
		this.data = pr.getElementos();
		this.page = pr.getPagina();
		this.total = pr.getTotal();
		this.limit = pr.getCantidad();
		this.productosEmpresa = pr.getProductosEmpresa();
	}

	@JsonProperty
	public long getCode() {
		return code;
	}

	@JsonProperty
	public T getData() {
		return data;
	}

	@JsonProperty("_links")
	public List<Link> getLinks() {
		return this.links;
	}

	@JsonProperty("page")
	@JsonInclude(value = Include.NON_EMPTY)
	public Long getPage() {
		return this.page;
	}

	@JsonProperty("limit")
	@JsonInclude(value = Include.NON_EMPTY)
	public Long getCount() {
		return this.limit;
	}

	@JsonProperty("total")
	@JsonInclude(value = Include.NON_EMPTY)
	public Long getTotal() {
		return this.total;
	}

	@JsonProperty("productosEmpresa")
	@JsonInclude(value = Include.NON_EMPTY)
	public List<Long> getProductosEmpresa() {
		return this.productosEmpresa;
	}

}
