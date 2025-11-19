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
    // guardar referencia al propietario de esta sesión y registrarse como observador
    this.p = prop;
    // Registrarse tanto en la fachada como en el propietario 
    Fachada.getInstancia().agregarObservador(this);
    prop.agregarObservador(this);

    return Respuesta.lista(
      new Respuesta("nombreCompleto", prop.getNombreCompleto()),
      new Respuesta("estado", prop.getEstado().getNombre()),
      new Respuesta("saldoactual", prop.getSaldoActual()),
      new Respuesta("bonificaciones", new PropietarioDTO(prop).getAsignaciones()),
      new Respuesta("vehiculos", new PropietarioDTO(prop).getVehiculos()),
      new Respuesta("transitos", new PropietarioDTO(prop).getTransitos()),
      new Respuesta("notificaciones", new PropietarioDTO(prop).getNotificaciones()));
  }

  @PostMapping("/vistaCerrada")
  public void vistaCerrada() {
    Fachada.getInstancia().quitarObservador(this);
    if (this.p != null) {
      this.p.quitarObservador(this);
    }
    // limpiar referencia y cerrar la conexión SSE de la sesión
    this.p = null;
    conexionNavegador.cerrarConexion();
  }

  @PostMapping("/borrarNotificaciones")
  public List<Respuesta> borrarNotificaciones(@SessionAttribute(name = "usuarioPropietario") Propietario prop) {

    if (prop.getNotificaciones().isEmpty()) {
      return Respuesta.lista(
          new Respuesta("error", "No hay notificaciones para borrar"));
    }
    Fachada.getInstancia().borrarNotificacionesPropietario(prop);
    return Respuesta.lista(
      new Respuesta("notificaciones", prop.getNotificaciones()));

  }

  @Override
  public void actualizar(Object evento, Observable origen) {
    try {
      if (evento.equals(Propietario.Eventos.eliminarNotificaciones)
          || evento.equals(Propietario.Eventos.emuloTransito)) {
        if (this.p == null)
          return;
        PropietarioDTO dto = new PropietarioDTO(this.p);
        // mandamos los mismos id que la vista espera para actualizar todos sus componentes
        conexionNavegador.enviarJSON(Respuesta.lista(
            new Respuesta("transitos", dto.getTransitos()),
            new Respuesta("notificaciones", dto.getNotificaciones()),
            new Respuesta("saldoactual", dto.getSaldoActual()),
            new Respuesta("bonificaciones", dto.getAsignaciones()),
            new Respuesta("vehiculos", dto.getVehiculos()),
            new Respuesta("estado", dto.getEstado())
        ));
      }
    } catch (Exception e) {
      System.out.println("Error en actualizar SSE: " + e.getMessage());
    }
  }

}