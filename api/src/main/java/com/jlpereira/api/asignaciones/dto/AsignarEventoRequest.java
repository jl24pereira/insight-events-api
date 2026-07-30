package com.jlpereira.api.asignaciones.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Jose Luis Pereira
 */
public record AsignarEventoRequest(
        @NotNull(message = "El ID del analista es obligatorio") UUID analistaId) {

}
