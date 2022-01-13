package banca.uy.core.services.interfaces;

import java.util.List;

import banca.uy.core.exceptions.ServiceException;
import banca.uy.core.entity.Rol;

public interface IRolesService {

	public List<Rol> GetAll() throws ServiceException;

}
