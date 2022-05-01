package banca.uy.core.db;

import banca.uy.core.entity.CincoDeOroCombinacion;
import banca.uy.core.repository.ICincoDeOroCombinacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class CincoDeOroCombinacionDAO {

	@Autowired
    ICincoDeOroCombinacionRepository cincoDeOroCombinacionRepository;

	private final MongoOperations mongoOperations;

	public CincoDeOroCombinacionDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public CincoDeOroCombinacion save(CincoDeOroCombinacion cincoDeOroCombinacion) {
		cincoDeOroCombinacion = cincoDeOroCombinacionRepository.save(cincoDeOroCombinacion);
		if (cincoDeOroCombinacion.getSId() == null) {
			cincoDeOroCombinacion.setSId(cincoDeOroCombinacion.getId());
			cincoDeOroCombinacion = cincoDeOroCombinacionRepository.save(cincoDeOroCombinacion);
		}
		return cincoDeOroCombinacion;
	}

}