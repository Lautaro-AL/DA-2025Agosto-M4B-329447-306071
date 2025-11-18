package ort.dda.obl.modelo;

public abstract class EstadoPropietario {
    private Propietario propietario;

    public enum Estados {
        habilitado, deshabilitado, suspendido, penalizado
    }

    public EstadoPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    // Comportamientos que varían según el estado
    public abstract boolean puedeIngresar();

    public abstract boolean puedeTransitar();

    public abstract boolean puedeRecibirBonificaciones();

    public abstract boolean recibeNotificaciones();

    public abstract String getNombre();

}
