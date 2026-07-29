package com.jlpereira.api.eventos.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.jlpereira.api.eventos.domain.Evento;
import com.jlpereira.api.eventos.domain.enums.EstadoEvento;
import com.jlpereira.api.eventos.domain.enums.Prioridad;

/**
 *
 * @author Jose Luis Pereira
 */
public record EventoResponse(
        UUID id,
        String codigo,
        String titulo,
        String descripcion,
        OffsetDateTime fecha,
        Prioridad prioridad,
        EstadoEvento estado,
        String fuente,
        CategoriaResumen categoria) {
    public record CategoriaResumen(UUID id, String nombre) {
    }

    public static EventoResponse from(Evento e) {
        return new EventoResponse(
                e.getId(), e.getCodigo(), e.getTitulo(), e.getDescripcion(),
                e.getFecha(), e.getPrioridad(), e.getEstado(), e.getFuente(),
                new CategoriaResumen(e.getCategoria().getId(), e.getCategoria().getNombre()));
    }
}
