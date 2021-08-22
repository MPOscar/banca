package banca.uy.core.repository;

import banca.uy.core.entity.Tombola;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITombolaRepository extends MongoRepository<Tombola, String> {
    public Tombola findFirstByFechaTirada(DateTime fechaTirada);
}
