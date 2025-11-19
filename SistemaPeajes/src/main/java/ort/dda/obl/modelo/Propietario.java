package ort.dda.obl.modelo;

import java.util.ArrayList;
import java.util.Date;
import ort.dda.obl.SistemaTransitoException;

public class Propietario extends Usuario {

    public enum Eventos {
        eliminarNotificaciones, emuloTransito
    }

    private double saldoActual;
    private int saldoAlerta;
    private ArrayList<Transito> transitos = new ArrayList<>();
    private ArrayList<Asignacion> asignaciones = new ArrayList<>();
    private ArrayList<Vehiculo> vehiculos = new ArrayList<>();
    private ArrayList<Notificacion> notificaciones = new ArrayList<>();
    private EstadoPropietario estado = new EstadoHabilitado(this);// por default esta habilitado el estado del
                                                                  // propietario

    public Propietario(String cedula, String password, String nombreCompleto, double saldoActual, int saldoAlerta,
            ArrayList<Transito> transitos, ArrayList<Vehiculo> vehiculos, ArrayList<Notificacion> notificaciones) {
        super(cedula, password, nombreCompleto);
        this.saldoActual = saldoActual;
        this.saldoAlerta = saldoAlerta;
        this.transitos = transitos;
        this.vehiculos = vehiculos;
        this.notificaciones = notificaciones;
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(ArrayList<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public ArrayList<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(ArrayList<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public ArrayList<Transito> getTransitos() {
        return transitos;
    }

    public void setTransitos(ArrayList<Transito> transitos) {
        this.transitos = transitos;
    }

    public int getSaldoAlerta() {
        return saldoAlerta;
    }

    public void setSaldoAlerta(int saldoAlerta) {
        this.saldoAlerta = saldoAlerta;
    }

    public ArrayList<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(ArrayList<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void agregarVehiculo(Vehiculo v) {
        if (v == null)
            return;
        if (vehiculos == null) {
            vehiculos = new ArrayList<>();
        }
        if (!vehiculos.contains(v)) {
            vehiculos.add(v);
        }
        if (v.getPropietario() != this) {
            v.setPropietario(this);
        }
    }

    public int getCantidadTransitos() {
        return transitos.size();
    }

    public void borrarNotificaciones() {
        if (this.notificaciones != null) {
            this.notificaciones.clear();
            avisar(Eventos.eliminarNotificaciones);
        } else {
            this.notificaciones = new ArrayList<>();
        }
    }

    protected void cambiarEstado(EstadoPropietario ep) {
        estado = ep;
    }

    public EstadoPropietario getEstado() {
        return estado;
    }

    public boolean puedeIngresar() {

        return estado != null && estado.puedeIngresar();
    }

    public boolean puedeTransitar() {
        return estado != null && estado.puedeTransitar();
    }

    public boolean puedeRecibirBonificaciones() {
        return estado != null && estado.puedeRecibirBonificaciones();
    }

    public boolean recibeNotificaciones() {
        return estado != null && estado.recibeNotificaciones();
    }

    public double calcularMontoFinal(Transito transito) {
        double montoBase = transito.getTarifa().getMonto();
        double montoFinal = montoBase;

        if (asignaciones != null) {
            for (Asignacion a : asignaciones) {
                double pago = a.calcularDescuento(transito, this);
                if (pago >= 0 && pago < montoFinal) {
                    montoFinal = pago;
                }
            }
        }
        return montoFinal;
    }

    public void descontarSaldoActual(double monto) throws SistemaTransitoException {
        if (saldoActual < monto) {
            throw new SistemaTransitoException("Saldo Insuficiente,   Saldo Actual : " + saldoActual + "$");
        }
        saldoActual = saldoActual - monto;
    }

    public void agregarTransito(Transito t) {
        if (transitos == null) {
            transitos = new ArrayList<>();
        }
        transitos.add(t);
        avisar(Eventos.emuloTransito);
    }

    public void notificarTransito(Vehiculo v, PuestoPeaje puesto) {
        if (!recibeNotificaciones())
            return;

        String msg = "Pasaste por el puesto " + puesto.getNombre() +
                " con el vehículo " + v.getMatricula();

        agregarNotificacion(msg);
    }

    public void notificarSaldoBajo() {
        if (!recibeNotificaciones())
            return;

        if (saldoActual < saldoAlerta) {
            String msg = "Tu saldo actual es $ " + saldoActual +
                    ". Te recomendamos hacer una recarga.";

            agregarNotificacion(msg);
        }
    }

    public void notificarEstado(String nuevoEstado) {
        String mensaje = "Se ha cambiado tu estado en el sistema. Tu estado actual es " + nuevoEstado;
        agregarNotificacion(mensaje);
    }

    public Asignacion getAsignacionParaPuesto(PuestoPeaje puesto) {
        for (Asignacion a : asignaciones) {
            if (a.getPuesto().equals(puesto)) {
                return a;
            }
        }
        return null;
    }

    public void agregarNotificacion(String mensaje) {
        if (notificaciones == null) {
            notificaciones = new ArrayList<>();
        }
        notificaciones.add(new Notificacion(mensaje));
    }

    public void agregarBonificacion(Bonificacion bonificacion, PuestoPeaje puesto) {
        if (this.asignaciones == null) {
            this.asignaciones = new ArrayList<>();
        }

        Asignacion nuevaAsignacion = new Asignacion(bonificacion, puesto, new Date());
        this.asignaciones.add(nuevaAsignacion);
    }

    public int getCantidadTransitosVehiculo(Vehiculo v) {
        int cantidad = 0;
        if (v != null && transitos != null) {
            for (Transito t : transitos) {
                if (t.getVehiculo().equals(v)) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    public double getMontoTotalVehiculo(Vehiculo v) {
        double total = 0;
        if (v != null && transitos != null) {
            for (Transito t : transitos) {
                if (t.getVehiculo().equals(v)) {
                    total += t.getMonto();
                }
            }
        }
        return total;
    }

}
