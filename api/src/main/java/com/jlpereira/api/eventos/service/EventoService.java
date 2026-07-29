package com.jlpereira.api.eventos.service;

import java.util.UUID;

import com.jlpereira.api.categorias.service.CategoriaService;
import com.jlpereira.api.eventos.domain.Evento;
import com.jlpereira.api.eventos.domain.enums.EstadoEvento;
import com.jlpereira.api.eventos.dto.EventoRequest;
import com.jlpereira.api.eventos.dto.EventoResponse;
import com.jlpereira.api.eventos.repository.EventoRepository;
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

    public Page<EventoResponse> listEvento(Pageable pageable) {
        return repository.findAll(pageable).map(EventoResponse::from);
    }

    public EventoResponse obtener(UUID id) {
        return EventoResponse.from(searchWithCategoria(id));
    }

    @Transactional
    public EventoResponse createEvento(EventoRequest request) {
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

        return EventoResponse.from(repository.saveAndFlush(evento));
    }

    @Transactional
    public EventoResponse updateEvento(UUID id, EventoRequest request) {
        Evento evento = searchWithCategoria(id);
        var categoria = categoriaService.getCategoriaEntity(request.categoriaId());

        evento.setTitulo(request.titulo().trim());
        evento.setDescripcion(request.descripcion());
        evento.setFecha(request.fecha());
        evento.setPrioridad(request.prioridad());
        evento.setFuente(request.fuente());
        evento.setCategoria(categoria);

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
