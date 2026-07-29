package com.jlpereira.api.analistas.web;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;

import com.jlpereira.api.analistas.dto.AnalistaRequest;
import com.jlpereira.api.analistas.dto.AnalistaResponse;
import com.jlpereira.api.analistas.service.AnalistaService;
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
@RequestMapping("/api/v1/analistas")
@RequiredArgsConstructor
public class AnalistaController {

    private final AnalistaService service;

    @GetMapping
    public PageResponse<AnalistaResponse> listar(
            @PageableDefault(size = 20, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        return PageResponse.from(service.listar(pageable));
    }

    @GetMapping("/{id}")
    public AnalistaResponse obtener(@PathVariable UUID id) {
        return service.getAnalista(id);
    }

    @PostMapping
    public ResponseEntity<AnalistaResponse> crear(@Valid @RequestBody AnalistaRequest request) {
        AnalistaResponse creado = service.createAnalista(request);
        return ResponseEntity
                .created(URI.create("/api/v1/analistas/" + creado.id()))
                .body(creado);
    }

    @PutMapping("/{id}")
    public AnalistaResponse actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AnalistaRequest request) {
        return service.updateAnalista(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        service.deleteAnalista(id);
    }
}
