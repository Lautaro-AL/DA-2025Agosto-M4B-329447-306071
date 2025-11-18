package ort.dda.obl.modelo;

public class EstadoHabilitado extends EstadoPropietario {

    public EstadoHabilitado(Propietario propietario) {
        super(propietario);
    }

    @Override
    public boolean puedeIngresar() {
        return true;
    }

    @Override
    public boolean puedeTransitar() {
        return true;
    }

    @Override
    public boolean puedeRecibirBonificaciones() {
        return true;
    }

    @Override
    public boolean recibeNotificaciones() {
        return true;
    }

    @Override
    public String getNombre() {
        return "Habilitado";
    }
}

