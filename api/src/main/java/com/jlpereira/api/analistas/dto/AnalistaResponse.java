package com.jlpereira.api.analistas.dto;

import java.util.UUID;

import com.jlpereira.api.analistas.domain.Analista;

/**
 *
 * @author Jose Luis Pereira
 */
public record AnalistaResponse(
        UUID id,
        String nombre,
        String correo) {
    public static AnalistaResponse from(Analista analista) {
        return new AnalistaResponse(
                analista.getId(),
                analista.getNombre(),
                analista.getCorreo());
    }
}
