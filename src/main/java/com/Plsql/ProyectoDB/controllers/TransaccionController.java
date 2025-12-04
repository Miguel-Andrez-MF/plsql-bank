package com.Plsql.ProyectoDB.controllers;

import com.Plsql.ProyectoDB.dto.*;
import com.Plsql.ProyectoDB.services.TransaccionOperationException;
import com.Plsql.ProyectoDB.services.TransaccionService;
import com.Plsql.ProyectoDB.services.TransaccionUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @Autowired
    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    // ========================================
    // OPERACIONES PRINCIPALES
    // ========================================

    /**
     * Realizar un depósito
     * POST /api/transacciones/deposito
     */
    @PostMapping("/deposito")
    public ResponseEntity<?> realizarDeposito(
            @RequestBody DepositoRequest request,
            @RequestHeader("X-User-ID") Long usuarioId) {
        try {
            TransaccionService.TransaccionResultado resultado =
                    transaccionService.realizarDeposito(request.getCuentaId(), request.getMonto(), usuarioId);

            if (resultado.isExitoso()) {
                return ResponseEntity.ok(Map.of(
                        "exitoso", true,
                        "transaccionId", resultado.getTransaccionId(),
                        "mensaje", resultado.getMensaje()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("exitoso", false, "error", resultado.getMensaje()));
            }
        } catch (TransaccionOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("exitoso", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("exitoso", false, "error", "Error interno del servidor"));
        }
    }

    /**
     * Realizar un retiro
     * POST /api/transacciones/retiro
     */
    @PostMapping("/retiro")
    public ResponseEntity<?> realizarRetiro(
            @RequestBody RetiroRequest request,
            @RequestHeader("X-User-ID") Long usuarioId) {
        try {
            TransaccionService.TransaccionResultado resultado =
                    transaccionService.realizarRetiro(request.getCuentaId(), request.getMonto(), usuarioId);

            if (resultado.isExitoso()) {
                return ResponseEntity.ok(Map.of(
                        "exitoso", true,
                        "transaccionId", resultado.getTransaccionId(),
                        "mensaje", resultado.getMensaje()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("exitoso", false, "error", resultado.getMensaje()));
            }
        } catch (TransaccionOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("exitoso", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("exitoso", false, "error", "Error interno del servidor"));
        }
    }

    /**
     * Realizar una transferencia
     * POST /api/transacciones/transferencia
     */
    @PostMapping("/transferencia")
    public ResponseEntity<?> realizarTransferencia(
            @RequestBody TransferenciaRequest request,
            @RequestHeader("X-User-ID") Long usuarioId) {
        try {
            TransaccionService.TransaccionResultado resultado =
                    transaccionService.realizarTransferencia(
                            request.getCuentaOrigen(),
                            request.getCuentaDestino(),
                            request.getMonto(),
                            usuarioId
                    );

            if (resultado.isExitoso()) {
                return ResponseEntity.ok(Map.of(
                        "exitoso", true,
                        "transaccionId", resultado.getTransaccionId(),
                        "mensaje", resultado.getMensaje()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("exitoso", false, "error", resultado.getMensaje()));
            }
        } catch (TransaccionOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("exitoso", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("exitoso", false, "error", "Error interno del servidor"));
        }
    }

    // ========================================
    // CONSULTAS
    // ========================================

    /**
     * Consultar saldo de una cuenta
     * GET /api/transacciones/saldo/{cuentaId}
     */
    @GetMapping("/saldo/{cuentaId}")
    public ResponseEntity<?> consultarSaldo(@PathVariable String cuentaId) {
        try {
            var saldo = transaccionService.consultarSaldo(cuentaId);
            return ResponseEntity.ok(new SaldoResponse(cuentaId, saldo));
        } catch (TransaccionOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al consultar saldo"));
        }
    }

    /**
     * Generar historial de transacciones
     * GET /api/transacciones/historial/{cuentaId}
     */
    @GetMapping("/historial/{cuentaId}")
    public ResponseEntity<?> generarHistorial(
            @PathVariable String cuentaId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {
        try {
            List<HistorialTransaccionDTO> historial =
                    transaccionService.generarHistorial(cuentaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(historial);
        } catch (TransaccionOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al generar historial"));
        }
    }

    /**
     * Listar todas las transacciones
     * GET /api/transacciones
     */
    @GetMapping
    public ResponseEntity<?> getAllTransacciones() {
        try {
            List<TransaccionDTO> transacciones = transaccionService.getAllTransacciones();
            return ResponseEntity.ok(transacciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener transacciones"));
        }
    }

    /**
     * Obtener transacciones por cuenta
     * GET /api/transacciones/cuenta/{cuentaId}
     */
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<?> getTransaccionesByCuenta(@PathVariable String cuentaId) {
        try {
            List<TransaccionDTO> transacciones = transaccionService.getTransaccionesByCuenta(cuentaId);
            return ResponseEntity.ok(transacciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener transacciones de la cuenta"));
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTransaccion(@PathVariable Long id, @RequestBody UpdateTransaccionRequest request) {
        try {
            transaccionService.updateTransaccion(id, request);
            return ResponseEntity.ok(Map.of("mensaje", "Transacción actualizada correctamente."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (TransaccionUpdateException e) {
            // Aquí se maneja el error del trigger o si no se encuentra el ID
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

}