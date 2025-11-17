package ort.dda.obl.modelo;

import java.util.Date;

public class Transito {

    private Date fecha = new Date();
    private Vehiculo vehiculo;
    private PuestoPeaje puestoPeaje;
    private Tarifa tarifa;
    private double monto;

    public Transito(Vehiculo vehiculo, PuestoPeaje puestoPeaje, Tarifa tarifa) {
        this.vehiculo = vehiculo;
        this.puestoPeaje = puestoPeaje;
        this.tarifa = tarifa;
        this.monto = tarifa.getMonto();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public PuestoPeaje getPuestoPeaje() {
        return puestoPeaje;
    }

    public void setPuestoPeaje(PuestoPeaje puestoPeaje) {
        this.puestoPeaje = puestoPeaje;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

}
