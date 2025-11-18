package ort.dda.obl.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import observador.Observable;
import observador.Observador;
import ort.dda.obl.ConexionNavegador;
import ort.dda.obl.SistemaTransitoException;
import ort.dda.obl.dto.PropietarioDTO;
import ort.dda.obl.modelo.Fachada;
import ort.dda.obl.modelo.Propietario;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Scope("session")
@RestController
@RequestMapping("/tablero")
public class ControladorTablero implements Observador {
  private Propietario p;
  private final ConexionNavegador conexionNavegador;

  public ControladorTablero(@Autowired ConexionNavegador conexionNavegador) {
    this.conexionNavegador = conexionNavegador;
  }

  @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter registrarSSE() {
    conexionNavegador.conectarSSE();
    return conexionNavegador.getConexionSSE();
  }

  @PostMapping("/vistaConectada")
  public List<Respuesta> inicializarVista(@SessionAttribute(name = "usuarioPropietario") Propietario prop)
      throws SistemaTransitoException {
    Fachada.getInstancia().agregarObservador(this);

    PropietarioDTO dto = new PropietarioDTO(prop);
    return Respuesta.lista(
        new Respuesta("nombreCompleto", dto.getNombreCompleto()),
        new Respuesta("estado", dto.getEstado()),
        new Respuesta("saldoactual", dto.getSaldoActual()),
        new Respuesta("bonificaciones", dto.getAsignaciones()),
        new Respuesta("vehiculos", dto.getVehiculos()),
        new Respuesta("transitos", dto.getTransitos()),
        new Respuesta("notificaciones", dto.getNotificaciones()));
  }

  @PostMapping("/vistaCerrada")
  public void vistaCerrada() {
    Fachada.getInstancia().quitarObservador(this);
  }

  @PostMapping("/borrarNotificaciones")
  public List<Respuesta> borrarNotificaciones(@SessionAttribute(name = "usuarioPropietario") Propietario prop) {
    Fachada.getInstancia().agregarObservador(this);

    if (prop.getNotificaciones().isEmpty()) {
      return Respuesta.lista(
          new Respuesta("error", "No hay notificaciones para borrar"));
    }
    Fachada.getInstancia().borrarNotificacionesPropietario(prop);
    PropietarioDTO dto = new PropietarioDTO(prop);
    return Respuesta.lista(
        new Respuesta("notificaciones", prop.getNotificaciones()));

  }

  private Respuesta propDTO() {
    return new Respuesta("propietario", new PropietarioDTO(p));
  }

  @Override
  public void actualizar(Object evento, Observable origen) {
    if (evento.equals(Propietario.Eventos.eliminarNotificaciones)) {
      conexionNavegador.enviarJSON(Respuesta.lista(propDTO()));
    }
  }

}