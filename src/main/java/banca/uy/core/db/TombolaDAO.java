package banca.uy.core.db;

import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.entity.Tombola;
import banca.uy.core.entity.TombolaCombinacionesDeTres;
import banca.uy.core.repository.ITombolaRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

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

	public List<Tombola> obtenerUltimasJugadas(int page, int limit) {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(limit);
		query.skip((page - 1) * limit);
		List<Tombola> ultimasJugadas = mongoOperations.find(query, Tombola.class);
		return ultimasJugadas;
	}

	public List<Tombola> obtenerTodasLasJugadas() {
		Aggregation tombolaAggregation = Aggregation.newAggregation(
				match(Criteria.where("eliminado").is(false)),
				Aggregation.sort(Sort.Direction.ASC, "fechaTirada")
				);
		List<Tombola> jugadas = mongoOperations.aggregate(tombolaAggregation, "Tombola", Tombola.class).getMappedResults();
		return jugadas;
	}

	public List<Tombola> obtenerTodasLasJugadas(DateTime fechaTirada) {
		Aggregation tombolaAggregation = Aggregation.newAggregation(
				match(Criteria.where("eliminado").is(false).andOperator(Criteria.where("fechaTirada").gt(fechaTirada))),
				Aggregation.sort(Sort.Direction.ASC, "fechaTirada")
		);
		List<Tombola> jugadas = mongoOperations.aggregate(tombolaAggregation, "Tombola", Tombola.class).getMappedResults();
		return jugadas;
	}

	public List<Tombola> obtenerJugadasAnteriones(Tombola tombola, int cantidadDeJugadas) {
		Aggregation tombolaAggregation = Aggregation.newAggregation(
				match(Criteria.where("eliminado").is(false).andOperator(Criteria.where("fechaTirada").lt(tombola.getFechaTirada()))),
				Aggregation.sort(Sort.Direction.DESC, "fechaTirada"),
				Aggregation.limit((long) cantidadDeJugadas)
		);
		List<Tombola> jugadas = mongoOperations.aggregate(tombolaAggregation, "Tombola", Tombola.class).getMappedResults();
		return jugadas;
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

	public Optional<Tombola> findById(String tombolaId) {
		return tombolaRepository.findById(tombolaId);
	}

	public DateTime obtenerUltimaFechaDeTirada() {
		Aggregation tombolaAggregation = Aggregation.newAggregation(
				match(Criteria.where("eliminado").is(false)),
				Aggregation.sort(Sort.Direction.DESC, "fechaTirada"),
				Aggregation.limit(1)
		);
		List<Tombola> combinacionesDeTres = mongoOperations.aggregate(tombolaAggregation, "Tombola", Tombola.class).getMappedResults();
		return combinacionesDeTres.get(0).getFechaTirada();
	}

}