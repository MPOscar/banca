package banca.uy.core.db;

import banca.uy.core.entity.Usuario;
import banca.uy.core.exceptions.ServiceException;
import banca.uy.core.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosDAO.class);

	@Autowired
	private IUserRepository userRepository;

	public Usuario save(Usuario u) {
		u = userRepository.save(u);
		u.setSId(u.getId());
		userRepository.save(u);
		return u;
	}

	public UsuariosDAO() {
	}

	public Usuario findById(String id) {
		return this.userRepository.findById(id).get();
	}

	public List<Usuario> findByAll() {
		return this.userRepository.findAll();
	}

	public Optional<Usuario> findByUsuario(String searchUsuario) {
		List<Usuario> listaUsuarios = userRepository.findAllByUsuario(searchUsuario);
		if (listaUsuarios.size() != 0)
			return Optional.of(listaUsuarios.get(0));
		return Optional.ofNullable(null);
	}

	public Optional<Usuario> findByEmail(String searchUsuario) {
		List<Usuario> listaUsuarios = userRepository.findAllByEmail(searchUsuario);
		if (listaUsuarios.size() != 0)
			return Optional.of(listaUsuarios.get(0));
		return Optional.ofNullable(null);
	}

	/*public Optional<List<Usuario>> findByParameter(MultivaluedMap<String, String> params) {
		CriteriaBuilder builder = currentSession().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> i = criteria.from(Usuario.class);

		Iterator<String> it = params.keySet().iterator();

		try {
			LOGGER.warn("msg=TRYING");
			while (it.hasNext()) {
				String key = (String) it.next();

				List<Integer> lst = new ArrayList<Integer>();
				lst.add(201);
				lst.add(2);

				criteria.select(i)
						.where(builder.like(i.get(key), MatchMode.ANYWHERE.toMatchString(params.getFirst(key))));

				In<Integer> inEmpresas = builder.in(i.get("id"));

				if (lst.isEmpty()) {
					inEmpresas.value((Integer) null);
				} else {
					for (Integer id : lst) {
						inEmpresas.value(id);
					}
				}

				criteria.where(inEmpresas);
			}
		} catch (Exception e) {
			LOGGER.warn("msg=Malos parámetros: {}", e.getMessage(), e);
			throw new IllegalArgumentException("Malos parámetros.");
		}

		TypedQuery<Usuario> query = currentSession().createQuery(criteria);


		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(Usuario.class).get();

		return Optional.of(query.getResultList());
	}*/

	public List<Usuario> getAll() {
		/*CriteriaBuilder builder = currentSession().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		criteria.select(root);*/

		return userRepository.findAll();//super.list((Criteria) criteria);
	}



	public void existeUsuario(Usuario u) throws ServiceException {
		Optional<Usuario> existingEmail = this.findByEmail(u.getEmail());
		if (!existingEmail.isPresent())
			throw new ServiceException("No existe el usuario con email " + u.getEmail());
	}

	public void usuarioNoEstaRepetido(Usuario u) throws ServiceException {
		Optional<Usuario> existingUsername = Optional.empty();
		if(u.getUsuario() != null)
			existingUsername = this.findByUsuario(u.getUsuario());
		Optional<Usuario> existingEmail = this.findByEmail(u.getEmail());
		if (existingUsername.isPresent())
			throw new ServiceException("El nombre de usuario " + existingUsername.get().getUsuario() + " ya existe");
		if (existingEmail.isPresent())
			throw new ServiceException("El email " + existingEmail.get().getEmail() + " ya existe");
	}

	public List<Usuario> getSystemAdmins() {
		/*CriteriaBuilder builder = currentSession().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> i = criteria.from(Usuario.class);

		criteria.select(i).where(builder.equal(i.get("esAdministradorSistema"), true),
				builder.equal(i.get("eliminado"), false));

		TypedQuery<Usuario> query = currentSession().createQuery(criteria);*/
		List<Usuario> listaUsuarios = userRepository.findAllByEsAdministradorSistema(true);//query.getResultList();
		return listaUsuarios;
	}

}