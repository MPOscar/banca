package banca.uy.core.repository;

import banca.uy.core.entity.TombolaCombinacionesDeSiete;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ITombolaCombinacionesDeSieteRepository extends MongoRepository<TombolaCombinacionesDeSiete, String> {
    TombolaCombinacionesDeSiete findFirstByCombinacion(String combinacion);
}
