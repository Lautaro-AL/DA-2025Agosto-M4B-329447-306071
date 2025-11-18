package ort.dda.obl.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ort.dda.obl.ConexionNavegador;
import ort.dda.obl.modelo.Administrador;

@RestController
@RequestMapping("/menu")
@Scope("session")
public class ControladorMenu {
    private final ConexionNavegador conexionNavegador;

    public ControladorMenu(@Autowired ConexionNavegador conexionNavegador) { // @autowired hace una inyeccion de
                                                                             // dependencias de la clase
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();

    }

    @PostMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(@SessionAttribute(name = "usuarioAdmin") Administrador administrador) {

        return Respuesta.lista(new Respuesta("nombreCompleto", administrador.getNombreCompleto()));

    }
}
