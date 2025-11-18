package ort.dda.obl.modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Frecuentes extends Bonificacion {

    public Frecuentes() {
        super("Frecuentes");
    }

    @Override
    public double aplicarDescuento(Transito transito, PuestoPeaje puestoAsignado, Propietario propietario) {
        // Verificar que el tránsito sea por el puesto asignado
        if (!transito.getPuestoPeaje().getNombre().equals(puestoAsignado.getNombre())) {
            return transito.getMonto(); // Sin descuento si no es el puesto asignado
        }

        // Fecha del tránsito que se está evaluando  
        // -->habia que usar el localdate aca a pesar de nunca haberlo dado para tener la hora exacta de esta zona

        Date fecha = transito.getFecha();
        LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Contar cuántos tránsitos con el mismo vehículo y puesto son del mismo día
        int transitosHoy = 0;
        if (propietario.getTransitos() != null) {
            for (Transito tr : propietario.getTransitos()) {
                // Comparar por matrícula para evitar depender de la identidad de objeto
                if (tr.getVehiculo() != null && transito.getVehiculo() != null
                        && tr.getVehiculo().getMatricula().equalsIgnoreCase(transito.getVehiculo().getMatricula())
                        && tr.getPuestoPeaje() != null
                        && tr.getPuestoPeaje().getNombre().equals(puestoAsignado.getNombre())) {

                    LocalDate fechaTr = tr.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (fechaLocal.equals(fechaTr)) {
                        transitosHoy++;
                    }
                }
            }
        }

        // Si ya hubo al menos un tránsito hoy → este sería el segundo tránsito o más
        // Aplicar 50% de descuento
        if (transitosHoy >= 1) {
            return transito.getMonto() * 0.5;
        }

        // si es el primer tránsito del día → no hay descuento
        return transito.getMonto();
    }

}
