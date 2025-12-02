package com.Plsql.ProyectoDB.dto;

import java.math.BigDecimal;

public class UpdateTransaccionRequest {

    private BigDecimal monto;
    private Long tipoTransaccionId;

    // Getters and Setters
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getTipoTransaccionId() {
        return tipoTransaccionId;
    }

    public void setTipoTransaccionId(Long tipoTransaccionId) {
        this.tipoTransaccionId = tipoTransaccionId;
    }
}
