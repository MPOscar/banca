package banca.uy.core.db;

import banca.uy.core.entity.TombolaCombinacionesDeTres;
import banca.uy.core.repository.ITombolaCombinacionesDeTresRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Component
public class TombolaCombinacionesDeTresDAO {

	@Autowired
	ITombolaCombinacionesDeTresRepository tombolaCombinacionesDeTresRepository;

	private final MongoOperations mongoOperations;

	public TombolaCombinacionesDeTresDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public TombolaCombinacionesDeTres save(TombolaCombinacionesDeTres tombolaCombinacionesDeTres){
		tombolaCombinacionesDeTres = tombolaCombinacionesDeTresRepository.save(tombolaCombinacionesDeTres);
		if(tombolaCombinacionesDeTres.getSId() == null){
			tombolaCombinacionesDeTres.setSId(tombolaCombinacionesDeTres.getId());
			tombolaCombinacionesDeTres = tombolaCombinacionesDeTresRepository.save(tombolaCombinacionesDeTres);
		}
		return tombolaCombinacionesDeTres;
	}

	public void eliminarCombinacionesDeTres() {
		tombolaCombinacionesDeTresRepository.deleteAll();
	}

	public DateTime obtenerUltimaFechaDeActualizacion() {
			Aggregation tombolaAggregation = Aggregation.newAggregation(
					match(Criteria.where("eliminado").is(false)),
					Aggregation.sort(Sort.Direction.DESC, "fechaTirada"),
					Aggregation.limit(1)
			);
			List<TombolaCombinacionesDeTres> combinacionesDeTres = mongoOperations.aggregate(tombolaAggregation, "TombolaCombinacionesDeTres", TombolaCombinacionesDeTres.class).getMappedResults();
			return combinacionesDeTres.get(0).getFechaTirada();
	}

	public TombolaCombinacionesDeTres findFirstByCombinacion(String combinacion) {
		return tombolaCombinacionesDeTresRepository.findFirstByCombinacion(combinacion);
	}
}