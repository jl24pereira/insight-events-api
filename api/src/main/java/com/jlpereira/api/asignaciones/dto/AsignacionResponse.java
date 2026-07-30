package com.jlpereira.api.asignaciones.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.jlpereira.api.asignaciones.domain.Asignacion;
import com.jlpereira.api.asignaciones.domain.enums.EstadoAsignacion;

/**
 *
 * @author Jose Luis Pereira
 */
public record AsignacionResponse(
        UUID id,
        UUID eventoId,
        String eventoCodigo,
        UUID analistaId,
        String analistaNombre,
        OffsetDateTime fechaAsignacion,
        EstadoAsignacion estado) {

    public static AsignacionResponse from(Asignacion a) {
        return new AsignacionResponse(
                a.getId(),
                a.getEvento().getId(), a.getEvento().getCodigo(),
                a.getAnalista().getId(), a.getAnalista().getNombre(),
                a.getFechaAsignacion(), a.getEstado());
    }
}
