package com.jlpereira.api.categorias.web;

import java.net.URI;
import java.util.UUID;

import com.jlpereira.api.categorias.dto.CategoriaRequest;
import com.jlpereira.api.categorias.dto.CategoriaResponse;
import com.jlpereira.api.categorias.service.CategoriaService;
import com.jlpereira.api.shared.dto.PageResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Jose Luis Pereira
 */
@RestController
@RequestMapping("/api/v1/categoria")
@RequiredArgsConstructor
public class CateogriaController {

    private final CategoriaService service;

    @GetMapping
    public PageResponse<CategoriaResponse> listar(
            @PageableDefault(size = 20, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        return PageResponse.from(service.listCategoria(pageable));
    }

    @GetMapping("/{id}")
    public CategoriaResponse obtener(@PathVariable UUID id) {
        return service.getCategoria(id);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse creada = service.createCategoria(request);
        return ResponseEntity
                .created(URI.create("/api/v1/categorias/" + creada.id()))
                .body(creada);
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CategoriaRequest request) {
        return service.updateCategoria(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        service.deleteCategoria(id);
    }

}
