package com.Plsql.ProyectoDB.services;

import com.Plsql.ProyectoDB.dto.AuditoriaTransaccionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuditoriaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AuditoriaTransaccionDTO> getAllAudits() {
        String sql = "SELECT AUDITORIA_ID, TRANSACCION_ID, USUARIO_ID, FECHA_OPERACION, MONTO_ANTERIOR, MONTO_NUEVO, TIPO_TRANSACCION_ANTERIOR, TIPO_TRANSACCION_NUEVO, OPERACION FROM PROYECTODB.TBL_AUDITORIAS_TRANSACCION ORDER BY FECHA_OPERACION DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            AuditoriaTransaccionDTO auditoria = new AuditoriaTransaccionDTO();
            auditoria.setAuditoriaId(rs.getLong("AUDITORIA_ID"));
            auditoria.setTransaccionId(rs.getLong("TRANSACCION_ID"));
            auditoria.setUsuarioId(rs.getLong("USUARIO_ID"));
            auditoria.setFechaOperacion(rs.getTimestamp("FECHA_OPERACION").toLocalDateTime());
            auditoria.setMontoAnterior(rs.getBigDecimal("MONTO_ANTERIOR"));
            auditoria.setMontoNuevo(rs.getBigDecimal("MONTO_NUEVO"));
            auditoria.setTipoTransaccionAnterior(rs.getLong("TIPO_TRANSACCION_ANTERIOR"));
            auditoria.setTipoTransaccionNuevo(rs.getLong("TIPO_TRANSACCION_NUEVO"));
            auditoria.setOperacion(rs.getString("OPERACION"));
            return auditoria;
        });
    }
}
