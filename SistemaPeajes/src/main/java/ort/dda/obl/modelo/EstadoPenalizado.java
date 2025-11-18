package ort.dda.obl.modelo;

public class EstadoPenalizado extends EstadoPropietario {

    public EstadoPenalizado(Propietario propietario) {
        super(propietario);
    }

    @Override
    public boolean puedeIngresar() {
        return true; // puede ingresar
    }

    @Override
    public boolean puedeTransitar() {
        return true; // puede transitar, pero no se aplican bonificaciones
    }

    @Override
    public boolean puedeRecibirBonificaciones() {
        return false; // bonificaciones no aplican
    }

    @Override
    public boolean recibeNotificaciones() {
        return false; // no se le registran notificaciones
    }
    @Override
    public String getNombre() {
        return "Penalizado";
    }
}

