package ort.dda.obl.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import ort.dda.obl.modelo.Notificacion;

public class NotificacionDTO {
    private String fechaHora;
    private String mensaje;

    public NotificacionDTO(Notificacion n) {
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date fechaOriginal = n.getFechaHora();
        fechaHora = formatoFechaHora.format(fechaOriginal);
        mensaje = n.getMensaje();
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getMensaje() {
        return mensaje;
    }

}
