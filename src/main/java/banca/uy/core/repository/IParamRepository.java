package banca.uy.core.repository;

import banca.uy.core.entity.Param;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IParamRepository extends MongoRepository<Param, String> {
    public Param findFirstByNombre(String nombre);

    Optional<Param> findByNombreAndEliminadoIsFalse(String propiedad);

    List<Param> findAllByEliminadoIsFalse();
}
