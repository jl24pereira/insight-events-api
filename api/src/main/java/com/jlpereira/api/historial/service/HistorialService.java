package com.jlpereira.api.historial.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.jlpereira.api.eventos.domain.Evento;
import com.jlpereira.api.eventos.repository.EventoRepository;
import com.jlpereira.api.historial.domain.HistorialEvento;
import com.jlpereira.api.historial.domain.enums.AccionHistorial;
import com.jlpereira.api.historial.dto.HistorialResponse;
import com.jlpereira.api.historial.reposiroty.HistorialEventoRepository;
import com.jlpereira.api.shared.exception.ResourceNotFoundException;

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
public class HistorialService {

    private final HistorialEventoRepository repository;
    private final EventoRepository eventoRepository;

    public List<HistorialResponse> consultarPorEvento(UUID eventoId) {
        if (!eventoRepository.existsById(eventoId))
            throw ResourceNotFoundException.de("Evento", eventoId);

        return repository.buscarHistorialCompleto(eventoId).stream()
                .map(HistorialResponse::from)
                .toList();
    }

    @Transactional
    public void registrar(Evento evento, String usuario,
            AccionHistorial accion, String comentario) {
        repository.save(HistorialEvento.builder()
                .evento(evento)
                .usuario(usuario)
                .fecha(OffsetDateTime.now())
                .accion(accion)
                .comentario(comentario)
                .build());
    }
}
