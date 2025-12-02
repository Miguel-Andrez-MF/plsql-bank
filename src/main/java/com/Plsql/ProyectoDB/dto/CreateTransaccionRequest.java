package com.Plsql.ProyectoDB.dto;

import java.math.BigDecimal;

public class CreateTransaccionRequest {

    private String cuentaId;
    private BigDecimal monto;
    private Long tipoTransacId;

    // Getters and Setters
    public String getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(String cuentaId) {
        this.cuentaId = cuentaId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getTipoTransacId() {
        return tipoTransacId;
    }

    public void setTipoTransacId(Long tipoTransacId) {
        this.tipoTransacId = tipoTransacId;
    }
}
