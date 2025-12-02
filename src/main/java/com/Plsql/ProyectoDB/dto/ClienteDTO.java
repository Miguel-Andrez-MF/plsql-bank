package com.Plsql.ProyectoDB.dto;

public class ClienteDTO {
    private String clienteId;
    private String nombre;
    private Long identificacion;
    private String direccion;
    private String telefono;
    private String email;
    private Long usuarioId;
    private Long totalCuentas;
    private java.math.BigDecimal saldoTotal;

    // Getters and Setters
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Long identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getTotalCuentas() {
        return totalCuentas;
    }

    public void setTotalCuentas(Long totalCuentas) {
        this.totalCuentas = totalCuentas;
    }

    public java.math.BigDecimal getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(java.math.BigDecimal saldoTotal) {
        this.saldoTotal = saldoTotal;
    }
}