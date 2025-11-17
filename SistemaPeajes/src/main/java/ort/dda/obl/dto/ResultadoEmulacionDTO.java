package ort.dda.obl.dto;

public class ResultadoEmulacionDTO {
    private String nombrePropietario;
    private String estadoPropietario;
    private String categoriaVehiculo;
    private String nombreBonificacion;
    private double costoTransito;
    private double saldoFinal;

    public ResultadoEmulacionDTO(String nombrePropietario, String estadoPropietario, String categoriaVehiculo,
            String nombreBonificacion, double costoTransito, double saldoFinal) {
        this.nombrePropietario = nombrePropietario;
        this.estadoPropietario = estadoPropietario;
        this.categoriaVehiculo = categoriaVehiculo;
        this.nombreBonificacion = nombreBonificacion;
        this.costoTransito = costoTransito;
        this.saldoFinal = saldoFinal;
    }

    public ResultadoEmulacionDTO(ResultadoEmulacionDTO res) {

    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public String getEstadoPropietario() {
        return estadoPropietario;
    }

    public String getCategoriaVehiculo() {
        return categoriaVehiculo;
    }

    public String getNombreBonificacion() {
        return nombreBonificacion;
    }

    public double getCostoTransito() {
        return costoTransito;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }
}
