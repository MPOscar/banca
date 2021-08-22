package banca.uy.api.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import banca.uy.core.entity.Rol;
import banca.uy.core.resources.dto.Representacion;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.IErrorService;
import banca.uy.core.services.interfaces.ILoginService;
import banca.uy.core.services.interfaces.IRolesService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import banca.uy.core.exceptions.ServiceException;

@RestController
@RequestMapping("/roles")
public class RoleController {
	@SuppressWarnings("unused")
	Logger logger = LogManager.getLogger(RoleController.class);

	private final IAuthenticationFacade authenticationFacade;
	private final IRolesService rolesService;
	@Autowired
	private IErrorService errorService;

	@Autowired
	private ILoginService loginService;

	public RoleController(IRolesService rolesService, IErrorService errorService, IAuthenticationFacade authenticationFacade) {
		this.rolesService = rolesService;
		this.errorService = errorService;
		this.authenticationFacade = authenticationFacade;
	}

	@GetMapping("")
	public ResponseEntity getRoles() {
		try {
			List<Rol> allRoles = this.rolesService.GetAll();
			return ok(new Representacion<List<Rol>>(HttpStatus.OK.value(), allRoles));
		} catch (ServiceException ex) {
			logger.log(Level.ERROR, "role controller @GetMapping(\"\") Error:", ex.getMessage(), ex.getStackTrace());
			this.errorService
					.Log("role controller @GetMapping(\"\") Error: " + ex.getMessage() , " StackTrace: " + ex.getStackTrace());
			throw new WebApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			logger.log(Level.ERROR, "role controller @GetMapping(\"\") Error:", ex.getMessage(), ex.getStackTrace());
			this.errorService
					.Log("role controller @GetMapping(\"\") Error: " + ex.getMessage() , " StackTrace: " + ex.getStackTrace());
			throw new WebApplicationException("Ocurró un error inesperado, intente nuevamente - " + ex.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
