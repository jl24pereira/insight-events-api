package com.jlpereira.api.historial.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.jlpereira.api.historial.domain.HistorialEvento;
import com.jlpereira.api.historial.domain.enums.AccionHistorial;

/**
 *
 * @author Jose Luis Pereira
 */
public record HistorialResponse(
        UUID id,
        String usuario,
        OffsetDateTime fecha,
        AccionHistorial accion,
        String comentario) {

    public static HistorialResponse from(HistorialEvento h) {
        return new HistorialResponse(
                h.getId(), h.getUsuario(), h.getFecha(),
                h.getAccion(), h.getComentario());
    }

}
