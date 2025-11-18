package ort.dda.obl.controlador;

import java.util.List;

import org.springframework.boot.context.FileEncodingApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import observador.Observable;
import observador.Observador;
import ort.dda.obl.UsuarioException;
import ort.dda.obl.dto.PropietarioDTO;
import ort.dda.obl.modelo.Administrador;
import ort.dda.obl.modelo.Fachada;
import ort.dda.obl.modelo.Propietario;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Scope("session")
@RequestMapping("/admin/estado")
public class ControladorEstado implements Observador {

    @PostMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(@SessionAttribute(name = "usuarioAdmin") Administrador admin)
            throws UsuarioException {
        Fachada.getInstancia().agregarObservador(this);
        return Respuesta.lista();
    }

    @PostMapping("/vistaCerrada")
    public void vistaCerrada() {
        Fachada.getInstancia().quitarObservador(this);
    }

    @PostMapping("/buscar")
    public List<Respuesta> buscarPropietario(@SessionAttribute(name = "usuarioAdmin") Administrador admin,
            @RequestParam String cedula) {
        Propietario prop = Fachada.getInstancia().buscarPropXCedula(cedula);
        if (prop == null) {
            return Respuesta.lista(new Respuesta("error", "No existe el propietario"));
        }
        PropietarioDTO dto = new PropietarioDTO(prop);
        return Respuesta.lista(new Respuesta("propietario", dto));
    }

    @PostMapping("/cambiarEstado")
    public List<Respuesta> cambiarEstado(@SessionAttribute(name = "usuarioAdmin") Administrador admin,
            @RequestParam String cedula, @RequestParam String nuevoEstado) {
        Propietario prop = Fachada.getInstancia().buscarPropXCedula(cedula);
        if (prop == null) {
            return Respuesta.lista(new Respuesta("error", "No existe el propietario"));
        }
        String estadoActual = prop.getEstado().getNombre();

        if (estadoActual.equalsIgnoreCase(nuevoEstado)) {
            return Respuesta.lista(new Respuesta("error", "El propietario ya tiene el estado: " + estadoActual));
        }
        Fachada.getInstancia().cambiarEstado(prop, nuevoEstado);
        PropietarioDTO dto = new PropietarioDTO(prop);
        return Respuesta.lista(new Respuesta("propietario", dto),
                new Respuesta("mensaje", "Estado cambiado exitosamente a: " + nuevoEstado));
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }
}