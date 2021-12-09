package com.example.miapp;

public class Terremoto {
    private Double magnitud;
    private String ciudad;
    private String fecha;

    public Terremoto(Double magnitud, String ciudad, String fecha) {
        this.magnitud = magnitud;
        this.ciudad = ciudad;
        this.fecha = fecha;
    }

    public Double getMagnitud() {
        return magnitud;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getFecha() {
        return fecha;
    }

}
