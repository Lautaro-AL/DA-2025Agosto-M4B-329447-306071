package ort.dda.obl.dto;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import ort.dda.obl.modelo.Transito;
import ort.dda.obl.modelo.Propietario;
import ort.dda.obl.modelo.Asignacion;

public class TransitoDTO {
    private String fecha;
    private String hora;
    private String vehiculoMat;
    private String puestoPeajeNombre;
    private String categoria;
    private double tarifaMonto;
    private String tarifaNombre;
    private String bonificacionNombre;
    private double bonificacionMonto;
    private double montoPagado;
    private double monto;

    public TransitoDTO(Transito t) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
        Date fechaOriginal = t.getFecha();
        this.fecha = formatoFecha.format(fechaOriginal);
        this.hora = formatoHora.format(fechaOriginal);
        this.monto = t.getMonto();
        this.vehiculoMat = t.getVehiculo().getMatricula();
        this.puestoPeajeNombre = t.getPuestoPeaje().getNombre();
        this.categoria = t.getVehiculo().getCategoria().getTipo();
        this.tarifaMonto = t.getTarifa().getMonto();
        this.tarifaNombre = t.getTarifa().getCategoria().getTipo();
        this.bonificacionNombre = null;
        this.bonificacionMonto = 0.0;
        this.montoPagado = this.monto;
    }

    public TransitoDTO(Transito t, Propietario prop) {
        this(t);
        if (prop != null) {
            double finalMonto = prop.calcularMontoFinal(t);
            this.montoPagado = finalMonto;

            if (prop.getAsignaciones() != null) {
                for (Asignacion a : prop.getAsignaciones()) {
                    double pago = a.calcularDescuento(t, prop);
                    if (pago >= 0 && pago < t.getMonto() && Math.abs(pago - finalMonto) < 0.0001) {
                        this.bonificacionNombre = a.getBonificacion().getNombre();
                        this.bonificacionMonto = t.getMonto() - finalMonto;
                        break;
                    }
                }
            }
        }
    }

    public String getVehiculoMat() {
        return vehiculoMat;
    }

    public String getPuestoPeajeNombre() {
        return puestoPeajeNombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getTarifaMonto() {
        return tarifaMonto;
    }

    public String getTarifaNombre() {
        return tarifaNombre;
    }

    public String getBonificacionNombre() {
        return bonificacionNombre;
    }

    public double getBonificacionMonto() {
        return bonificacionMonto;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public double getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
}
