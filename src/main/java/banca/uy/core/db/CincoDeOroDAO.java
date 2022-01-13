package banca.uy.core.db;

import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.repository.ICincoDeOroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CincoDeOroDAO {

	@Autowired
    ICincoDeOroRepository cincoDeOroRepository;

	private final MongoOperations mongoOperations;

	public CincoDeOroDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public CincoDeOro obtenerUltimaJugadaCompleta() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(2);
		List<CincoDeOro> cincoDeOroList = mongoOperations.find(query, CincoDeOro.class);
		CincoDeOro cincoDeOro = cincoDeOroList.size() > 1 ? cincoDeOroList.get(1) : new CincoDeOro();
		return cincoDeOro;
	}

	public CincoDeOro save(CincoDeOro cincoDeOro){
		cincoDeOro = cincoDeOroRepository.save(cincoDeOro);
		if(cincoDeOro.getSId() == null){
			cincoDeOro.setSId(cincoDeOro.getId());
			cincoDeOro = cincoDeOroRepository.save(cincoDeOro);
		}
		return cincoDeOro;
	}

}