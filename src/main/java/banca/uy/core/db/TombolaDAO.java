package banca.uy.core.db;

import banca.uy.core.entity.Tombola;
import banca.uy.core.repository.ITombolaRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TombolaDAO {

	@Autowired
    ITombolaRepository tombolaRepository;

	private final MongoOperations mongoOperations;

	public TombolaDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public Tombola obtenerUltimaJugadaCompleta() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(2);
		List<Tombola> tombolaList = mongoOperations.find(query, Tombola.class);
		Tombola tombola = tombolaList.size() > 1 ? tombolaList.get(1) : new Tombola();
		return tombola;
	}


	public List<Tombola> findAllSortByFechaTirada(DateTime fecha) {
		Query query = new Query();
		List<Tombola> tombolaList = mongoOperations.find(query.with(Sort.by(Sort.Direction.DESC, "fechaTirada")).addCriteria(Criteria.where("fechaTirada").lte(fecha)).limit(10), Tombola.class);
		return tombolaList;
	}

	public Tombola save(Tombola tombola){
		tombola = tombolaRepository.save(tombola);
		if(tombola.getSId() == null){
			tombola.setSId(tombola.getId());
			tombola = tombolaRepository.save(tombola);
		}
		return tombola;

	}

}