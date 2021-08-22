package banca.uy.core.repository;

import banca.uy.core.entity.CodigoUsuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICodigoUsuarioRepository extends MongoRepository<CodigoUsuario, String> {
    public CodigoUsuario findFirstByOldId(long oldId);
}
