package com.jlpereira.api.eventos.web;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;

import com.jlpereira.api.eventos.dto.CambioEstadoRequest;
import com.jlpereira.api.eventos.dto.EventoRequest;
import com.jlpereira.api.eventos.dto.EventoResponse;
import com.jlpereira.api.eventos.service.EventoService;
import com.jlpereira.api.shared.dto.PageResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Jose Luis Pereira
 */
@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService service;

    @GetMapping
    public PageResponse<EventoResponse> listar(
            @PageableDefault(size = 20, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable) {
        return PageResponse.from(service.listEvento(pageable));
    }

    @GetMapping("/{id}")
    public EventoResponse obtener(@PathVariable UUID id) {
        return service.getEvento(id);
    }

    @PostMapping
    public ResponseEntity<EventoResponse> crear(@Valid @RequestBody EventoRequest request,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {
        EventoResponse creado = service.createEvento(request, usuario);
        return ResponseEntity
                .created(URI.create("/api/v1/eventos/" + creado.id()))
                .body(creado);
    }

    @PutMapping("/{id}")
    public EventoResponse actualizar(
            @PathVariable UUID id, @Valid @RequestBody EventoRequest request,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {
        return service.updateEvento(id, request, usuario);
    }

    @PatchMapping("/{id}/estado")
    public EventoResponse actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CambioEstadoRequest request,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {
        return service.changeStatus(id, request.estado(), request.comentario(), usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        service.deleteEvento(id);
    }
}
