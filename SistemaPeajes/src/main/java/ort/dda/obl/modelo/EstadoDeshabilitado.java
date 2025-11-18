package ort.dda.obl.modelo;

public class EstadoDeshabilitado extends EstadoPropietario {

    public EstadoDeshabilitado(Propietario propietario) {
        super(propietario);
    }

    @Override
    public boolean puedeIngresar() {
        return false;
    }

    @Override
    public boolean puedeTransitar() {
        return false;
    }

    @Override
    public boolean puedeRecibirBonificaciones() {
        return false;
    }

    @Override
    public boolean recibeNotificaciones() {
        return false;
    }

    @Override
    public String getNombre() {
        return "Deshabilitado";
    }
}
