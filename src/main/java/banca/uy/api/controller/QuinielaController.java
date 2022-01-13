package banca.uy.api.controller;

import banca.uy.core.entity.Quiniela;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.IQuinielaService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/quiniela")
public class QuinielaController {

  Logger logger = LogManager.getLogger(QuinielaController.class);

  @Autowired
  IQuinielaService quinielaService;

  private final IAuthenticationFacade authenticationFacade;

  public QuinielaController(IAuthenticationFacade authenticationFacade) {
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping("/tirada")
  public ResponseEntity saveTirada(@RequestBody String tirada) {
    try {
      Quiniela quiniela = quinielaService.saveTirada(tirada);
      return ok(quiniela);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/jugada")
  public ResponseEntity getJugada() {
    try {
      Set<Integer> jugada = quinielaService.getJugada();
      return ok(jugada);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/numerosSinSalir")
  public ResponseEntity getNumerosSinSalir(@RequestParam(defaultValue = "") String fecha) {
    try {
      Set<Integer> jugada = quinielaService.getNumerosSinSalir(fecha);
      return ok(jugada);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}