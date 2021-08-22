package banca.uy.core.db;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;

import banca.uy.core.resources.dto.PaginadoRequest;
import org.hibernate.Session;

import banca.uy.core.exceptions.ModelException;
import banca.uy.core.entity.Entidad;
import banca.uy.core.resources.dto.PaginadoResponse;

public class Utils {

	/**
	 * 
	 * Método que a partir de los datos contenidos en el parámetro <b>"order"</b>
	 * agrega a la consulta pasada por parámetros los datos correspondientes para
	 * ordenar la consulta. Si no se envia un parámetro <b>"order"</b> se devuelve
	 * la consulta sin modificar
	 * 
	 * @param consultaOriginal
	 * @param queryParamsMap
	 * @return
	 */
	public static String constructorDinamicoOrdenamientos(String consultaOriginal,
			MultivaluedMap<String, String> queryParamsMap) {

		if (!queryParamsMap.containsKey("sort"))
			return consultaOriginal;
		String order = queryParamsMap.getFirst("sort");
		if (order.toLowerCase().contains("select") || order.toLowerCase().contains("where"))
			return consultaOriginal;

		final String sortDirection = queryParamsMap.containsKey("sortType")
				? " " + (queryParamsMap.getFirst("sortType").trim().toUpperCase().equals("ASC") ? "ASC" : "DESC")
				: "";
		return consultaOriginal.trim() + " order by  " + queryParamsMap.getFirst("sort").trim() + sortDirection;
	}

	/**
	 * 
	 * Este método devuelve la consulta pasada por parámetros con los filtros
	 * añadidos según corresponda
	 * 
	 * @param consultaOriginal
	 * @param queryParamsMap
	 * @return
	 */
	public static String constructorDinamicoFiltros(String consultaOriginal,
			MultivaluedMap<String, String> queryParamsMap) {
		// Tengo en cuenta solo los parametros con puntos, y selecciono de cada clave su
		// primer valor
		List<FilterValue> listadoFiltros = queryParamsMap.keySet().stream().filter(e -> e.indexOf(".") != -1).map(e -> {
			return FilterValue.loadFilter(e, queryParamsMap.getFirst(e));
		}).filter(e -> e.isPresent()).map(e -> e.get()).collect(Collectors.toList());
		;
		return Utils.constructorDinamicoFiltros(consultaOriginal, listadoFiltros);
	}

	/**
	 * 
	 * Este método devuelve la consulta pasada por parámetros con los Filtros
	 * también pasados por parámetros añadidos. De no pasarse filtros se devuelve la
	 * consulta sin modificaciones
	 * 
	 * @param consultaOriginal
	 * @param listadoFiltros
	 * @return
	 */
	public static String constructorDinamicoFiltros(String consultaOriginal, List<FilterValue> listadoFiltros) {
		// TODO Iteracion 2: Implementar uso de parametros para evitar posibles ataques
		// de tipo SQL Injection
		if (listadoFiltros.size() == 0)
			return consultaOriginal;
		// Agrego WHERE si no existe
		final Boolean existenCondicionesPrevias = consultaOriginal.toUpperCase().indexOf("WHERE", 0) != -1;
		final String consultaOriginalWhere = existenCondicionesPrevias ? consultaOriginal
				: consultaOriginal.trim() + " WHERE ";
		// Util para abstraer la forma general de añadir la consulta
		final String conditionsFinal = " ( :query )";

		// Convierto a SQL cada FilterValue, selecciono solo los filtros correctos,
		// añado todos los filtros y finalmente los substituyo por :query en
		// conditionsFinal para retornar ese valor
		return listadoFiltros.stream().map((e) -> {
			return e.toSQL();
		}).filter((e) -> e.isPresent() && !e.get().trim().isEmpty()).reduce(Optional.of(""), (acc, e) -> {
			return Optional.of(acc.get() + (acc.get().length() != 0 ? " AND " : "") + e.get());
		}).map((e) -> consultaOriginalWhere + (existenCondicionesPrevias ? " AND " : "")
				+ conditionsFinal.replace(":query", e)).orElse(consultaOriginal.trim());

	}

	/**
	 * Atajo a llamar {@link #predicadoEliminado(CriteriaBuilder, Root, Boolean)}
	 * con valor <b>false</b> en el tercer parámetro
	 * 
	 * @param <T>
	 * @param builder
	 * @param root
	 * @return
	 */
	public static <T extends Entidad> Predicate predicadoEliminado(CriteriaBuilder builder, Root<T> root) {
		return builder.equal(root.get("eliminado"), false);
	}

	/**
	 * Devuelve un {@link Predicate} asegurando que el elemento no esté eliminado
	 * siguiendo los criterios usados en la definición del Modelo {@link Entidad}
	 * 
	 * @param <T>
	 * @param builder
	 * @param root
	 * @param eliminado
	 * @return
	 */
	public static <T extends Entidad> Predicate predicadoEliminado(CriteriaBuilder builder, Root<T> root,
			Boolean eliminado) {
		return builder.equal(root.get("eliminado"), eliminado);
	}

	/**
	 * Método encargado de realizar el paginado a las consultas que le son pasadas
	 * por parámetros.
	 * <p>
	 * Se le debe pasar la consulta a ejecutar y un total de elementos disponibles
	 * </p>
	 * 
	 * @param <T>
	 * @param paginadoRequest
	 * @param selectQuery
	 * @param total
	 * @param session
	 * @return
	 * @throws ModelException
	 */
	public static <T> PaginadoResponse<List<T>> paginar(final PaginadoRequest paginadoRequest,
			final TypedQuery<T> selectQuery, final Long total, Session session) throws ModelException {
		PaginadoResponse<List<T>> response = new PaginadoResponse<List<T>>(paginadoRequest.getPagina(),
				paginadoRequest.getCantidad(), total);

		Long cantidadElementosPorPagina = paginadoRequest.getCantidad();
		Integer cantidadPaginasPosibles = (int) (Math.ceil((double)total / (double)cantidadElementosPorPagina));
		if (cantidadPaginasPosibles == 0) {
			cantidadPaginasPosibles = 1;
		}
		if (response.getPagina() > cantidadPaginasPosibles)
			response.setPagina(Long.parseLong(cantidadPaginasPosibles.toString()));

		response.setCantidad(cantidadElementosPorPagina);
		response.setElementos(Utils.consultaPaginada(selectQuery,
				response.getCantidad().intValue() * (response.getPagina().intValue() - 1),
				response.getCantidad().intValue(), session));

		return response;
	}

	/**
	 * Método encargado de realizar el paginado a las consultas que le son pasadas
	 * por parámetros.
	 * <p>
	 * Se le debe pasar la consulta a ejecutar y un total de elementos disponibles
	 * </p>
	 *
	 * @param <T>
	 * @param paginadoRequest
	 * @param selectQuery
	 * @param total
	 * @return
	 * @throws ModelException
	 */
	public static <T> PaginadoResponse<List<T>> paginar(final PaginadoRequest paginadoRequest,
														final TypedQuery<T> selectQuery, final Long total) throws ModelException {
		PaginadoResponse<List<T>> response = new PaginadoResponse<List<T>>(paginadoRequest.getPagina(),
				paginadoRequest.getCantidad(), total);

		Long cantidadElementosPorPagina = paginadoRequest.getCantidad();
		Integer cantidadPaginasPosibles = (int) (Math.ceil((double)total / (double)cantidadElementosPorPagina));
		if (cantidadPaginasPosibles == 0) {
			cantidadPaginasPosibles = 1;
		}
		if (response.getPagina() > cantidadPaginasPosibles)
			response.setPagina(Long.parseLong(cantidadPaginasPosibles.toString()));

		response.setCantidad(cantidadElementosPorPagina);
		response.setElementos(Utils.consultaPaginada(selectQuery,
				response.getCantidad().intValue() * (response.getPagina().intValue() - 1),
				response.getCantidad().intValue()));

		return response;
	}

	/**
	 * Método encargado de realizar el paginado a las consultas que le son pasadas
	 * por parámetros.
	 * <p>
	 * Se le debe pasar la consulta a ejecutar y una consulta que devuelva como
	 * resultado el total de elementos disponibles
	 * </p>
	 * 
	 * @param <T>
	 * @param paginadoRequest
	 * @param selectQuery
	 * @param countQuery
	 * @param session
	 * @return
	 * @throws ModelException
	 */
	public static <T> PaginadoResponse<List<T>> paginar(final PaginadoRequest paginadoRequest,
			final TypedQuery<T> selectQuery, final TypedQuery<BigInteger> countQuery, Session session)
			throws ModelException {

		BigInteger total = countQuery.getSingleResult();
		return Utils.paginar(paginadoRequest, selectQuery, total.longValue(), session);

	}

	/**
	 * Método encargado de realizar el paginado a las consultas que le son pasadas
	 * por parámetros.
	 * <p>
	 * Se le debe pasar la consulta a ejecutar y una consulta que devuelva como
	 * resultado el total de elementos disponibles
	 * </p>
	 *
	 * @param <T>
	 * @param paginadoRequest
	 * @param selectQuery
	 * @param countQuery
	 * @param session
	 * @return
	 * @throws ModelException
	 */
	public static <T> PaginadoResponse<List<T>> paginar(final PaginadoRequest paginadoRequest,
														final TypedQuery<T> selectQuery, final TypedQuery<BigInteger> countQuery)
			throws ModelException {

		BigInteger total = countQuery.getSingleResult();
		return Utils.paginar(paginadoRequest, selectQuery, total.longValue());

	}

	public static <T> PaginadoResponse<List<T>> paginarNamedQuery(final PaginadoRequest paginadoRequest,
			final TypedQuery<T> selectQuery, final TypedQuery<Long> countQuery)
			throws ModelException {

		Long total = countQuery.getSingleResult();
		return Utils.paginar(paginadoRequest, selectQuery, total.longValue());

	}

	/**
	 * Método encargado de realizar el paginado a las consultas que le son pasadas
	 * por parámetros
	 * 
	 * @param <T>
	 * @param paginado
	 * @param criteriaQuery
	 * @param em
	 * @return
	 * @throws ModelException
	 */
	public static <T> PaginadoResponse<List<T>> paginar(final PaginadoRequest paginadoRequest,
			final CriteriaQuery<T> criteriaQuery, final CriteriaBuilder cb, final Root<T> root, Session session)
			throws ModelException {

		Long total = Utils.total(cb, criteriaQuery, root, session);
		PaginadoResponse<List<T>> response = new PaginadoResponse<List<T>>(paginadoRequest.getPagina(),
				paginadoRequest.getCantidad(), total);

		Long cantidadElementosPorPagina = paginadoRequest.getCantidad();
		Integer cantidadPaginasPosibles = (int) (Math.ceil(total / cantidadElementosPorPagina));
		if (cantidadPaginasPosibles == 0) {
			cantidadPaginasPosibles = 1;
		}
		if (response.getPagina() > cantidadPaginasPosibles)
			response.setPagina(Long.parseLong(cantidadPaginasPosibles.toString()));

		response.setCantidad(cantidadElementosPorPagina);
		response.setElementos(Utils.consultaPaginada(criteriaQuery,
				response.getCantidad().intValue() * (response.getPagina().intValue() - 1),
				response.getCantidad().intValue(), session));

		return response;
	}

	/**
	 * Este método devuelve los resultados obtenidos de la consulta ejecutada
	 * teniendo en cuenta el siguiente criterio:
	 * <p>
	 * <b>offset</b>: Representa el corrimiento en los resultados para obtener el
	 * primer resultado
	 * </p>
	 * <p>
	 * <b>limit</b>: Representa la cantidad máxima de resultados a obtener,
	 * independientemente del la cantidad total que realmente exista
	 * </p>
	 * 
	 * @param <T>
	 * @param selectQuery
	 * @param offset
	 * @param limit
	 * @param session
	 * @return Listado de elementos de tipo <b>T</b>
	 * @throws ModelException
	 */
	public static <T> List<T> consultaPaginada(TypedQuery<T> selectQuery, Integer offset, Integer limit,
			Session session) throws ModelException {
		try {
			return selectQuery.setFirstResult(offset).setMaxResults(limit).getResultList();
		} catch (Exception ex) {
			String hqlQueryString = selectQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
			throw new ModelException("Error al realizar consulta para Paginado, consulta: " + hqlQueryString);
		}
	}

	/**
	 * Este método devuelve los resultados obtenidos de la consulta ejecutada
	 * teniendo en cuenta el siguiente criterio:
	 * <p>
	 * <b>offset</b>: Representa el corrimiento en los resultados para obtener el
	 * primer resultado
	 * </p>
	 * <p>
	 * <b>limit</b>: Representa la cantidad máxima de resultados a obtener,
	 * independientemente del la cantidad total que realmente exista
	 * </p>
	 *
	 * @param <T>
	 * @param selectQuery
	 * @param offset
	 * @param limit
	 * @return Listado de elementos de tipo <b>T</b>
	 * @throws ModelException
	 */
	public static <T> List<T> consultaPaginada(TypedQuery<T> selectQuery, Integer offset, Integer limit) throws ModelException {
		try {
			return selectQuery.setFirstResult(offset).setMaxResults(limit).getResultList();
		} catch (Exception ex) {
			String hqlQueryString = selectQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
			throw new ModelException("Error al realizar consulta para Paginado, consulta: " + hqlQueryString);
		}
	}

	/**
	 * Este método devuelve los resultados obtenidos de la consulta ejecutada a
	 * partir de la consulta generada del parámetro <b>criteriaQuery</b> teniendo en
	 * cuenta el siguiente criterio:
	 * <p>
	 * <b>offset</b>: Representa el corrimiento en los resultados para obtener el
	 * primer resultado
	 * </p>
	 * <p>
	 * <b>limit</b>: Representa la cantidad máxima de resultados a obtener,
	 * independientemente del la cantidad total que realmente exista
	 * </p>
	 * 
	 * @param <T>
	 * @param criteriaQuery
	 * @param offset
	 * @param limit
	 * @param session
	 * @return Listado de elementos de tipo <b>T</b>
	 * @throws ModelException
	 */
	public static <T> List<T> consultaPaginada(CriteriaQuery<T> criteriaQuery, Integer offset, Integer limit,
			Session session) throws ModelException {
		try {
			return session.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(limit).getResultList();
		} catch (Exception ex) {
			Query query = session.createQuery(criteriaQuery);
			String hqlQueryString = query.unwrap(org.hibernate.query.Query.class).getQueryString();
			throw new ModelException("Error al realizar consulta para Paginado, consulta: " + hqlQueryString);
		}
	}

	/**
	 * 
	 * Este método devuelve el número total de los registros disponibles a partir de
	 * la consulta generada del parámetro <b>criteriaQuery</b>
	 * 
	 * @param <T>
	 * @param cb
	 * @param selectQuery
	 * @param root
	 * @param session
	 * @return Número total de registros disponibles en la BD
	 */
	public static <T> Long total(final CriteriaBuilder cb, final CriteriaQuery<T> selectQuery, Root<T> root,
			final Session session) {
		CriteriaQuery<Long> query = Utils.createCountQuery(cb, selectQuery, root);
		return session.createQuery(query).getSingleResult();
	}

	private static <T> CriteriaQuery<Long> createCountQuery(final CriteriaBuilder cb, final CriteriaQuery<T> criteria,
			final Root<T> root) {

		final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		final Root<T> countRoot = countQuery.from(criteria.getResultType());

		Utils.doJoins(root.getJoins(), countRoot);
		Utils.doJoinsOnFetches(root.getFetches(), countRoot);

		countQuery.select(cb.count(countRoot));
		countQuery.where(criteria.getRestriction());

		if (root.getAlias() == null)
			root.alias("alias");
		countRoot.alias(root.getAlias());

		return countQuery.distinct(criteria.isDistinct());
	}

	@SuppressWarnings("unchecked")
	private static void doJoinsOnFetches(Set<? extends Fetch<?, ?>> joins, Root<?> root) {
		Utils.doJoins((Set<? extends Join<?, ?>>) joins, root);
	}

	private static void doJoins(Set<? extends Join<?, ?>> joins, Root<?> root) {
		for (Join<?, ?> join : joins) {
			Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
			joined.alias(join.getAlias());
			doJoins(join.getJoins(), joined);
		}
	}

	private static void doJoins(Set<? extends Join<?, ?>> joins, Join<?, ?> root) {
		for (Join<?, ?> join : joins) {
			Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
			joined.alias(join.getAlias());
			Utils.doJoins(join.getJoins(), joined);
		}
	}
}
