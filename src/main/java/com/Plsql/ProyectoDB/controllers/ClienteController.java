package com.Plsql.ProyectoDB.controllers;

import com.Plsql.ProyectoDB.dto.ClienteDTO;
import com.Plsql.ProyectoDB.dto.CreateClienteRequest;
import com.Plsql.ProyectoDB.dto.UpdateClienteRequest;
import com.Plsql.ProyectoDB.services.ClienteOperationException;
import com.Plsql.ProyectoDB.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody CreateClienteRequest request) {
        try {
            String clienteId = clienteService.createCliente(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Cliente creado exitosamente", "clienteId", clienteId));
        } catch (ClienteOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error inesperado"));
        }
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<?> updateCliente(
            @PathVariable String clienteId,
            @RequestBody UpdateClienteRequest request) {
        try {
            clienteService.updateCliente(clienteId, request);
            return ResponseEntity.ok(Map.of("mensaje", "Cliente actualizado exitosamente"));
        } catch (ClienteOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error inesperado"));
        }
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<?> deleteCliente(@PathVariable String clienteId) {
        try {
            clienteService.deleteCliente(clienteId);
            return ResponseEntity.ok(Map.of("mensaje", "Cliente eliminado exitosamente"));
        } catch (ClienteOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error inesperado"));
        }
    }

    @GetMapping
    public ResponseEntity<?> listClientes(@RequestParam(required = false) String filtroNombre) {
        try {
            List<ClienteDTO> clientes = clienteService.listClientes(filtroNombre);
            return ResponseEntity.ok(clientes);
        } catch (ClienteOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error inesperado"));
        }
    }

    @GetMapping("/buscar/{identificacion}")
    public ResponseEntity<?> findByIdentificacion(@PathVariable Long identificacion) {
        try {
            String clienteId = clienteService.findByIdentificacion(identificacion);
            if (clienteId != null) {
                return ResponseEntity.ok(Map.of("clienteId", clienteId));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "Cliente no encontrado"));
            }
        } catch (ClienteOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error inesperado"));
        }
    }
}