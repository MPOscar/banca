package banca.uy.core.db;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import banca.uy.core.entity.CodigoUsuario;
import banca.uy.core.repository.ICodigoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CodigosUsuariosDAO {

	@PersistenceContext
	private EntityManager em;

	@Autowired
    ICodigoUsuarioRepository codigoUsuarioRepository;

	private final MongoOperations mongoOperations;

	public CodigosUsuariosDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public CodigoUsuario save(CodigoUsuario u) {
		u = codigoUsuarioRepository.save(u);
		u.setSId(u.getId());
		codigoUsuarioRepository.save(u);
		return u;
	}

	public void update(CodigoUsuario cu) {
		codigoUsuarioRepository.save(cu);
	}

	public Optional<CodigoUsuario> findByCodigo(String searchCodigo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("codigo").is(searchCodigo).andOperator(Criteria.where("eliminado").is(false)));
		List<CodigoUsuario> listaUsuarios = mongoOperations.find(query, CodigoUsuario.class);

		if (listaUsuarios.size() != 0)
			return Optional.of(listaUsuarios.get(0));
		return Optional.ofNullable(null);
	}

	public Optional<CodigoUsuario> findByUserAndBusiness(String userId, String businessId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("susuario").is(userId).andOperator(Criteria.where("sempresa").is(businessId), Criteria.where("expirado").is(false), Criteria.where("usado").is(false), Criteria.where("eliminado").is(false)));
		List<CodigoUsuario> listaUsuarios = mongoOperations.find(query, CodigoUsuario.class);

		if (listaUsuarios.size() != 0)
			return Optional.of(listaUsuarios.get(0));
		return Optional.ofNullable(null);
	}

	public List<CodigoUsuario> getAll(){
		return codigoUsuarioRepository.findAll();
	}

}
