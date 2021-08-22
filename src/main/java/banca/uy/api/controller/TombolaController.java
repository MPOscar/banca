package banca.uy.api.controller;

import banca.uy.core.entity.Tombola;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.IErrorService;
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
  private IErrorService errorService;

  @Autowired
  ITombolaService tombolaService;

  private final IAuthenticationFacade authenticationFacade;

  public TombolaController(IAuthenticationFacade authenticationFacade) {
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping("/tirada")
  public ResponseEntity saveTirada(@RequestBody String tirada) {
    try {
      Tombola tombola = tombolaService.saveTirada(tirada);
      return ok(tombola);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      this.errorService
              .Log("AtributosLaboratorioController controller @PostMapping(\"/excel/actualizar\") Error: " + ex.getMessage(), " StackTrace: " + ex.getStackTrace());
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
      this.errorService
              .Log("AtributosLaboratorioController controller @PostMapping(\"/excel/actualizar\") Error: " + ex.getMessage(), " StackTrace: " + ex.getStackTrace());
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
      this.errorService
              .Log("AtributosLaboratorioController controller @PostMapping(\"/excel/actualizar\") Error: " + ex.getMessage(), " StackTrace: " + ex.getStackTrace());
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
      this.errorService
              .Log("AtributosLaboratorioController controller @PostMapping(\"/excel/actualizar\") Error: " + ex.getMessage(), " StackTrace: " + ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}