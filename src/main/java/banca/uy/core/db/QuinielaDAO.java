package banca.uy.core.db;

import banca.uy.core.entity.Quiniela;
import banca.uy.core.repository.IQuinielaRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuinielaDAO {

	@Autowired
    IQuinielaRepository quinielaRepository;

	private final MongoOperations mongoOperations;

	public QuinielaDAO(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public List<Quiniela> findAllSortByFechaTirada() {
		Query query = new Query();
		List<Quiniela> quinielaList = mongoOperations.find(query.with(Sort.by(Sort.Direction.DESC, "fechaTirada")), Quiniela.class);
		return quinielaList;
	}

	public List<Quiniela> findAllSortByFechaTirada(DateTime fecha) {
		Query query = new Query();
		List<Quiniela> quinielaList = mongoOperations.find(query.with(Sort.by(Sort.Direction.DESC, "fechaTirada")).addCriteria(Criteria.where("fechaTirada").lte(fecha)).limit(30), Quiniela.class);
		return quinielaList;
	}

}