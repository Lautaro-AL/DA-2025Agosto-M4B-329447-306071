package ort.dda.obl.dto;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import ort.dda.obl.modelo.Transito;

public class TransitoDTO {
    private String fecha;
    private String hora;
    private String vehiculoMat;
    private String puestoPeajeNombre;
    private String categoria;
    private double tarifaMonto;
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
