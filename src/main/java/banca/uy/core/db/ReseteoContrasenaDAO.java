package banca.uy.core.db;

import java.util.List;
import java.util.Optional;

import banca.uy.core.entity.ReseteoContrasena;
import banca.uy.core.repository.IReseteoContrasenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class ReseteoContrasenaDAO {

	@Autowired
    IReseteoContrasenaRepository reseteoContrasenaRepository;

	private final MongoOperations mongoOperations;

	public ReseteoContrasenaDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public ReseteoContrasena save(ReseteoContrasena cu) {
		cu = reseteoContrasenaRepository.save(cu);
		cu.setSId(cu.getId());
		reseteoContrasenaRepository.save(cu);
		return cu;
	}

	public Optional<ReseteoContrasena> findByCodigo(String searchCodigo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("codigo").is(searchCodigo).andOperator(Criteria.where("eliminado").is(false)));
		List<ReseteoContrasena> listaUsuarios = mongoOperations.find(query, ReseteoContrasena.class);

		if (listaUsuarios.size() != 0)
			return Optional.of(listaUsuarios.get(0));
		return Optional.ofNullable(null);
	}

	public List<ReseteoContrasena> getAll(){
		return reseteoContrasenaRepository.findAll();
	}

}
