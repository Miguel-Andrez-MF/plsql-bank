package com.Plsql.ProyectoDB.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuditoriaTransaccionDTO {
    private Long auditoriaId;
    private Long transaccionId;
    private Long usuarioId;
    private LocalDateTime fechaOperacion;
    private BigDecimal montoAnterior;
    private BigDecimal montoNuevo;
    private Long tipoTransaccionAnterior;
    private Long tipoTransaccionNuevo;
    private String operacion;

    // Getters and Setters
    public Long getAuditoriaId() {
        return auditoriaId;
    }

    public void setAuditoriaId(Long auditoriaId) {
        this.auditoriaId = auditoriaId;
    }

    public Long getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Long transaccionId) {
        this.transaccionId = transaccionId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(LocalDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public BigDecimal getMontoAnterior() {
        return montoAnterior;
    }

    public void setMontoAnterior(BigDecimal montoAnterior) {
        this.montoAnterior = montoAnterior;
    }

    public BigDecimal getMontoNuevo() {
        return montoNuevo;
    }

    public void setMontoNuevo(BigDecimal montoNuevo) {
        this.montoNuevo = montoNuevo;
    }

    public Long getTipoTransaccionAnterior() {
        return tipoTransaccionAnterior;
    }

    public void setTipoTransaccionAnterior(Long tipoTransaccionAnterior) {
        this.tipoTransaccionAnterior = tipoTransaccionAnterior;
    }

    public Long getTipoTransaccionNuevo() {
        return tipoTransaccionNuevo;
    }

    public void setTipoTransaccionNuevo(Long tipoTransaccionNuevo) {
        this.tipoTransaccionNuevo = tipoTransaccionNuevo;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
}
