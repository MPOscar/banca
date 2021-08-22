package banca.uy.core.repository;

import banca.uy.core.entity.Error;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IErrorRepository extends MongoRepository<Error, String> {
    public Error findFirstByOldId(long oldId);
}
