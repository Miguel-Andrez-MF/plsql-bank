package com.Plsql.ProyectoDB.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionDTO {
    private Long transaccionId;
    private String cuentaId;
    private Long tipoTransacId;
    private BigDecimal monto;
    private LocalDateTime fechaTransac;

    // Getters and Setters
    public Long getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Long transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(String cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Long getTipoTransacId() {
        return tipoTransacId;
    }

    public void setTipoTransacId(Long tipoTransacId) {
        this.tipoTransacId = tipoTransacId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaTransac() {
        return fechaTransac;
    }

    public void setFechaTransac(LocalDateTime fechaTransac) {
        this.fechaTransac = fechaTransac;
    }
}
