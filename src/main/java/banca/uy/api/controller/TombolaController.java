package banca.uy.api.controller;

import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.ITombolaService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;

import java.util.List;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/tombola")
public class TombolaController {

  Logger logger = LogManager.getLogger(TombolaController.class);

  @Autowired
  ITombolaService tombolaService;

  private final IAuthenticationFacade authenticationFacade;

  public TombolaController(IAuthenticationFacade authenticationFacade) {
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping("/inicializarBaseDeDatos")
  public ResponseEntity inicializarBaseDeDatos(@RequestBody String tirada) {
    try {
      tombolaService.inicializarBaseDeDatos(tirada);
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
      tombolaService.actualizarBaseDeDatos();
      return ok("terminamos de actualizar la base de datos");
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/jugada")
  public ResponseEntity getJugada(@RequestParam(defaultValue = "") String fecha) {
    try {
      Set<Integer> jugada = tombolaService.getJugada(fecha);
      return ok(jugada);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/repetidas")
  public ResponseEntity getJugadasRepetidas(@RequestParam(defaultValue = "") String fecha) {
    try {
      List<String> jugada = tombolaService.getJugadaRepetidas(fecha);
      return ok(jugada);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/ultimasTres")
  public ResponseEntity getUltimasTres(@RequestParam(defaultValue = "") String fecha) {
    try {
      List<String> jugada = tombolaService.getJugadaRepetidas(fecha);
      return ok(jugada);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}