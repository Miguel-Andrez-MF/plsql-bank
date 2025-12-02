package com.Plsql.ProyectoDB.services;

import com.Plsql.ProyectoDB.dto.CreateTransaccionRequest;
import com.Plsql.ProyectoDB.dto.TransaccionDTO;
import com.Plsql.ProyectoDB.dto.UpdateTransaccionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransaccionService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransaccionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTransaccion(CreateTransaccionRequest request, Long usuarioId) {
        System.out.println("--- DEBUG: Creando transacción para Usuario ID: " + usuarioId + " ---");
        System.out.println("Cuenta ID recibida: " + request.getCuentaId());
        System.out.println("Monto recibido: " + request.getMonto());
        System.out.println("TipoTransacId recibido: " + request.getTipoTransacId());
        System.out.println("----------------------------------------------------------");


        if (isSuperAdmin(usuarioId)) {
            throw new TransaccionUpdateException("Operación no permitida: Los super-administradores no pueden realizar transacciones.");
        }

        if (request.getCuentaId() == null || request.getCuentaId().isBlank()) {
            throw new IllegalArgumentException("El campo 'cuentaId' es obligatorio para crear una transacción.");
        }


        try {
            String sql = "INSERT INTO PROYECTODB.TBL_TRANSACCIONES (TRANSACCION_ID, CUENTA_ID, TIPO_TRANSAC_ID, MONTO, FECHA_TRANSAC) VALUES (PROYECTODB.SEQ_TRANSACCION.NEXTVAL, ?, ?, ?, SYSDATE)";
            jdbcTemplate.update(sql, request.getCuentaId(), request.getTipoTransacId(), request.getMonto());
        } catch (DataAccessException e) {
            Throwable rootCause = e.getMostSpecificCause();
            throw new TransaccionUpdateException("Error al crear la transacción: " + rootCause.getMessage());
        }
    }

    private boolean isSuperAdmin(Long usuarioId) {
        String plsql = "DECLARE " +
                       "  v_is_super BOOLEAN; " +
                       "BEGIN " +
                       "  v_is_super := PROYECTODB.AUTENTICACION_PKG.es_superadmin(?); " +
                       "  ? := CASE WHEN v_is_super THEN 1 ELSE 0 END; " +
                       "END;";
        try {
            return Boolean.TRUE.equals(
                    jdbcTemplate.execute(plsql, (CallableStatementCallback<Boolean>) cs -> {
                        cs.setLong(1, usuarioId);
                        cs.registerOutParameter(2, Types.INTEGER);
                        cs.execute();
                        return cs.getInt(2) == 1;
                    })
            );
        } catch (DataAccessException e) {
            System.err.println("ERROR: DataAccessException in isSuperAdmin for usuarioId: " + usuarioId);
            e.printStackTrace();
            throw new TransaccionUpdateException("Error al verificar rol de usuario: " + e.getMostSpecificCause().getMessage());
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

    public List<TransaccionDTO> getAllTransacciones() {
        String sql = "SELECT TRANSACCION_ID, CUENTA_ID, TIPO_TRANSAC_ID, MONTO, FECHA_TRANSAC FROM PROYECTODB.TBL_TRANSACCIONES ORDER BY FECHA_TRANSAC ASC";
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
}
