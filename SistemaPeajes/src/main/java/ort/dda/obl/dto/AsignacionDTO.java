package ort.dda.obl.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import ort.dda.obl.modelo.Asignacion;

public class AsignacionDTO {
    private String bonificacion;
    private String puesto;
    private String fechaAsignada;

    public AsignacionDTO(Asignacion a) {
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaOriginal = a.getFechaAsignada();
        fechaAsignada = formatoFechaHora.format(fechaOriginal);
        bonificacion = a.getBonificacion().getNombre();
        puesto = a.getPuesto().getNombre();
    }

    public String getBonificacion() {
        return bonificacion;
    }

    public String getPuesto() {
        return puesto;
    }

    public String getFechaAsignada() {
        return fechaAsignada;
    }

}
