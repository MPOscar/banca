package banca.uy.core.services.implementations;

import java.util.List;

import banca.uy.core.db.RolesDAO;
import banca.uy.core.entity.Rol;
import banca.uy.core.exceptions.ServiceException;
import banca.uy.core.services.interfaces.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService implements IRolesService {
	@Autowired
	private RolesDAO rolesRepository;

	public RolesService(RolesDAO rolesDAO) {
		this.rolesRepository = rolesDAO;
	}

	@Override
	public List<Rol> GetAll() throws ServiceException {
		return this.rolesRepository.getAll();
	}

}
