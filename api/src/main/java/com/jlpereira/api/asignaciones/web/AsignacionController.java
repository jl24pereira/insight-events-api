package com.jlpereira.api.asignaciones.web;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;

import com.jlpereira.api.asignaciones.dto.AsignacionResponse;
import com.jlpereira.api.asignaciones.dto.AsignarEventoRequest;
import com.jlpereira.api.asignaciones.service.AsignacionService;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author Jose Luis Pereira
 */
@RestController
@RequestMapping("/api/v1/eventos/{eventoId}/asignacion")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService service;

    @PostMapping
    public ResponseEntity<AsignacionResponse> asignar(
            @PathVariable UUID eventoId,
            @Valid @RequestBody AsignarEventoRequest request,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {
        AsignacionResponse asignacionCreada = service.asignar(eventoId, request.analistaId(), usuario);
        return ResponseEntity
                .created(URI.create("/api/v1/eventos/" + eventoId + "/asignacion"))
                .body(asignacionCreada);

    }

}
