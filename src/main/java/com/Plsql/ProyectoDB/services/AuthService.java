package com.Plsql.ProyectoDB.services;

import com.Plsql.ProyectoDB.dto.AuthResponse;
import com.Plsql.ProyectoDB.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;

@Service
public class AuthService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        String plsql =
            "DECLARE " +
            "  v_resultado PROYECTODB.AUTENTICACION_PKG.t_resultado_auth; " +
            "BEGIN " +
            "  v_resultado := PROYECTODB.AUTENTICACION_PKG.validar_credenciales(?, ?); " +
            "  ? := CASE WHEN v_resultado.exitoso THEN 1 ELSE 0 END; " +
            "  ? := v_resultado.usuario_id; " +
            "  ? := v_resultado.cliente_id; " +
            "  ? := v_resultado.nombre_usuario; " +
            "  ? := v_resultado.rol_id; " +
            "  ? := v_resultado.nombre_rol; " +
            "  ? := v_resultado.mensaje; " +
            "END;";

        return jdbcTemplate.execute(plsql, (CallableStatementCallback<AuthResponse>) cs -> {
            cs.setString(1, loginRequest.getUsuario());
            cs.setString(2, loginRequest.getPassword());
            
            // Register OUT parameters
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.NUMERIC);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.registerOutParameter(7, Types.NUMERIC);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.registerOutParameter(9, Types.VARCHAR);

            cs.execute();

            AuthResponse response = new AuthResponse();
            response.setExitoso(cs.getInt(3) == 1);

            BigDecimal usuarioIdBd = cs.getBigDecimal(4);
            if (usuarioIdBd != null) {
                response.setUsuarioId(usuarioIdBd.longValue());
            }

            response.setClienteId(cs.getString(5));
            response.setNombreUsuario(cs.getString(6));

            BigDecimal rolIdBd = cs.getBigDecimal(7);
            if (rolIdBd != null) {
                response.setRolId(rolIdBd.longValue());
            }
            
            response.setNombreRol(cs.getString(8));
            response.setMensaje(cs.getString(9));

            return response;
        });
    }
}