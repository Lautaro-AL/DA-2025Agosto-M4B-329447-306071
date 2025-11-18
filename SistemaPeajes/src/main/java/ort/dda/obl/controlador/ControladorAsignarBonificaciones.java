package ort.dda.obl.controlador;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import observador.Observable;
import observador.Observador;
import ort.dda.obl.ConexionNavegador;
import ort.dda.obl.SistemaTransitoException;
import ort.dda.obl.UsuarioException;
import ort.dda.obl.modelo.Asignacion;
import ort.dda.obl.modelo.Bonificacion;
import ort.dda.obl.modelo.Exonerados;
import ort.dda.obl.modelo.Fachada;
import ort.dda.obl.modelo.Frecuentes;
import ort.dda.obl.modelo.PuestoPeaje;
import ort.dda.obl.modelo.Trabajadores;
import ort.dda.obl.modelo.Propietario;
import ort.dda.obl.dto.PropietarioDTO;

@Scope("session")
@RestController
@RequestMapping("/admin/bonificaciones")
public class ControladorAsignarBonificaciones implements Observador {

	private Propietario propietarioEnVista;
	private final ConexionNavegador conexionNavegador;

	public ControladorAsignarBonificaciones(@Autowired ConexionNavegador conexionNavegador) {
		this.conexionNavegador = conexionNavegador;
	}

	@PostMapping("/vistaConectada")
	public List<Respuesta> inicializarVista(@SessionAttribute(name = "usuarioAdmin") Object admin)
			throws UsuarioException {
		Fachada.getInstancia().agregarObservador(this);
		return Respuesta.lista();
	}

	@PostMapping("/vistaCerrada")
	public void vistaCerrada() {
		Fachada.getInstancia().quitarObservador(this);
	}

	// cargar bonificaciones disp.
	@PostMapping("/bonificaciones")
	public List<Respuesta> cargarBonificaciones(@SessionAttribute(name = "usuarioAdmin") Object admin)
			throws UsuarioException {

		return Respuesta.lista(new Respuesta("bonificaciones", 0)); // ToDo
	}

	@PostMapping("/puestos")
	public List<Respuesta> cargarPuestos(@SessionAttribute(name = "usuarioAdmin") Object admin)
			throws UsuarioException {
		List<PuestoPeaje> puestos = Fachada.getInstancia().getPuestosPeaje();
		return Respuesta.lista(new Respuesta("puestos", puestos));
	}

	@PostMapping("/buscar")
	public List<Respuesta> buscarPropietario(@SessionAttribute(name = "usuarioAdmin") Object admin,
			@RequestParam String cedula) throws UsuarioException {
		Propietario prop = Fachada.getInstancia().buscarPropXCedula(cedula);
		if (prop == null) {
			return Respuesta.lista(new Respuesta("error", "no existe el propietario"));
		}
		// Guardamos el propietario en la vista para las posibles notificaciones
		this.propietarioEnVista = prop;
		PropietarioDTO dto = new PropietarioDTO(prop);
		return Respuesta.lista(new Respuesta("propietario", dto));
	}

	@PostMapping("/asignar")
	public List<Respuesta> asignarBonificacion(@SessionAttribute(name = "usuarioAdmin") Object admin,
			@RequestParam String cedula,
			@RequestParam(required = false) String bonificacion,
			@RequestParam(required = false) String nombrePuesto) throws UsuarioException, SistemaTransitoException {

		// se debe especificar una bonificación
		if (bonificacion == null || bonificacion.trim().isEmpty()) {
			return Respuesta.lista(new Respuesta("error", "Debe especificar una bonificación"));
		}

		// Validación: Debe especificar un puesto
		if (nombrePuesto == null || nombrePuesto.trim().isEmpty()) {
			return Respuesta.lista(new Respuesta("error", "Debe especificar un puesto"));
		}

		// Buscar propietario
		Propietario prop = Fachada.getInstancia().buscarPropXCedula(cedula);
		if (prop == null) {
			return Respuesta.lista(new Respuesta("error", "No existe el propietario"));
		}

		// Buscar puesto
		PuestoPeaje puesto = Fachada.getInstancia().buscarPuestoPorNombre(nombrePuesto);
		if (puesto == null) {
			return Respuesta.lista(new Respuesta("error", "Puesto no encontrado"));
		}

		// Crear instancia de Bonificación según nombre
		Bonificacion b = crearBonificacionDesdeNombre(bonificacion);
		if (b == null) {
			return Respuesta.lista(new Respuesta("error", "Bonificación no válida"));
		}

		// Delegar a la Fachada (patrón Fachada + validaciones)
		Fachada.getInstancia().asignarBonificacionAPropietario(prop, b, puesto);

		// Actualizar propietario en vista
		this.propietarioEnVista = prop;

		// Retornar propietario actualizado
		PropietarioDTO dto = new PropietarioDTO(prop);
		return Respuesta.lista(
				new Respuesta("propietario", dto),
				new Respuesta("exito", "Bonificación asignada correctamente"));
	}

	private Bonificacion crearBonificacionDesdeNombre(String nombre) {
		if (nombre == null)
			return null;
		String n = nombre.trim();
		if (n.equalsIgnoreCase("Trabajadores"))
			return new Trabajadores();
		if (n.equalsIgnoreCase("Frecuentes"))
			return new Frecuentes();
		if (n.equalsIgnoreCase("Exonerados"))
			return new Exonerados();
		return null;
	}

	private Respuesta propDTO() {
		if (this.propietarioEnVista == null)
			return new Respuesta("propietario", null);
		return new Respuesta("propietario", new PropietarioDTO(this.propietarioEnVista));
	}

	// obs--> evento asignacion
	@Override
	public void actualizar(Object evento, Observable origen) {
		if (evento != null && evento.equals("asignacion")) {
			conexionNavegador.enviarJSON(Respuesta.lista(propDTO()));
		}
	}

}