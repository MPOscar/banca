package banca.uy.core.repository;

import banca.uy.core.entity.TombolaCombinacionesDeTres;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ITombolaCombinacionesDeTresRepository extends MongoRepository<TombolaCombinacionesDeTres, String> {
    TombolaCombinacionesDeTres findFirstByCombinacion(String combinacion);
}
