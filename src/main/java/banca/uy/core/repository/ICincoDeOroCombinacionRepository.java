package banca.uy.core.repository;

import banca.uy.core.entity.CincoDeOroCombinacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICincoDeOroCombinacionRepository extends MongoRepository<CincoDeOroCombinacion, String> {
}
