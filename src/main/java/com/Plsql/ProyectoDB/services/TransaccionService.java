package com.Plsql.ProyectoDB.services;

import com.Plsql.ProyectoDB.dto.HistorialTransaccionDTO;
import com.Plsql.ProyectoDB.dto.TransaccionDTO;
import com.Plsql.ProyectoDB.dto.UpdateTransaccionRequest;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransaccionService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransaccionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Realizar un depósito usando el paquete PL/SQL
     */
    public TransaccionResultado realizarDeposito(String cuentaId, BigDecimal monto, Long usuarioId) {
        String plsql =
                "DECLARE " +
                        "  v_transaccion_id NUMBER; " +
                        "  v_resultado VARCHAR2(1000); " +
                        "BEGIN " +
                        "  PROYECTODB.gestion_transacciones_pkg.realizar_deposito( " +
                        "    p_cuenta_id => ?, " +
                        "    p_monto => ?, " +
                        "    p_usuario_id => ?, " +
                        "    p_transaccion_id => v_transaccion_id, " +
                        "    p_resultado => v_resultado " +
                        "  ); " +
                        "  ? := v_transaccion_id; " +
                        "  ? := v_resultado; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<TransaccionResultado>) cs -> {
                cs.setString(1, cuentaId);
                cs.setBigDecimal(2, monto);
                cs.setLong(3, usuarioId);
                cs.registerOutParameter(4, Types.NUMERIC);
                cs.registerOutParameter(5, Types.VARCHAR);

                cs.execute();

                TransaccionResultado resultado = new TransaccionResultado();
                resultado.setTransaccionId(cs.getLong(4));
                resultado.setMensaje(cs.getString(5));
                resultado.setExitoso(resultado.getMensaje().startsWith("EXITO"));

                return resultado;
            });
        } catch (DataAccessException e) {
            throw new TransaccionOperationException("Error al realizar depósito: " + e.getMostSpecificCause().getMessage());
        }
    }

    /**
     * Realizar un retiro usando el paquete PL/SQL
     */
    public TransaccionResultado realizarRetiro(String cuentaId, BigDecimal monto, Long usuarioId) {
        String plsql =
                "DECLARE " +
                        "  v_transaccion_id NUMBER; " +
                        "  v_resultado VARCHAR2(1000); " +
                        "BEGIN " +
                        "  PROYECTODB.gestion_transacciones_pkg.realizar_retiro( " +
                        "    p_cuenta_id => ?, " +
                        "    p_monto => ?, " +
                        "    p_usuario_id => ?, " +
                        "    p_transaccion_id => v_transaccion_id, " +
                        "    p_resultado => v_resultado " +
                        "  ); " +
                        "  ? := v_transaccion_id; " +
                        "  ? := v_resultado; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<TransaccionResultado>) cs -> {
                cs.setString(1, cuentaId);
                cs.setBigDecimal(2, monto);
                cs.setLong(3, usuarioId);
                cs.registerOutParameter(4, Types.NUMERIC);
                cs.registerOutParameter(5, Types.VARCHAR);

                cs.execute();

                TransaccionResultado resultado = new TransaccionResultado();
                resultado.setTransaccionId(cs.getLong(4));
                resultado.setMensaje(cs.getString(5));
                resultado.setExitoso(resultado.getMensaje().startsWith("EXITO"));

                return resultado;
            });
        } catch (DataAccessException e) {
            throw new TransaccionOperationException("Error al realizar retiro: " + e.getMostSpecificCause().getMessage());
        }
    }

    /**
     * Realizar una transferencia usando el paquete PL/SQL
     */
    public TransaccionResultado realizarTransferencia(String cuentaOrigen, String cuentaDestino,
                                                      BigDecimal monto, Long usuarioId) {
        String plsql =
                "DECLARE " +
                        "  v_transaccion_id NUMBER; " +
                        "  v_resultado VARCHAR2(2000); " +
                        "BEGIN " +
                        "  PROYECTODB.gestion_transacciones_pkg.realizar_transferencia( " +
                        "    p_cuenta_origen => ?, " +
                        "    p_cuenta_destino => ?, " +
                        "    p_monto => ?, " +
                        "    p_usuario_id => ?, " +
                        "    p_transaccion_id => v_transaccion_id, " +
                        "    p_resultado => v_resultado " +
                        "  ); " +
                        "  ? := v_transaccion_id; " +
                        "  ? := v_resultado; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<TransaccionResultado>) cs -> {
                cs.setString(1, cuentaOrigen);
                cs.setString(2, cuentaDestino);
                cs.setBigDecimal(3, monto);
                cs.setLong(4, usuarioId);
                cs.registerOutParameter(5, Types.NUMERIC);
                cs.registerOutParameter(6, Types.VARCHAR);

                cs.execute();

                TransaccionResultado resultado = new TransaccionResultado();
                resultado.setTransaccionId(cs.getLong(5));
                resultado.setMensaje(cs.getString(6));
                resultado.setExitoso(resultado.getMensaje().startsWith("EXITO"));

                return resultado;
            });
        } catch (DataAccessException e) {
            throw new TransaccionOperationException("Error al realizar transferencia: " + e.getMostSpecificCause().getMessage());
        }
    }

    /**
     * Consultar saldo de una cuenta
     */
    public BigDecimal consultarSaldo(String cuentaId) {
        String plsql =
                "DECLARE " +
                        "  v_saldo NUMBER; " +
                        "BEGIN " +
                        "  v_saldo := PROYECTODB.gestion_transacciones_pkg.consultar_saldo(?); " +
                        "  ? := v_saldo; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<BigDecimal>) cs -> {
                cs.setString(1, cuentaId);
                cs.registerOutParameter(2, Types.NUMERIC);
                cs.execute();
                return cs.getBigDecimal(2);
            });
        } catch (DataAccessException e) {
            throw new TransaccionOperationException("Error al consultar saldo: " + e.getMostSpecificCause().getMessage());
        }
    }

    /**
     * Generar historial de transacciones
     */
    public List<HistorialTransaccionDTO> generarHistorial(String cuentaId, Date fechaInicio, Date fechaFin) {
        String plsql =
                "DECLARE " +
                        "  v_cursor SYS_REFCURSOR; " +
                        "BEGIN " +
                        "  v_cursor := PROYECTODB.gestion_transacciones_pkg.generar_historial(?, ?, ?); " +
                        "  ? := v_cursor; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<List<HistorialTransaccionDTO>>) cs -> {
                cs.setString(1, cuentaId);

                if (fechaInicio != null) {
                    cs.setDate(2, new java.sql.Date(fechaInicio.getTime()));
                } else {
                    cs.setNull(2, Types.DATE);
                }

                if (fechaFin != null) {
                    cs.setDate(3, new java.sql.Date(fechaFin.getTime()));
                } else {
                    cs.setNull(3, Types.DATE);
                }

                cs.registerOutParameter(4, OracleTypes.CURSOR);
                cs.execute();

                List<HistorialTransaccionDTO> historial = new ArrayList<>();
                ResultSet rs = (ResultSet) cs.getObject(4);

                while (rs.next()) {
                    HistorialTransaccionDTO dto = new HistorialTransaccionDTO();
                    dto.setTransaccionId(rs.getLong("TRANSACCION_ID"));
                    dto.setCuentaId(rs.getString("CUENTA_ID"));
                    dto.setTipoTransaccion(rs.getString("TIPO_TRANSACCION"));
                    dto.setMonto(rs.getBigDecimal("MONTO"));
                    dto.setFechaTransaccion(rs.getTimestamp("FECHA_TRANSAC"));
                    dto.setFechaFormateada(rs.getString("FECHA_FORMATEADA"));
                    historial.add(dto);
                }
                rs.close();
                return historial;
            });
        } catch (DataAccessException e) {
            throw new TransaccionOperationException("Error al generar historial: " + e.getMostSpecificCause().getMessage());
        }
    }

    /**
     * Listar todas las transacciones (consulta directa)
     */
    public List<TransaccionDTO> getAllTransacciones() {
        String sql = "SELECT TRANSACCION_ID, CUENTA_ID, TIPO_TRANSAC_ID, MONTO, FECHA_TRANSAC " +
                "FROM PROYECTODB.TBL_TRANSACCIONES ORDER BY FECHA_TRANSAC DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TransaccionDTO transaccion = new TransaccionDTO();
            transaccion.setTransaccionId(rs.getLong("TRANSACCION_ID"));
            transaccion.setCuentaId(rs.getString("CUENTA_ID"));
            transaccion.setTipoTransacId(rs.getLong("TIPO_TRANSAC_ID"));
            transaccion.setMonto(rs.getBigDecimal("MONTO"));
            transaccion.setFechaTransac(rs.getTimestamp("FECHA_TRANSAC").toLocalDateTime());
            return transaccion;
        });
    }

    /**
     * Obtener transacciones por cuenta
     */
    public List<TransaccionDTO> getTransaccionesByCuenta(String cuentaId) {
        String sql = "SELECT TRANSACCION_ID, CUENTA_ID, TIPO_TRANSAC_ID, MONTO, FECHA_TRANSAC " +
                "FROM PROYECTODB.TBL_TRANSACCIONES WHERE CUENTA_ID = ? ORDER BY FECHA_TRANSAC DESC";

        return jdbcTemplate.query(sql, new Object[]{cuentaId}, (rs, rowNum) -> {
            TransaccionDTO transaccion = new TransaccionDTO();
            transaccion.setTransaccionId(rs.getLong("TRANSACCION_ID"));
            transaccion.setCuentaId(rs.getString("CUENTA_ID"));
            transaccion.setTipoTransacId(rs.getLong("TIPO_TRANSAC_ID"));
            transaccion.setMonto(rs.getBigDecimal("MONTO"));
            transaccion.setFechaTransac(rs.getTimestamp("FECHA_TRANSAC").toLocalDateTime());
            return transaccion;
        });
    }

    // Clase interna para resultado de transacciones
    public static class TransaccionResultado {
        private Long transaccionId;
        private String mensaje;
        private boolean exitoso;

        public Long getTransaccionId() {
            return transaccionId;
        }

        public void setTransaccionId(Long transaccionId) {
            this.transaccionId = transaccionId;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public boolean isExitoso() {
            return exitoso;
        }

        public void setExitoso(boolean exitoso) {
            this.exitoso = exitoso;
        }
    }

    public void updateTransaccion(Long id, UpdateTransaccionRequest request) {
        if (request.getMonto() == null && request.getTipoTransaccionId() == null) {
            throw new IllegalArgumentException("Se requiere al menos un campo (monto o tipoTransaccionId) para actualizar.");
        }

        StringBuilder sql = new StringBuilder("UPDATE PROYECTODB.TBL_TRANSACCIONES SET ");
        List<Object> params = new ArrayList<>();

        if (request.getMonto() != null) {
            sql.append("MONTO = ?");
            params.add(request.getMonto());
        }

        if (request.getTipoTransaccionId() != null) {
            if (!params.isEmpty()) {
                sql.append(", ");
            }
            sql.append("TIPO_TRANSAC_ID = ?");
            params.add(request.getTipoTransaccionId());
        }

        sql.append(" WHERE TRANSACCION_ID = ?");
        params.add(id);

        try {
            int updatedRows = jdbcTemplate.update(sql.toString(), params.toArray());
            if (updatedRows == 0) {
                throw new TransaccionUpdateException("No se encontró la transacción con ID: " + id);
            }
        } catch (DataAccessException e) {
            // Aquí capturamos la excepción de la base de datos
            // El mensaje del trigger estará en la causa raíz.
            Throwable rootCause = e.getMostSpecificCause();
            throw new TransaccionUpdateException("Error al actualizar la transacción: " + rootCause.getMessage());
        }
    }

}