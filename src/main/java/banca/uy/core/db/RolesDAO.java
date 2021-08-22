package banca.uy.core.db;

import java.util.List;
import java.util.Optional;

import banca.uy.core.entity.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import banca.uy.core.repository.IRolRepository;

@Component
public class RolesDAO {

	@Autowired
	IRolRepository rolRepository;

	private final MongoOperations mongoOperations;

	public RolesDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public List<Rol> getAll() {
		return rolRepository.findAll();
	}

	public Optional<Rol> findByName(String rol) {
		Query query = new Query();
		List<Rol> listaRoles = mongoOperations.find(query.addCriteria(Criteria.where("rol").is(rol)), Rol.class);
		if (listaRoles.size() != 0)
			return Optional.of(listaRoles.get(0));
		return Optional.ofNullable(null);
	}

	public Rol findById(String rolId) {
		return rolRepository.findById(rolId).get();
	}

}