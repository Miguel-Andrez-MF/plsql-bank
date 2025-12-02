package com.Plsql.ProyectoDB.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CuentaDTO {
    private String cuentaId;
    private String clienteId;
    private Long tipoCuentaId;
    private String tipoCuentaDescripcion;
    private Long estadoId;
    private String estadoDescripcion;
    private LocalDate fechaApertura;
    private BigDecimal saldo;

    // Getters and Setters
    public String getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(String cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public Long getTipoCuentaId() {
        return tipoCuentaId;
    }

    public void setTipoCuentaId(Long tipoCuentaId) {
        this.tipoCuentaId = tipoCuentaId;
    }

    public String getTipoCuentaDescripcion() {
        return tipoCuentaDescripcion;
    }

    public void setTipoCuentaDescripcion(String tipoCuentaDescripcion) {
        this.tipoCuentaDescripcion = tipoCuentaDescripcion;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}