package banca.uy.core.repository;

import banca.uy.core.entity.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRolRepository extends MongoRepository<Rol, String> {
    public Rol findFirstByOldId(long oldId);
}
