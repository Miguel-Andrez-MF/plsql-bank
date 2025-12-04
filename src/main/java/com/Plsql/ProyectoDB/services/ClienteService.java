package com.Plsql.ProyectoDB.services;

import com.Plsql.ProyectoDB.dto.ClienteDTO;
import com.Plsql.ProyectoDB.dto.CreateClienteRequest;
import com.Plsql.ProyectoDB.dto.UpdateClienteRequest;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClienteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Crear cliente
    public String createCliente(CreateClienteRequest request) {
        String plsql =
                "DECLARE " +
                        "  v_cliente_id VARCHAR2(20); " +
                        "  v_resultado VARCHAR2(500); " +
                        "BEGIN " +
                        "  PROYECTODB.gestion_clientes_pkg.crear_cliente( " +
                        "    p_nombre => ?, " +
                        "    p_identificacion => ?, " +
                        "    p_direccion => ?, " +
                        "    p_telefono => ?, " +
                        "    p_email => ?, " +
                        "    p_usuario_id => ?, " +
                        "    p_cliente_id => v_cliente_id, " +
                        "    p_resultado => v_resultado " +
                        "  ); " +
                        "  ? := v_cliente_id; " +
                        "  ? := v_resultado; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<String>) cs -> {
                cs.setString(1, request.getNombre());
                cs.setLong(2, request.getIdentificacion());
                cs.setString(3, request.getDireccion());
                cs.setString(4, request.getTelefono());
                cs.setString(5, request.getEmail());

                if (request.getUsuarioId() != null) {
                    cs.setLong(6, request.getUsuarioId());
                } else {
                    cs.setNull(6, Types.NUMERIC);
                }

                cs.registerOutParameter(7, Types.VARCHAR);
                cs.registerOutParameter(8, Types.VARCHAR);

                cs.execute();

                String clienteId = cs.getString(7);
                String resultado = cs.getString(8);

                if (resultado.startsWith("EXITO")) {
                    return clienteId;
                } else {
                    throw new ClienteOperationException(resultado);
                }
            });
        } catch (DataAccessException e) {
            throw new ClienteOperationException("Error al crear cliente: " + e.getMostSpecificCause().getMessage());
        }
    }

    // Actualizar cliente
    public void updateCliente(String clienteId, UpdateClienteRequest request) {
        String plsql =
                "DECLARE " +
                        "  v_resultado VARCHAR2(500); " +
                        "BEGIN " +
                        "  PROYECTODB.gestion_clientes_pkg.actualizar_cliente( " +
                        "    p_cliente_id => ?, " +
                        "    p_nombre => ?, " +
                        "    p_direccion => ?, " +
                        "    p_telefono => ?, " +
                        "    p_email => ?, " +
                        "    p_resultado => v_resultado " +
                        "  ); " +
                        "  ? := v_resultado; " +
                        "END;";

        try {
            jdbcTemplate.execute(plsql, (CallableStatementCallback<Void>) cs -> {
                cs.setString(1, clienteId);
                cs.setString(2, request.getNombre());
                cs.setString(3, request.getDireccion());
                cs.setString(4, request.getTelefono());
                cs.setString(5, request.getEmail());
                cs.registerOutParameter(6, Types.VARCHAR);

                cs.execute();

                String resultado = cs.getString(6);
                if (!resultado.startsWith("EXITO")) {
                    throw new ClienteOperationException(resultado);
                }
                return null;
            });
        } catch (DataAccessException e) {
            throw new ClienteOperationException("Error al actualizar cliente: " + e.getMostSpecificCause().getMessage());
        }
    }

    // Eliminar cliente
    public void deleteCliente(String clienteId) {
        String plsql =
                "DECLARE " +
                        "  v_resultado VARCHAR2(500); " +
                        "BEGIN " +
                        "  PROYECTODB.gestion_clientes_pkg.eliminar_cliente( " +
                        "    p_cliente_id => ?, " +
                        "    p_resultado => v_resultado " +
                        "  ); " +
                        "  ? := v_resultado; " +
                        "END;";

        try {
            jdbcTemplate.execute(plsql, (CallableStatementCallback<Void>) cs -> {
                cs.setString(1, clienteId);
                cs.registerOutParameter(2, Types.VARCHAR);

                cs.execute();

                String resultado = cs.getString(2);
                if (!resultado.startsWith("EXITO")) {
                    throw new ClienteOperationException(resultado);
                }
                return null;
            });
        } catch (DataAccessException e) {
            throw new ClienteOperationException("Error al eliminar cliente: " + e.getMostSpecificCause().getMessage());
        }
    }

    // Listar todos los clientes
    public List<ClienteDTO> listClientes(String filtroNombre) {
        String plsql =
                "DECLARE " +
                        "  v_cursor SYS_REFCURSOR; " +
                        "BEGIN " +
                        "  v_cursor := PROYECTODB.gestion_clientes_pkg.listar_clientes(?); " +
                        "  ? := v_cursor; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<List<ClienteDTO>>) cs -> {
                if (filtroNombre != null && !filtroNombre.isBlank()) {
                    cs.setString(1, filtroNombre);
                } else {
                    cs.setNull(1, Types.VARCHAR);
                }

                cs.registerOutParameter(2, OracleTypes.CURSOR);
                cs.execute();

                List<ClienteDTO> clientes = new ArrayList<>();
                ResultSet rs = (ResultSet) cs.getObject(2);

                while (rs.next()) {
                    ClienteDTO cliente = new ClienteDTO();
                    cliente.setClienteId(rs.getString("CLIENTE_ID"));
                    cliente.setNombre(rs.getString("NOMBRE"));
                    cliente.setIdentificacion(rs.getLong("IDENTIFICACION"));
                    cliente.setEmail(rs.getString("EMAIL"));
                    cliente.setTelefono(rs.getString("TELEFONO"));
                    cliente.setUsuarioId(rs.getLong("USUARIO_ID"));
                    cliente.setDireccion(rs.getString("DIRECCION"));
                    cliente.setTotalCuentas(rs.getLong("TOTAL_CUENTAS"));
                    cliente.setSaldoTotal(rs.getBigDecimal("SALDO_TOTAL"));
                    clientes.add(cliente);
                }
                rs.close();
                return clientes;
            });
        } catch (DataAccessException e) {
            throw new ClienteOperationException("Error al listar clientes: " + e.getMostSpecificCause().getMessage());
        }
    }

    // Buscar cliente por identificación
    public String findByIdentificacion(Long identificacion) {
        String plsql =
                "DECLARE " +
                        "  v_cliente_id VARCHAR2(20); " +
                        "BEGIN " +
                        "  v_cliente_id := PROYECTODB.gestion_clientes_pkg.buscar_por_identificacion(?); " +
                        "  ? := v_cliente_id; " +
                        "END;";

        try {
            return jdbcTemplate.execute(plsql, (CallableStatementCallback<String>) cs -> {
                cs.setLong(1, identificacion);
                cs.registerOutParameter(2, Types.VARCHAR);
                cs.execute();
                return cs.getString(2);
            });
        } catch (DataAccessException e) {
            throw new ClienteOperationException("Error al buscar cliente: " + e.getMostSpecificCause().getMessage());
        }
    }
}