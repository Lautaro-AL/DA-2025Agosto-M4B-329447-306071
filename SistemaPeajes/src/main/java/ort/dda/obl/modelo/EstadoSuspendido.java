package ort.dda.obl.modelo;

public class EstadoSuspendido extends EstadoPropietario {

    public EstadoSuspendido(Propietario propietario) {
        super(propietario);
    }

    @Override
    public boolean puedeIngresar() {
        return true; // puede ingresar al sistema
    }

    @Override
    public boolean puedeTransitar() {
        return false; // no puede realizar tránsitos
    }

    @Override
    public boolean puedeRecibirBonificaciones() {
        return true; // permite asignaciones/bonificaciones
    }

    @Override
    public boolean recibeNotificaciones() {
        return true;
    }

    @Override
    public String getNombre() {
        return "Suspendido";
    }
}

