package com.Plsql.ProyectoDB.controllers;

import com.Plsql.ProyectoDB.dto.CreateTransaccionRequest;
import com.Plsql.ProyectoDB.dto.TransaccionDTO;
import com.Plsql.ProyectoDB.dto.UpdateTransaccionRequest;
import com.Plsql.ProyectoDB.services.TransaccionService;
import com.Plsql.ProyectoDB.services.TransaccionUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> createTransaccion(
            @RequestBody CreateTransaccionRequest request,
            @RequestHeader("X-User-ID") Long usuarioId) {
        try {
            transaccionService.createTransaccion(request, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Transacción creada correctamente."));
        } catch (TransaccionUpdateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Ocurrió un error inesperado."));
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

    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> getAllTransacciones() {
        List<TransaccionDTO> transacciones = transaccionService.getAllTransacciones();
        return ResponseEntity.ok(transacciones);
    }
}
