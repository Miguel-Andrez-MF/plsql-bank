package com.Plsql.ProyectoDB.dto;

import java.math.BigDecimal;

public class RetiroRequest {
    private String cuentaId;
    private BigDecimal monto;

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
}