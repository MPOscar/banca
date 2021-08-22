package banca.uy.core.repository;

import banca.uy.core.entity.Entidad;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IEntidadRepository extends MongoRepository<Entidad, String> {
}
