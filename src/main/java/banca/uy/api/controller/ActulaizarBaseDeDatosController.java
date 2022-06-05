package banca.uy.api.controller;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.IActualizarBaseDeDatosService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/actualizarBaseDeDatos")
public class ActulaizarBaseDeDatosController {

  Logger logger = LogManager.getLogger(ActulaizarBaseDeDatosController.class);

  @Autowired
  IActualizarBaseDeDatosService actualizarBaseDeDatosService;

  private final IAuthenticationFacade authenticationFacade;

  public ActulaizarBaseDeDatosController(IAuthenticationFacade authenticationFacade) {
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping("")
  public ResponseEntity actualizarBaseDeDatos() {
    try {
      actualizarBaseDeDatosService.actualizarBaseDeDatos();
      return ok("terminamos de actualizar la base de datos");
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }
}