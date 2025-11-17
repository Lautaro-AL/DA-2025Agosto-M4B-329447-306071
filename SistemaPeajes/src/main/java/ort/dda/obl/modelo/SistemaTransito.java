package ort.dda.obl.modelo;

import java.util.ArrayList;

import ort.dda.obl.SistemaTransitoException;

public class SistemaTransito {
    private ArrayList<PuestoPeaje> puestosPeaje = new ArrayList<PuestoPeaje>();
    private ArrayList<Bonificacion> bonificaciones = new ArrayList<Bonificacion>();
    private ArrayList<Transito> transitos = new ArrayList<Transito>();

    public void agregarPuesto(String nombre, String direccion) {
        puestosPeaje.add(new PuestoPeaje(nombre, direccion));
    }

    public void emularTransito(Vehiculo vehiculo, PuestoPeaje puesto, Tarifa tarifa, Propietario propietario)
            throws SistemaTransitoException {
        if (!propietario.puedeTransitar()) {
            throw new SistemaTransitoException("El propietario no puede realizar tránsitos");
        }
        registrarTransito(vehiculo, puesto, tarifa, propietario);
        propietario.notificarTransito(vehiculo, puesto);
        propietario.notificarSaldoBajo();
    }

    public void registrarTransito(Vehiculo vehiculo, PuestoPeaje puesto, Tarifa tarifa, Propietario propietario)
            throws SistemaTransitoException {
        if (vehiculo == null || puesto == null || tarifa == null || propietario == null) {
            throw new SistemaTransitoException("Error - Campos nulos");
        }
        Transito transito = new Transito(vehiculo, puesto, tarifa);
        double montoFinal = propietario.calcularMontoFinal(transito);
        propietario.descontarSaldoActual(montoFinal);
        transito.setMonto(montoFinal);
        transitos.add(transito);
        propietario.agregarTransito(transito);
    }

    public ArrayList<PuestoPeaje> getPuestosPeaje() {
        return puestosPeaje;
    }

    public ArrayList<Transito> getTransitos() {
        return transitos;
    }

    public PuestoPeaje buscarPuestoPorNombre(String nombrePuesto) {

        if (nombrePuesto == null) {
            return null;
        }
        for (PuestoPeaje p : getPuestosPeaje()) {
            if (p.getNombre().equals(nombrePuesto)) {
                return p;
            }
        }
        return null;
    }

}
