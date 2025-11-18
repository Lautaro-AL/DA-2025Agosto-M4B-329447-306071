package ort.dda.obl.modelo;
import java.util.Date;

public class Asignacion {
    private Bonificacion bonificacion;    // Ej: BonificacionFrecuente
    private PuestoPeaje puesto;           // Puesto al que aplica
    private Date fechaAsignada;  // Fecha de asignación

    public Asignacion(Bonificacion bonificacion, PuestoPeaje puesto, Date fechaAsignada) {
        this.bonificacion = bonificacion;
        this.puesto = puesto;
        this.fechaAsignada = fechaAsignada ;
    }

    public Bonificacion getBonificacion() {
        return bonificacion;
    }

    public PuestoPeaje getPuesto() {
        return puesto;
    }

    public Date getFechaAsignada() {
        return fechaAsignada;
    }

   
    public double calcularDescuento(Transito t, Propietario propietario) {
        return bonificacion.aplicarDescuento(t, puesto, propietario);
    }
}
