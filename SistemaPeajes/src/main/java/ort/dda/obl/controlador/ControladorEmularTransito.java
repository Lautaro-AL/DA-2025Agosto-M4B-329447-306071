package ort.dda.obl.controlador;

import ort.dda.obl.ConexionNavegador;
import ort.dda.obl.SistemaTransitoException;
import ort.dda.obl.UsuarioException;
import ort.dda.obl.dto.ResultadoEmulacionDTO;
import ort.dda.obl.modelo.Administrador;
import ort.dda.obl.modelo.Asignacion;
import ort.dda.obl.modelo.Fachada;
import ort.dda.obl.modelo.Propietario;
import ort.dda.obl.modelo.PuestoPeaje;
import ort.dda.obl.modelo.Tarifa;
import ort.dda.obl.modelo.Vehiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import observador.Observable;
import observador.Observador;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Scope("session")
@RestController
@RequestMapping("/admin/transito")
public class ControladorEmularTransito implements Observador {
    ResultadoEmulacionDTO res;
    private final ConexionNavegador conexionNavegador;

    public ControladorEmularTransito(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

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

    @PostMapping("/puestos")
    public List<Respuesta> cargarPuestos(@SessionAttribute(name = "usuarioAdmin") Administrador admin)
            throws UsuarioException {
        List<PuestoPeaje> puestos = Fachada.getInstancia().getPuestosPeaje();
        return Respuesta.lista(new Respuesta("puestos", puestos));

    }

    @PostMapping("/tarifas")
    public List<Respuesta> cargarTarifas(@SessionAttribute(name = "usuarioAdmin") Administrador admin,
            @RequestParam String nombrePuesto) throws UsuarioException {
        PuestoPeaje puestoSeleccionado = Fachada.getInstancia().buscarPuestoPorNombre(nombrePuesto);
        if (puestoSeleccionado == null) {
            return Respuesta.lista(new Respuesta("error", "Puesto no encontrado"));
        }

        List<Tarifa> tarifas = Fachada.getInstancia().obtenerTarifasPorPuesto(puestoSeleccionado);
        return Respuesta.lista(new Respuesta("tarifas", tarifas));

    }

    @PostMapping("/emular")
    public List<Respuesta> emularTransito(@SessionAttribute(name = "usuarioAdmin") Administrador admin,
            @RequestParam String matricula, @RequestParam String nombrePuesto,
            @RequestParam String categoriaVehiculo, @RequestParam double monto,
            @RequestParam String fechaHora) throws UsuarioException {
        try {
            Date fecha = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            fecha = sdf.parse(fechaHora);
            Vehiculo vehiculo = Fachada.getInstancia().buscarVehiculoPorMatricula(matricula);
            if (vehiculo == null) {
                return Respuesta.lista(new Respuesta("error", "No existe el vehículo"));
            }

            Propietario propietario = vehiculo.getPropietario();
            if (propietario == null) {
                return Respuesta.lista(new Respuesta("error", "El vehículo no tiene propietario"));
            }

            PuestoPeaje puesto = Fachada.getInstancia().buscarPuestoPorNombre(nombrePuesto);
            if (puesto == null) {
                return Respuesta.lista(new Respuesta("error", "Puesto no encontrado"));
            }

            Tarifa tarifaSeleccionada = Fachada.getInstancia().buscarTarifaPorMontoYCategoria(monto, categoriaVehiculo);
            if (tarifaSeleccionada == null) {
                return Respuesta.lista(new Respuesta("error", "Tarifa no encontrada"));
            }
            try {
                Fachada.getInstancia().emularTransito(vehiculo, puesto, tarifaSeleccionada, propietario, fecha);
            } catch (SistemaTransitoException e) {
                return Respuesta.lista(new Respuesta("error", e.getMessage()));
            }

            String nombreBonificacion = "Sin bonificación";
            if (propietario.getAsignaciones() != null && !propietario.getAsignaciones().isEmpty()) {
                Asignacion asig = propietario.getAsignaciones().get(0);
                nombreBonificacion = asig.getBonificacion().getNombre();
            }

            double costoFinal = monto;

            res = new ResultadoEmulacionDTO(propietario.getNombreCompleto(),
                    propietario.getEstado().getNombre(), vehiculo.getCategoria().getTipo(), nombreBonificacion,
                    costoFinal, propietario.getSaldoActual());

            return Respuesta.lista(new Respuesta("resultadoTransito", res));
        } catch (Exception e) {
            return Respuesta.lista(new Respuesta("error", "Error al emular tránsito: " + e.getMessage()));
        }
    }

    private Respuesta resultadoDTO() {
        return new Respuesta("resultado", new ResultadoEmulacionDTO(res));
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (evento.equals(Propietario.Eventos.emuloTransito)) {
            conexionNavegador.enviarJSON(Respuesta.lista(resultadoDTO()));
        }
    }

}