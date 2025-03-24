package com.example.appqrsalones.modelo;
public class Salon {
    private String salon_numero;
    private String salon_sede;
    private String codigo_qr;
    public Salon() {
    }
    public Salon(String salon_numero, String salon_sede, String codigo_qr) {
        this.salon_numero = salon_numero;
        this.salon_sede = salon_sede;
        this.codigo_qr = codigo_qr;
    }
    public String getSalon_numero() {
        return salon_numero;
    }
    public void setSalon_numero(String salon_numero) {
        this.salon_numero = salon_numero;
    }
    public String getSalon_sede() {
        return salon_sede;
    }
    public void setSalon_sede(String salon_sede) {
        this.salon_sede = salon_sede;
    }
    public String getCodigo_qr() {
        return codigo_qr;
    }
    public void setCodigo_qr(String codigo_qr) {
        this.codigo_qr = codigo_qr;
    }
}
