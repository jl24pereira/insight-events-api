package com.jlpereira.api.asignaciones.service;

import java.util.UUID;

import com.jlpereira.api.asignaciones.dto.AsignacionResponse;
import com.jlpereira.api.asignaciones.repository.AsignacionRepository;
import com.jlpereira.api.asignaciones.repository.AsignacionSpRepository;
import com.jlpereira.api.shared.exception.BusinessRuleException;
import com.jlpereira.api.shared.exception.ResourceNotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Jose Luis Pereira
 */
@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository repository;
    private final AsignacionSpRepository spRepository;

    @Transactional
    public AsignacionResponse asignar(UUID eventoId, UUID analistaId, String usuario) {
        UUID asignacionId;
        try {
            asignacionId = spRepository.asignar(eventoId, analistaId, usuario);
        } catch (DataAccessException ex) {
            throw traducirError(ex);
        }

        return repository.findById(asignacionId)
                .map(AsignacionResponse::from)
                .orElseThrow(() -> new IllegalStateException(
                        "El procedimiento no devolvió una asignación válida"));
    }

    private RuntimeException traducirError(DataAccessException ex) {
        String mensaje = ex.getMostSpecificCause().getMessage();

        if (mensaje.contains("EVENTO_NO_ENCONTRADO")) {
            return ResourceNotFoundException.de("Evento", extraerId(mensaje));
        }
        if (mensaje.contains("ANALISTA_NO_ENCONTRADO")) {
            return ResourceNotFoundException.de("Analista", extraerId(mensaje));
        }
        if (mensaje.contains("ASIGNACION_DUPLICADA")) {
            return new BusinessRuleException("El evento ya fue asignado previamente a ese analista");
        }
        return new IllegalStateException("Error inesperado al asignar el evento", ex);
    }

    private String extraerId(String mensaje) {
        int idx = mensaje.indexOf(':');
        return idx >= 0 ? mensaje.substring(idx + 1).trim() : mensaje;
    }

}
