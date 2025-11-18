package ort.dda.obl.modelo;

public class Bonificacion {
    private String nombre;
    private TipoBonificacion tipo;

    public Bonificacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double aplicarDescuento(Transito transito, PuestoPeaje puestoAsignado, Propietario propietario) {
        return tipo.aplicarDescuento(transito, puestoAsignado, propietario);
    }; // delega a tipoBonificacion

    public void cambiarTipo(TipoBonificacion tb) {
        this.tipo = tb;
    }
}
