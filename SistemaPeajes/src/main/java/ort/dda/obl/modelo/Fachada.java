package ort.dda.obl.modelo;

import java.util.ArrayList;

import observador.Observable;
import ort.dda.obl.UsuarioException;
import ort.dda.obl.SistemaTransitoException;

public class Fachada extends Observable {
    private SistemaAcceso sAcceso = new SistemaAcceso();
    private SistemaTransito sTransito = new SistemaTransito();
    private SistemaVehiculos sVehiculos = new SistemaVehiculos();

    // SINGLETON
    private static Fachada instancia = new Fachada();

    public static Fachada getInstancia() {
        return instancia;
    }

    private Fachada() {
    }

    // DELEGACIONES SISTEMA ACCESO
    public void agregarUsuarioPropietario(String cedula, String pwd, String nombreCompleto, int saldoActual,
            int saldoAlerta) {
        sAcceso.agregarUsuarioPropietario(cedula, pwd, nombreCompleto, saldoActual, saldoAlerta);
    }

    public ArrayList<Propietario> obtenerPropietarios() {
        return sAcceso.getPropietarios();
    }

    public Propietario loginPropetario(String cedula, String password) throws UsuarioException {
        return sAcceso.loginPropetario(cedula, password);
    }

    public void agregarAdministrador(String cedula, String password, String nombreCompleto) {
        sAcceso.agregarAdministrador(cedula, password, nombreCompleto);
    }

    public Administrador loginAdministrador(String cedula, String password) throws UsuarioException {
        return sAcceso.loginAdministrador(cedula, password);
    }

    public void borrarNotificacionesPropietario(Propietario propietario) {
        sAcceso.borrarNotificacionesPropietario(propietario);
    }

    // get propietarios x cedula
    public Propietario buscarPropXCedula(String cedula) {
        return sAcceso.getPropietarioPorCedula(cedula);
    }

    // get bonificaciones por nombre
    // public Bonificacion buscarBonificacionXNombre(String nombre){
    // return sVehiculos.buscarBonificacionXNombre(nombre);
    // }

    // DELEGACIONES SISTEMA TRANSITO
    public void agregarPuesto(String nombre, String direccion) {
        sTransito.agregarPuesto(nombre, direccion);
    }

    public ArrayList<PuestoPeaje> getPuestosPeaje() {
        return sTransito.getPuestosPeaje();
    }

    public void registrarTransito(Vehiculo vehiculo, PuestoPeaje puesto, Tarifa tarifa, Propietario propietario)
            throws SistemaTransitoException {
        sTransito.registrarTransito(vehiculo, puesto, tarifa, propietario);
    }

    public PuestoPeaje buscarPuestoPorNombre(String nombrePuesto) {
        return sTransito.buscarPuestoPorNombre(nombrePuesto);
    }

    public void emularTransito(Vehiculo vehiculo, PuestoPeaje puesto, Tarifa tarifa, Propietario propietario)
            throws SistemaTransitoException {
        sTransito.emularTransito(vehiculo, puesto, tarifa, propietario);
        avisar(Propietario.Eventos.emuloTransito);
    }

    // DELEGACIONES SISTEMA VEHICULOS
    public void agregarCategoria(String tipo) {
        sVehiculos.agregarCategoria(tipo);
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return sVehiculos.getVehiculos();
    }

    public void agregarTarifa(double monto, Categoria categoria) {
        sVehiculos.agregarTarifas(monto, categoria);
    }

    public void agregarVehiculo(String matricula, String modelo, String color, Categoria categoria,
            Propietario propietario) {
        sVehiculos.agregarVehiculo(matricula, modelo, color, categoria, propietario);
    }

    public ArrayList<Categoria> getCategorias() {
        return sVehiculos.getCategorias();
    }

    public ArrayList<Tarifa> getTarifas() {
        return sVehiculos.getTarifas();
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) {
        return sVehiculos.buscarVehiculoPorMatricula(matricula);
    }

    public ArrayList<Tarifa> obtenerTarifasPorPuesto(PuestoPeaje puesto) {
        return sVehiculos.obtenerTarifasPorPuesto(puesto);
    }

    public Tarifa buscarTarifaPorMontoYCategoria(double monto, String categoriaVehiculo) {
        return sVehiculos.buscarTarifaPorMontoYCategoria(monto, categoriaVehiculo);
    }

}
