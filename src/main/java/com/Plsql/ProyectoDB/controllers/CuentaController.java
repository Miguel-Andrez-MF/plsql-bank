package com.Plsql.ProyectoDB.controllers;

import com.Plsql.ProyectoDB.dto.CuentaDTO;
import com.Plsql.ProyectoDB.services.CuentaOperationException;
import com.Plsql.ProyectoDB.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // Listar todas las cuentas
    @GetMapping
    public ResponseEntity<?> getAllCuentas() {
        try {
            List<CuentaDTO> cuentas = cuentaService.getAllCuentas();
            return ResponseEntity.ok(cuentas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener cuentas: " + e.getMessage()));
        }
    }

    // Obtener cuenta por ID
    @GetMapping("/{cuentaId}")
    public ResponseEntity<?> getCuentaById(@PathVariable String cuentaId) {
        try {
            CuentaDTO cuenta = cuentaService.getCuentaById(cuentaId);
            return ResponseEntity.ok(cuenta);
        } catch (CuentaOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener cuenta: " + e.getMessage()));
        }
    }

    // Obtener cuentas por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> getCuentasByCliente(@PathVariable String clienteId) {
        try {
            List<CuentaDTO> cuentas = cuentaService.getCuentasByCliente(clienteId);
            return ResponseEntity.ok(cuentas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener cuentas: " + e.getMessage()));
        }
    }

    // Activar cuenta
    @PatchMapping("/{cuentaId}/activar")
    public ResponseEntity<?> activarCuenta(@PathVariable String cuentaId) {
        try {
            cuentaService.activarCuenta(cuentaId);
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta activada exitosamente"));
        } catch (CuentaOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al activar cuenta: " + e.getMessage()));
        }
    }

    // Desactivar cuenta
    @PatchMapping("/{cuentaId}/deshabilitar")
    public ResponseEntity<?> deshabilitarCuenta(@PathVariable String cuentaId) {
        try {
            cuentaService.deshabilitarCuenta(cuentaId);
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta deshabilitada exitosamente"));
        } catch (CuentaOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al deshabilitar cuenta: " + e.getMessage()));
        }
    }

    // Bloquear cuenta
    @PatchMapping("/{cuentaId}/bloquear")
    public ResponseEntity<?> bloquearCuenta(@PathVariable String cuentaId) {
        try {
            cuentaService.bloquearCuenta(cuentaId);
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta bloqueada exitosamente"));
        } catch (CuentaOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al bloquear cuenta: " + e.getMessage()));
        }
    }
}


//        ## 📊 Resumen Final del Backend Completo
//```
//        ✅ Auth         → Login
//✅ Transacciones → GET, POST, PATCH
//✅ Auditorías   → GET
//✅ Clientes     → GET, POST, PUT, DELETE, Buscar
//✅ Cuentas      → GET, Cambiar estado