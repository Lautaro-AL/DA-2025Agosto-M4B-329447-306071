package ort.dda.obl.modelo;

import java.util.ArrayList;

import ort.dda.obl.UsuarioException;

public class SistemaAcceso {
    private ArrayList<Propietario> propietarios = new ArrayList<>();
    private ArrayList<Administrador> administradores = new ArrayList();

    public void agregarUsuarioPropietario(String cedula, String password, String nombreCompleto, int saldoActual,
            int saldoAlerta) {
        propietarios.add(new Propietario(cedula, password, nombreCompleto, saldoActual, saldoAlerta,
                new ArrayList<Transito>(), new ArrayList<Vehiculo>(), new ArrayList<Notificacion>()));
    }

    public void agregarAdministrador(String cedula, String password, String nombreCompleto) {
        administradores.add(new Administrador(cedula, password, nombreCompleto));
    }

    public ArrayList<Propietario> getPropietarios() {
        return propietarios;
    }

    public void borrarNotificacionesPropietario(Propietario propietario) {
        propietario.borrarNotificaciones();
    }

    public Propietario loginPropetario(String cedula, String password) throws UsuarioException {
        Propietario user = (Propietario) Login(cedula, password, propietarios);
        if (user != null && !user.puedeIngresar()) {
            throw new UsuarioException("Usuario deshabilitado, no puede ingresar al sistema");
        }

        return user;
    }

    // get prop por cedula
    public Propietario getPropietarioPorCedula(String cedula) {
        for (Propietario p : propietarios) {
            if (p.getCedula().equals(cedula)) {
                return p;
            }
        }
        return null;
    }

    public Administrador loginAdministrador(String cedula, String password) throws UsuarioException {
        Administrador admin = (Administrador) Login(cedula, password, administradores);
        if (admin == null)
            throw new UsuarioException("Login incorrecto");
        return admin;
    }

    private Usuario Login(String cedula, String password, ArrayList lista) throws UsuarioException {
        Usuario usuario;
        for (Object u : lista) {
            usuario = (Usuario) u;
            if (usuario.getCedula().equals(cedula) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }

        throw new UsuarioException("Usuario o contraseña incorrectos");
    }

    public void cambiarEstado(Propietario prop, String nuevoEstado) {
        if (prop == null || nuevoEstado == null) {
            return;
        }

        EstadoPropietario nuevoEstadoObj = null;

        switch (nuevoEstado.toUpperCase()) {
            case "HABILITADO":
                nuevoEstadoObj = new EstadoHabilitado(prop);
                break;
            case "DESHABILITADO":
                nuevoEstadoObj = new EstadoDeshabilitado(prop);
                break;
            case "SUSPENDIDO":
                nuevoEstadoObj = new EstadoSuspendido(prop);
                break;
            case "PENALIZADO":
                nuevoEstadoObj = new EstadoPenalizado(prop);
                break;
            default:
                return;
        }

        if (nuevoEstadoObj != null) {
            prop.cambiarEstado(nuevoEstadoObj);
            prop.notificarEstado(nuevoEstadoObj.getNombre());
        }

    }

    public Asignacion obtenerAsignacionPuesto(Propietario prop, PuestoPeaje puesto) {
        return prop.getAsignacionParaPuesto(puesto);
    }

}
