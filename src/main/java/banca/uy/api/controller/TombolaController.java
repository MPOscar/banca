package banca.uy.api.controller;

import banca.uy.core.dto.EstadisticaTombola;
import banca.uy.core.entity.Tombola;
import banca.uy.core.resources.dto.Representacion;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.ITombolaCombinacionesDeSieteService;
import banca.uy.core.services.interfaces.ITombolaCombinacionesDeTresService;
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

  @Autowired
  ITombolaCombinacionesDeTresService tombolaCombinacionesDeTresService;

  @Autowired
  ITombolaCombinacionesDeSieteService tombolaCombinacionesDeSieteService;

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

  @GetMapping("/obtenerJugadasTombolaConMayorNumeroDeCoincidencias")
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

  @GetMapping("/estadisticas")
  public Representacion estadisticas() {
    try {
      HashMap<String, Integer> estadisticas = tombolaService.estadisticas();
      return new Representacion<>(HttpStatus.OK.value(), estadisticas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/estadisticasJugadasRepetidas")
  public Representacion estadisticasJugadasRepetidas() {
    try {
      List<EstadisticaTombola> estadisticas = tombolaService.estadisticasJugadasMayorNumeroCoincidenciasRepetidas();
      return new Representacion<>(HttpStatus.OK.value(), estadisticas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/estadisticasUltimaJugada")
  public Representacion estadisticasUltimaJugada(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "5") int limit
  ) {
    try {
      List<EstadisticaTombola> estadisticas = tombolaService.estadisticasUltimaJugada(page, limit);
      return new Representacion<>(HttpStatus.OK.value(), estadisticas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerTodasLasJugadasDeTresTombola")
  public Representacion obtenerTodasLasJugadasDeTresTombola() {
    try {
      HashMap<String, Integer> jugadasDeTres = tombolaCombinacionesDeTresService.obtenerTodasLasJugadasDeTresTombola();
      return new Representacion<>(HttpStatus.OK.value(), jugadasDeTres);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/inicializarTodasLasJugadasDeTresTombola")
  public Representacion inicializarTodasLasJugadasDeTresTombola() {
    try {
      tombolaCombinacionesDeTresService.inicializarTodasLasJugadasDeTresTombola();
      return new Representacion<>(HttpStatus.OK.value(), "inicializarTodasLasJugadasDeTresTombola");
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/actualizarTodasLasJugadasDeTresTombola")
  public Representacion actualizarTodasLasJugadasDeTresTombola() {
    try {
      tombolaCombinacionesDeTresService.actualizarTodasLasJugadasDeTresTombola();
      return new Representacion<>(HttpStatus.OK.value(), "actualizarTodasLasJugadasDeTresTombola");
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/inicializarTodasLasJugadasDeSieteTombola")
  public Representacion inicializarTodasLasJugadasDeSieteTombola() {
    try {
      tombolaCombinacionesDeSieteService.inicializarTodasLasJugadasDeSieteTombola();
      return new Representacion<>(HttpStatus.OK.value(), "inicializarTodasLasJugadasDeSieteTombola");
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}