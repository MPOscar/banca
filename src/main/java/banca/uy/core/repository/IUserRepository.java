package banca.uy.core.repository;

import banca.uy.core.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends MongoRepository<Usuario, String> {

    public Usuario findFirstByOldId(long oldId);

    public List<Usuario> findAll();

    public Usuario findOneById(String id);

    public Optional<Usuario> findByUsuario(String usuario);

    public List<Usuario> findAllByUsuario(String usuario);

    public Optional<Usuario> findByEmail(String email);

    public List<Usuario> findAllByEmail(String email);

    public List<Usuario> findAllByEsAdministradorSistema(boolean esAdministradorSistema);

}
