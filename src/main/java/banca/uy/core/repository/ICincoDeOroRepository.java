package banca.uy.core.repository;

import banca.uy.core.entity.CincoDeOro;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICincoDeOroRepository extends MongoRepository<CincoDeOro, String> {
    public CincoDeOro findFirstByFechaTirada(DateTime fechaTirada);
}
