package com.Plsql.ProyectoDB.services;

import com.Plsql.ProyectoDB.dto.CuentaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class CuentaService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CuentaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Activar cuenta
    public void activarCuenta(String cuentaId) {
        String plsql = "BEGIN PROYECTODB.PKG_GESTION_CUENTAS.ACTIVAR_CUENTA(?); END;";

        try {
            jdbcTemplate.execute(plsql, (CallableStatementCallback<Void>) cs -> {
                cs.setString(1, cuentaId);
                cs.execute();
                return null;
            });
        } catch (DataAccessException e) {
            throw new CuentaOperationException("Error al activar cuenta: " + e.getMostSpecificCause().getMessage());
        }
    }

    // Desactivar cuenta
    public void deshabilitarCuenta(String cuentaId) {
        String plsql = "BEGIN PROYECTODB.PKG_GESTION_CUENTAS.DESHABILITAR_CUENTA(?); END;";

        try {
            jdbcTemplate.execute(plsql, (CallableStatementCallback<Void>) cs -> {
                cs.setString(1, cuentaId);
                cs.execute();
                return null;
            });
        } catch (DataAccessException e) {
            throw new CuentaOperationException("Error al desactivar cuenta: " + e.getMostSpecificCause().getMessage());
        }
    }

    // Bloquear cuenta
    public void bloquearCuenta(String cuentaId) {
        String plsql = "BEGIN PROYECTODB.PKG_GESTION_CUENTAS.BLOQUEAR_CUENTA(?); END;";

        try {
            jdbcTemplate.execute(plsql, (CallableStatementCallback<Void>) cs -> {
                cs.setString(1, cuentaId);
                cs.execute();
                return null;
            });
        } catch (DataAccessException e) {
            throw new CuentaOperationException("Error al bloquear cuenta: " + e.getMostSpecificCause().getMessage());
        }
    }

    // Listar todas las cuentas
    public List<CuentaDTO> getAllCuentas() {
        String sql =
                "SELECT " +
                        "  C.CUENTA_ID, " +
                        "  C.CLIENTE_ID, " +
                        "  C.TIPO_CUENTA_ID, " +
                        "  TC.DESCRIPCION AS TIPO_CUENTA_DESC, " +
                        "  C.ESTADO_ID, " +
                        "  E.DESCRIPCION AS ESTADO_DESC, " +
                        "  C.FECHA_APERTURA, " +
                        "  C.SALDO " +
                        "FROM PROYECTODB.TBL_CUENTAS C " +
                        "JOIN PROYECTODB.TBL_TIPOS_PARAMETROS TC ON C.TIPO_CUENTA_ID = TC.TIPO_PARAMETRO_ID " +
                        "JOIN PROYECTODB.TBL_TIPOS_PARAMETROS E ON C.ESTADO_ID = E.TIPO_PARAMETRO_ID " +
                        "ORDER BY C.FECHA_APERTURA DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CuentaDTO cuenta = new CuentaDTO();
            cuenta.setCuentaId(rs.getString("CUENTA_ID"));
            cuenta.setClienteId(rs.getString("CLIENTE_ID"));
            cuenta.setTipoCuentaId(rs.getLong("TIPO_CUENTA_ID"));
            cuenta.setTipoCuentaDescripcion(rs.getString("TIPO_CUENTA_DESC"));
            cuenta.setEstadoId(rs.getLong("ESTADO_ID"));
            cuenta.setEstadoDescripcion(rs.getString("ESTADO_DESC"));

            Date fechaApertura = rs.getDate("FECHA_APERTURA");
            if (fechaApertura != null) {
                cuenta.setFechaApertura(fechaApertura.toLocalDate());
            }

            cuenta.setSaldo(rs.getBigDecimal("SALDO"));
            return cuenta;
        });
    }

    // Obtener cuentas por cliente
    public List<CuentaDTO> getCuentasByCliente(String clienteId) {
        String sql =
                "SELECT " +
                        "  C.CUENTA_ID, " +
                        "  C.CLIENTE_ID, " +
                        "  C.TIPO_CUENTA_ID, " +
                        "  TC.DESCRIPCION AS TIPO_CUENTA_DESC, " +
                        "  C.ESTADO_ID, " +
                        "  E.DESCRIPCION AS ESTADO_DESC, " +
                        "  C.FECHA_APERTURA, " +
                        "  C.SALDO " +
                        "FROM PROYECTODB.TBL_CUENTAS C " +
                        "JOIN PROYECTODB.TBL_TIPOS_PARAMETROS TC ON C.TIPO_CUENTA_ID = TC.TIPO_PARAMETRO_ID " +
                        "JOIN PROYECTODB.TBL_TIPOS_PARAMETROS E ON C.ESTADO_ID = E.TIPO_PARAMETRO_ID " +
                        "WHERE C.CLIENTE_ID = ? " +
                        "ORDER BY C.FECHA_APERTURA DESC";

        return jdbcTemplate.query(sql, new Object[]{clienteId}, (rs, rowNum) -> {
            CuentaDTO cuenta = new CuentaDTO();
            cuenta.setCuentaId(rs.getString("CUENTA_ID"));
            cuenta.setClienteId(rs.getString("CLIENTE_ID"));
            cuenta.setTipoCuentaId(rs.getLong("TIPO_CUENTA_ID"));
            cuenta.setTipoCuentaDescripcion(rs.getString("TIPO_CUENTA_DESC"));
            cuenta.setEstadoId(rs.getLong("ESTADO_ID"));
            cuenta.setEstadoDescripcion(rs.getString("ESTADO_DESC"));

            Date fechaApertura = rs.getDate("FECHA_APERTURA");
            if (fechaApertura != null) {
                cuenta.setFechaApertura(fechaApertura.toLocalDate());
            }

            cuenta.setSaldo(rs.getBigDecimal("SALDO"));
            return cuenta;
        });
    }

    // Obtener cuenta por ID
    public CuentaDTO getCuentaById(String cuentaId) {
        String sql =
                "SELECT " +
                        "  C.CUENTA_ID, " +
                        "  C.CLIENTE_ID, " +
                        "  C.TIPO_CUENTA_ID, " +
                        "  TC.DESCRIPCION AS TIPO_CUENTA_DESC, " +
                        "  C.ESTADO_ID, " +
                        "  E.DESCRIPCION AS ESTADO_DESC, " +
                        "  C.FECHA_APERTURA, " +
                        "  C.SALDO " +
                        "FROM PROYECTODB.TBL_CUENTAS C " +
                        "JOIN PROYECTODB.TBL_TIPOS_PARAMETROS TC ON C.TIPO_CUENTA_ID = TC.TIPO_PARAMETRO_ID " +
                        "JOIN PROYECTODB.TBL_TIPOS_PARAMETROS E ON C.ESTADO_ID = E.TIPO_PARAMETRO_ID " +
                        "WHERE C.CUENTA_ID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{cuentaId}, (rs, rowNum) -> {
                CuentaDTO cuenta = new CuentaDTO();
                cuenta.setCuentaId(rs.getString("CUENTA_ID"));
                cuenta.setClienteId(rs.getString("CLIENTE_ID"));
                cuenta.setTipoCuentaId(rs.getLong("TIPO_CUENTA_ID"));
                cuenta.setTipoCuentaDescripcion(rs.getString("TIPO_CUENTA_DESC"));
                cuenta.setEstadoId(rs.getLong("ESTADO_ID"));
                cuenta.setEstadoDescripcion(rs.getString("ESTADO_DESC"));

                Date fechaApertura = rs.getDate("FECHA_APERTURA");
                if (fechaApertura != null) {
                    cuenta.setFechaApertura(fechaApertura.toLocalDate());
                }

                cuenta.setSaldo(rs.getBigDecimal("SALDO"));
                return cuenta;
            });
        } catch (DataAccessException e) {
            throw new CuentaOperationException("Cuenta no encontrada: " + cuentaId);
        }
    }
}