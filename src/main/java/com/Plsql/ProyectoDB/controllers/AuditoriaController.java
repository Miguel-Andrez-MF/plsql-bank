package com.Plsql.ProyectoDB.controllers;

import com.Plsql.ProyectoDB.dto.AuditoriaTransaccionDTO;
import com.Plsql.ProyectoDB.services.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Autowired
    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public ResponseEntity<List<AuditoriaTransaccionDTO>> getAllAudits() {
        List<AuditoriaTransaccionDTO> auditorias = auditoriaService.getAllAudits();
        return ResponseEntity.ok(auditorias);
    }
}
