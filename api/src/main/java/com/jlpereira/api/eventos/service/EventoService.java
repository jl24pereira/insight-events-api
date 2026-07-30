package com.jlpereira.api.eventos.service;

import java.text.MessageFormat;
import java.util.UUID;

import com.jlpereira.api.categorias.service.CategoriaService;
import com.jlpereira.api.eventos.domain.Evento;
import com.jlpereira.api.eventos.domain.enums.EstadoEvento;
import com.jlpereira.api.eventos.dto.EventoRequest;
import com.jlpereira.api.eventos.dto.EventoResponse;
import com.jlpereira.api.eventos.repository.EventoRepository;
import com.jlpereira.api.historial.domain.enums.AccionHistorial;
import com.jlpereira.api.historial.service.HistorialService;
import com.jlpereira.api.shared.exception.BusinessRuleException;
import com.jlpereira.api.shared.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Jose Luis Pereira
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoService {

    private final EventoRepository repository;

    private final CategoriaService categoriaService;
    private final HistorialService historialService;

    public Page<EventoResponse> listEvento(Pageable pageable) {
        return repository.findAll(pageable).map(EventoResponse::from);
    }

    public EventoResponse getEvento(UUID id) {
        return EventoResponse.from(searchWithCategoria(id));
    }

    @Transactional
    public EventoResponse createEvento(EventoRequest request, String usuario) {
        var categoria = categoriaService.getCategoriaEntity(request.categoriaId());

        Evento evento = Evento.builder()
                .titulo(request.titulo().trim())
                .descripcion(request.descripcion())
                .fecha(request.fecha())
                .prioridad(request.prioridad())
                .estado(EstadoEvento.NUEVO)
                .fuente(request.fuente())
                .categoria(categoria)
                .build();

        Evento saved = repository.saveAndFlush(evento);

        historialService.registrar(saved, usuario, AccionHistorial.CREACION,
                MessageFormat.format("Evento creado con codigo: {0}", saved.getCodigo()));

        return EventoResponse.from(saved);
    }

    @Transactional
    public EventoResponse updateEvento(UUID id, EventoRequest request, String usuario) {
        Evento evento = searchWithCategoria(id);
        var categoria = categoriaService.getCategoriaEntity(request.categoriaId());

        evento.setTitulo(request.titulo().trim());
        evento.setDescripcion(request.descripcion());
        evento.setFecha(request.fecha());
        evento.setPrioridad(request.prioridad());
        evento.setFuente(request.fuente());
        evento.setCategoria(categoria);

        historialService.registrar(evento, usuario, AccionHistorial.ACTUALIZACION, "Datos del evento actualizados");

        return EventoResponse.from(evento);
    }

    @Transactional
    public EventoResponse changeStatus(UUID id, EstadoEvento nuevoEstado, String comentario, String usuario) {
        Evento evento = searchWithCategoria(id);
        EstadoEvento anterior = evento.getEstado();

        if (anterior == nuevoEstado)
            throw new BusinessRuleException(
                    MessageFormat.format("El evento ya se encuentra en estado: {0}", nuevoEstado));

        evento.setEstado(nuevoEstado);

        historialService.registrar(evento, usuario, AccionHistorial.CAMBIO_ESTADO,
                MessageFormat.format("Estado cambiado de: {0} a {1}. {2}", anterior, nuevoEstado,
                        comentario == null || comentario.isBlank() ? "" : comentario));

        return EventoResponse.from(evento);
    }

    @Transactional
    public void deleteEvento(UUID id) {
        repository.delete(searchWithCategoria(id));
    }

    private Evento searchWithCategoria(UUID id) {
        return repository.findWithCategoriaById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Evento", id));
    }

}
