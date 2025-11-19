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

        // Tránsitos -> orden descendente por fecha/hora (sin usar Collections.sort)
        if (prop.getTransitos() != null) {
            ArrayList<Transito> ordenados = new ArrayList<>();
            for (Transito t : prop.getTransitos()) {
                // encontrar posición para insertar manteniendo orden descendente (fecha más reciente primero)
                int pos = 0;
                while (pos < ordenados.size() && ordenados.get(pos).getFecha().after(t.getFecha())) {
                    pos++;
                }
                ordenados.add(pos, t);
            }

            for (Transito t : ordenados) {
                this.transitos.add(new TransitoDTO(t, prop));
            }
        }

        // Asignaciones
        if (prop.getAsignaciones() != null) {
            for (Asignacion a : prop.getAsignaciones()) {
                this.asignaciones.add(new AsignacionDTO(a));
            }
        }

        // Notificaciones -> orden descendente por fecha/hora (sin Collections.sort)
        if (prop.getNotificaciones() != null) {
            ArrayList<Notificacion> ordenNotif = new ArrayList<>();
            for (Notificacion n : prop.getNotificaciones()) {
                int pos = 0;
                while (pos < ordenNotif.size() && ordenNotif.get(pos).getFechaHora().after(n.getFechaHora())) {
                    pos++;
                }
                ordenNotif.add(pos, n);
            }
            for (Notificacion n : ordenNotif) {
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