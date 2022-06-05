package banca.uy.core.db;

import banca.uy.core.entity.TombolaCombinacionesDeSiete;
import banca.uy.core.entity.TombolaCombinacionesDeTres;
import banca.uy.core.repository.ITombolaCombinacionesDeSieteRepository;
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
public class TombolaCombinacionesDeSieteDAO {

	@Autowired
	ITombolaCombinacionesDeSieteRepository tombolaCombinacionesDeSieteRepository;

	private final MongoOperations mongoOperations;

	public TombolaCombinacionesDeSieteDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public TombolaCombinacionesDeSiete save(TombolaCombinacionesDeSiete tombolaCombinacionesDeSiete){
		tombolaCombinacionesDeSiete = tombolaCombinacionesDeSieteRepository.save(tombolaCombinacionesDeSiete);
		if(tombolaCombinacionesDeSiete.getSId() == null){
			tombolaCombinacionesDeSiete.setSId(tombolaCombinacionesDeSiete.getId());
			tombolaCombinacionesDeSiete = tombolaCombinacionesDeSieteRepository.save(tombolaCombinacionesDeSiete);
		}
		return tombolaCombinacionesDeSiete;
	}

	public void eliminarCombinacionesDeSiete() {
		tombolaCombinacionesDeSieteRepository.deleteAll();
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

	public TombolaCombinacionesDeSiete findFirstByCombinacion(String combinacion) {
		return tombolaCombinacionesDeSieteRepository.findFirstByCombinacion(combinacion);
	}
}