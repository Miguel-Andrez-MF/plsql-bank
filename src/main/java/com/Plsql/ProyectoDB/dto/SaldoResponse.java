package com.Plsql.ProyectoDB.dto;

import java.math.BigDecimal;

public class SaldoResponse {
    private String cuentaId;
    private BigDecimal saldo;

    public SaldoResponse() {
    }

    public SaldoResponse(String cuentaId, BigDecimal saldo) {
        this.cuentaId = cuentaId;
        this.saldo = saldo;
    }

    public String getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(String cuentaId) {
        this.cuentaId = cuentaId;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}