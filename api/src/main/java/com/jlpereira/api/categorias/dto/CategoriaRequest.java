package com.jlpereira.api.categorias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Jose Luis Pereira
 */
public record CategoriaRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 100, message = "El nombre no puede exceder 100 caracteres") String nombre,

        @Size(max = 500, message = "La descripcion no puede exceder 500 caracteres") String descripcion) {

}
