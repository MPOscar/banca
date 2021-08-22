package banca.uy.core.db;

import banca.uy.core.entity.Quiniela;
import banca.uy.core.entity.Rol;
import banca.uy.core.entity.Tombola;
import banca.uy.core.repository.IRolRepository;
import banca.uy.core.repository.ITombolaRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TombolaDAO {

	@Autowired
	ITombolaRepository tombolaRepository;

	private final MongoOperations mongoOperations;

	public TombolaDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public List<Tombola> findAllSortByFechaTirada() {
		Query query = new Query();
		List<Tombola> tombolaList = mongoOperations.find(query.with(Sort.by(Sort.Direction.DESC, "fechaTirada")), Tombola.class);
		return tombolaList;
	}

	public List<Tombola> findAllSortByFechaTirada(DateTime fecha) {
		Query query = new Query();
		List<Tombola> tombolaList = mongoOperations.find(query.with(Sort.by(Sort.Direction.DESC, "fechaTirada")).addCriteria(Criteria.where("fechaTirada").lte(fecha)).limit(10), Tombola.class);
		return tombolaList;
	}

}