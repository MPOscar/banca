package banca.uy.core.services.implementations;

import banca.uy.core.repository.IQuinielaRepository;
import banca.uy.core.db.QuinielaDAO;
import banca.uy.core.entity.Quiniela;
import banca.uy.core.services.interfaces.IQuinielaService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuinielaService implements IQuinielaService {

	@Autowired
	private IQuinielaRepository quinielaRepository;

	@Autowired
	private QuinielaDAO quinielaDAO;

	public QuinielaService(IQuinielaRepository quinielaRepository) {
		this.quinielaRepository = quinielaRepository;
	}


}
