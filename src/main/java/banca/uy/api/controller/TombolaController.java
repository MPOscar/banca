package banca.uy.api.controller;

import banca.uy.core.entity.Tombola;
import banca.uy.core.resources.dto.Representacion;
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

import java.util.HashMap;
import java.util.List;

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

  @GetMapping("/obtenerUltimaJugada")
  public Representacion<Tombola> obtenerUltimaJugada() {
    try {
      Tombola tombola = tombolaService.obtenerUltimaJugada();
      return new Representacion<>(HttpStatus.OK.value(), tombola);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @PostMapping("/obtenerJugadasAnteriores")
  public Representacion<List<Tombola>> obtenerJugadasAnteriores(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size,
          @RequestBody Tombola tombola
  ) {
    try {
      List<Tombola> ultimasJugadas = tombolaService.obtenerJugadasAnteriores(tombola, page, size);
      return new Representacion<>(HttpStatus.OK.value(), ultimasJugadas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @PostMapping("/obtenerJugadasPosteriores")
  public Representacion<List<Tombola>> obtenerJugadasPosteriores(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size,
          @RequestBody Tombola tombola
  ) {
    try {
      List<Tombola> ultimasJugadas = tombolaService.obtenerJugadasPosteriores(tombola, page, size);
      return new Representacion<>(HttpStatus.OK.value(), ultimasJugadas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerUltimasJugadas")
  public Representacion<List<Tombola>> obtenerUltimasJugadas(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size
  ) {
    try {
      List<Tombola> ultimasJugadas = tombolaService.obtenerUltimasJugadas(page, size);
      return new Representacion<>(HttpStatus.OK.value(), ultimasJugadas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerJugadasCincoDeOroConMayorNumeroDeCoincidencias")
  public Representacion<HashMap<Integer, List<Tombola>>> obtenerJugadasCincoDeOroConMayorNumeroDeCoincidencias(
          @RequestParam(defaultValue = "1") int numeroDeCoincidencias
  ) {
    try {
      HashMap<Integer, List<Tombola>> jugadasTombolaConMayorNumeroDeCoincidencias = tombolaService.obtenerJugadasTombolaConMayorNumeroDeCoincidencias(numeroDeCoincidencias);
      return new Representacion<>(HttpStatus.OK.value(), jugadasTombolaConMayorNumeroDeCoincidencias);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}