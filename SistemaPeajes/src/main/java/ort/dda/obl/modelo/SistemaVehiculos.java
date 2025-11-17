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

    public void agregarTarifas(double monto, Categoria categoria) {
        tarifas.add(new Tarifa(monto, categoria));
    }

    public void agregarVehiculo(String matricula, String modelo, String color, Categoria categoria,
            Propietario propietario) {
        // Creamos el vehículo sin propietario y lo agregamos a la lista global.
        // Si se pasa un propietario, delegamos en Propietario.agregarVehiculo(v)
        // para que realice la asignación y validaciones.
        Vehiculo v = new Vehiculo(matricula, modelo, color, categoria, null);
        vehiculos.add(v);
        if (propietario != null) {
            try {
                // Propietario.agregarVehiculo establecerá el propietario en el vehículo
                // y lo agregará a la lista de vehículos del propietario.
                propietario.agregarVehiculo(v);
            } catch (Exception e) {
                // Si por alguna razón falla (validación), hacemos un fallback seguro:
                // asignamos el propietario y lo añadimos a su lista directamente.
                v.setPropietario(propietario);
                if (propietario.getVehiculos() == null) {
                    propietario.setVehiculos(new java.util.ArrayList<Vehiculo>());
                }
                propietario.getVehiculos().add(v);
            }
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

    //GET BONIFICACIONES POR NOMBRE
    public Bonificacion buscarBonificacionPorNombre(String nombre){
        for (Bonificacion b : bonificaciones) {
            if (b.getNombre().equalsIgnoreCase(nombre)) {
                return b;
            }
        }
        return null;
    }
}
