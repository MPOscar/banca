package banca.uy.core.db;

import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.entity.Tombola;
import banca.uy.core.repository.ITombolaRepository;
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
		Tombola tombola = mongoOperations.findOne(query, Tombola.class);
		return tombola;
	}

	public List<Tombola> obtenerJugadasAnterioresCincoDeOro(Tombola tombola, int page, int size) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fechaTirada").lte(tombola.getFechaTirada()));
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(size);
		query.skip((page - 1) * size);
		List<Tombola> tombolaJugadasAnteriores = mongoOperations.find(query, Tombola.class);
		return tombolaJugadasAnteriores;
	}

	public List<Tombola> obtenerJugadasPosterioresCincoDeOro(Tombola tombola, int page, int size) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fechaTirada").gt(tombola.getFechaTirada()));
		query.with(Sort.by(Sort.Direction.ASC, "fechaTirada"));
		query.limit(size);
		query.skip((page - 1) * size);
		List<Tombola> tombolaJugadasAnteriores = mongoOperations.find(query, Tombola.class);
		return tombolaJugadasAnteriores;
	}

	public List<Tombola> obtenerUltimasJugadas(int page, int size) {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(size);
		query.skip((page - 1) * size);
		List<Tombola> ultimasJugadas = mongoOperations.find(query, Tombola.class);
		return ultimasJugadas;
	}

	public List<Tombola> obtenerJugadasTombolaConCoincidencias(Tombola tombola) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sid").ne(tombola.getSId()).andOperator(Criteria.where("sorteo").in(tombola.getSorteo()), Criteria.where("eliminado").is(false)));
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		List<Tombola>jugadasTombolaConCoincidencias = mongoOperations.find(query, Tombola.class);
		return jugadasTombolaConCoincidencias;
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