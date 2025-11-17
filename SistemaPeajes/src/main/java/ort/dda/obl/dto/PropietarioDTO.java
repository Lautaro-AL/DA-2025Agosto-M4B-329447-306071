package ort.dda.obl.dto;

import java.util.ArrayList;

import ort.dda.obl.modelo.Asignacion;
import ort.dda.obl.modelo.Notificacion;
import ort.dda.obl.modelo.Propietario;
import ort.dda.obl.modelo.Transito;
import ort.dda.obl.modelo.Vehiculo;

public class PropietarioDTO {
    private String nombreCompleto;
    private String cedula;
    private double saldoActual;
    private int saldoAlerta;
    private String estado;
    private ArrayList<VehiculoDTO> vehiculos = new ArrayList<>();
    private ArrayList<TransitoDTO> transitos = new ArrayList<>();
    private ArrayList<AsignacionDTO> asignaciones = new ArrayList<>();
    private ArrayList<NotificacionDTO> notificaciones = new ArrayList<>();

    public PropietarioDTO(Propietario prop) {
        this.nombreCompleto = prop.getNombreCompleto();
        this.cedula = prop.getCedula();
        this.saldoActual = prop.getSaldoActual();
        this.saldoAlerta = prop.getSaldoAlerta();
        this.estado = prop.getEstado().getNombre();

        if (prop.getVehiculos() != null) {
            for (Vehiculo v : prop.getVehiculos()) {
                this.vehiculos.add(new VehiculoDTO(v));
            }
        }

        // Tránsitos
        if (prop.getTransitos() != null) {

            for (Transito t : prop.getTransitos()) {
                this.transitos.add(new TransitoDTO(t));
            }
        }

        // Asignaciones
        if (prop.getAsignaciones() != null) {
            for (Asignacion a : prop.getAsignaciones()) {
                this.asignaciones.add(new AsignacionDTO(a));
            }
        }

        // Notificaciones
        if (prop.getNotificaciones() != null) {
            for (Notificacion n : prop.getNotificaciones()) {
                this.notificaciones.add(new NotificacionDTO(n));
            }
        }

    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getCedula() {
        return cedula;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public int getSaldoAlerta() {
        return saldoAlerta;
    }

    public String getEstado() {
        return estado;
    }

    public ArrayList<VehiculoDTO> getVehiculos() {
        return vehiculos;
    }

    public ArrayList<TransitoDTO> getTransitos() {
        return transitos;
    }

    public ArrayList<AsignacionDTO> getAsignaciones() {
        return asignaciones;
    }

    public ArrayList<NotificacionDTO> getNotificaciones() {
        return notificaciones;
    }
}