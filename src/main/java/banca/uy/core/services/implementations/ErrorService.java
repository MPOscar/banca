package banca.uy.core.services.implementations;

import banca.uy.core.db.ErrorsDAO;
import banca.uy.core.entity.Usuario;
import banca.uy.core.services.interfaces.IErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ErrorService implements IErrorService {

	@Autowired
	private ErrorsDAO errorsRepository;

	public ErrorService (ErrorsDAO errorsDAO) {
		errorsRepository = errorsDAO;
	}

	@Override
	public void Log(String message, Usuario usuario) {
		this.errorsRepository.insert(message, usuario);
	}
	@Override
	public void Log(String message) {
		this.errorsRepository.insert(message);
	}
	@Override
	public void Log(String message, String stackTrace) {
		this.errorsRepository.insert(message, stackTrace);
	}
}
