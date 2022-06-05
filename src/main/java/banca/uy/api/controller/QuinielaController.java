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

}