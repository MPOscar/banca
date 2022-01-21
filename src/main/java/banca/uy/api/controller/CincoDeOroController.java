package banca.uy.api.controller;

import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.resources.dto.LoginResponse;
import banca.uy.core.resources.dto.Representacion;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.ICincoDeOroService;
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
@RequestMapping("/cincoDeOro")
public class CincoDeOroController {

  Logger logger = LogManager.getLogger(CincoDeOroController.class);

  @Autowired
  ICincoDeOroService cincoDeOroService;

  private final IAuthenticationFacade authenticationFacade;

  public CincoDeOroController(IAuthenticationFacade authenticationFacade) {
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping("/inicializarBaseDeDatos")
  public ResponseEntity inicializarBaseDeDatos(@RequestBody String tirada) {
    try {
      cincoDeOroService.inicializarBaseDeDatos(tirada);
      return ok("terminamos de actualizar la base de datos " + tirada);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @PostMapping("/actualizarBaseDeDatos")
  public ResponseEntity actualizarBaseDeDatos() {
    try {
      cincoDeOroService.actualizarBaseDeDatos();
      return ok("terminamos de actualizar la base de datos");
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerUltimaJugada")
  public Representacion<CincoDeOro> obtenerUltimaJugada() {
    try {
      CincoDeOro cincoDeOro = cincoDeOroService.obtenerUltimaJugada();
      return new Representacion<>(HttpStatus.OK.value(), cincoDeOro);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

  }

}