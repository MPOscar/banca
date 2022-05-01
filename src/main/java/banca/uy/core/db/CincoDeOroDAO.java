package banca.uy.core.db;

import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.repository.ICincoDeOroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
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
		CincoDeOro cincoDeOro = cincoDeOroList.size() > 1 ? cincoDeOroList.get(0) : new CincoDeOro();
		return cincoDeOro;
	}

	public List<CincoDeOro> obtenerUltimasJugadas(int page, int size) {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(size);
		query.skip((page - 1) * size);
		List<CincoDeOro> ultimasJugadas = mongoOperations.find(query, CincoDeOro.class);
		return ultimasJugadas;
	}

	public CincoDeOro obtenerJugadaAnteriorCincoDeOro(CincoDeOro cincoDeOro) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fechaTirada").lt(cincoDeOro.getFechaTirada()));
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(2);
		List<CincoDeOro> cincoDeOroJugadas = mongoOperations.find(query, CincoDeOro.class);
		CincoDeOro jugadaAnteriorCincoDeOro = cincoDeOroJugadas.size() > 1 ? cincoDeOroJugadas.get(0) : new CincoDeOro();
		return jugadaAnteriorCincoDeOro;
	}

	public List<CincoDeOro> obtenerJugadasAnterioresCincoDeOro(CincoDeOro cincoDeOro, int page, int size) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fechaTirada").lte(cincoDeOro.getFechaTirada()));
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		query.limit(size);
		query.skip((page - 1) * size);
		List<CincoDeOro> cincoDeOroJugadasAnteriores = mongoOperations.find(query, CincoDeOro.class);
		return cincoDeOroJugadasAnteriores;
	}

	public List<CincoDeOro> obtenerJugadasPosterioresCincoDeOro(CincoDeOro cincoDeOro, int page, int size) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fechaTirada").gt(cincoDeOro.getFechaTirada()));
		query.with(Sort.by(Sort.Direction.ASC, "fechaTirada"));
		query.limit(size);
		query.skip((page - 1) * size);
		List<CincoDeOro> cincoDeOroJugadasAnteriores = mongoOperations.find(query, CincoDeOro.class);
		return cincoDeOroJugadasAnteriores;
	}

	public List<CincoDeOro> obtenerJugadasCincoDeOroConCoincidencias(CincoDeOro cincoDeOro) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sid").ne(cincoDeOro.getSId()).andOperator(Criteria.where("cincoDeOro").in(cincoDeOro.getCincoDeOro()), Criteria.where("eliminado").is(false)));
		query.with(Sort.by(Sort.Direction.DESC, "fechaTirada"));
		List<CincoDeOro>jugadasCincoDeOroConCoincidencias = mongoOperations.find(query, CincoDeOro.class);
		return jugadasCincoDeOroConCoincidencias;
	}

	public CincoDeOro obtenerJugadaConCoincidencia(List<Integer> jugada) {
		Query query = new Query();
		query.addCriteria(Criteria.where("cincoDeOro").all(jugada));
		CincoDeOro cincoDeOro = mongoOperations.findOne(query, CincoDeOro.class);
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