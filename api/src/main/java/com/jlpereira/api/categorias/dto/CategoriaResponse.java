package com.jlpereira.api.categorias.dto;

import java.util.UUID;

import com.jlpereira.api.categorias.domain.Categoria;

/**
 *
 * @author Jose Luis Pereira
 */
public record CategoriaResponse(
        UUID id,
        String nombre,
        String descripcion) {

    public static CategoriaResponse from(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
    }
}
