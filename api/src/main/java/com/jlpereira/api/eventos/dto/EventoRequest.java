package com.jlpereira.api.eventos.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.jlpereira.api.eventos.domain.enums.Prioridad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Jose Luis Pereira
 */
public record EventoRequest(
        @NotBlank(message = "El título es obligatorio") @Size(max = 200, message = "El título no puede exceder 200 caracteres") String titulo,

        String descripcion,

        @NotNull(message = "La fecha es obligatoria") OffsetDateTime fecha,

        @NotNull(message = "La prioridad es obligatoria") Prioridad prioridad,

        @Size(max = 120, message = "La fuente no puede exceder 120 caracteres") String fuente,

        @NotNull(message = "La categoría es obligatoria") UUID categoriaId) {

}
