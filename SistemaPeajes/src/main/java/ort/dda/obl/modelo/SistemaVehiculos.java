package ort.dda.obl.modelo;

import java.util.ArrayList;

public class SistemaVehiculos {

    private ArrayList<Vehiculo> vehiculos = new ArrayList<Vehiculo>();
    private ArrayList<Categoria> categorias = new ArrayList<Categoria>();
    private ArrayList<Tarifa> tarifas = new ArrayList<Tarifa>();
    private ArrayList<Bonificacion> bonificaciones = new ArrayList<Bonificacion>();

    public void agregarCategoria(String tipo) {
        categorias.add(new Categoria(tipo));
    }

    public void agregarTarifa(double monto, Categoria categoria) {
        tarifas.add(new Tarifa(monto, categoria));
    }

    public Vehiculo crearVehiculo(String matricula, String modelo, String color, Categoria categoria) {
        Vehiculo v = new Vehiculo(matricula, modelo, color, categoria, null);
        vehiculos.add(v);
        return v;
    }

    public void vincularVehiculoAPropietario(Vehiculo v, Propietario propietario) {
        if (v == null || propietario == null)
            return;

        propietario.agregarVehiculo(v);
        v.setPropietario(propietario);
    }

    public void agregarVehiculo(String matricula, String modelo, String color, Categoria categoria,
            Propietario propietario) {
        Vehiculo v = crearVehiculo(matricula, modelo, color, categoria);
        if (propietario != null) {
            vincularVehiculoAPropietario(v, propietario);
        }
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public ArrayList<Tarifa> getTarifas() {
        return tarifas;
    }

    public Tarifa buscarTarifaPorMontoYCategoria(double monto, String categoriaVehiculo) {
        if (categoriaVehiculo == null) {
            return null;
        }
        for (Tarifa t : getTarifas()) {
            if (t.getMonto() == monto && t.getCategoria().getTipo().equals(categoriaVehiculo)) {
                return t;
            }
        }
        return null;
    }

    public Tarifa buscarTarifaPorCategoria(String categoriaVehiculo) {
        if (categoriaVehiculo == null) {
            return null;
        }
        for (Tarifa t : getTarifas()) {
            if (t.getCategoria() != null && t.getCategoria().getTipo().equals(categoriaVehiculo)) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<Tarifa> obtenerTarifasPorPuesto(PuestoPeaje puesto) {
        ArrayList<Tarifa> resultado = new ArrayList<>();
        for (Tarifa t : getTarifas()) {
            resultado.add(t);
        }
        return resultado;
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) {
        for (Vehiculo v : getVehiculos()) {
            if (v.getMatricula().equalsIgnoreCase(matricula)) {
                return v;
            }
        }
        return null;
    }

    public Tarifa buscarTarifaPorPuestoYCategoria(PuestoPeaje puesto, Categoria categoria) {
        for (Tarifa t : tarifas) {
            if (t.getCategoria().equals(categoria)) {
                return t;
            }
        }
        return null;
    }
}
