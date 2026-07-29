package com.jlpereira.api.eventos.dto;

import com.jlpereira.api.eventos.domain.enums.EstadoEvento;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Jose Luis Pereira
 */
public record CambioEstadoRequest(
        @NotNull(message = "El nuevo estado es obligatorio") EstadoEvento estado,
        String comentario) {

}
