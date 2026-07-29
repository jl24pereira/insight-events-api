package com.jlpereira.api.analistas.service;

import java.util.UUID;

import com.jlpereira.api.analistas.domain.Analista;
import com.jlpereira.api.analistas.dto.AnalistaRequest;
import com.jlpereira.api.analistas.dto.AnalistaResponse;
import com.jlpereira.api.analistas.repository.AnalistaRepository;
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
public class AnalistaService {

    private final AnalistaRepository repository;

    public Page<AnalistaResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(AnalistaResponse::from);
    }

    public AnalistaResponse getAnalista(UUID id) {
        return AnalistaResponse.from(searchAnalista(id));
    }

    @Transactional
    public AnalistaResponse createAnalista(AnalistaRequest request) {
        String correo = request.correoNormalizado();

        if (repository.existsByCorreo(correo)) {
            throw new BusinessRuleException("Ya existe un analista con el correo: " + correo);
        }

        Analista analista = Analista.builder()
                .nombre(request.nombre().trim())
                .correo(correo)
                .build();

        return AnalistaResponse.from(repository.save(analista));
    }

    @Transactional
    public AnalistaResponse updateAnalista(UUID id, AnalistaRequest request) {
        Analista analista = searchAnalista(id);
        String correo = request.correoNormalizado();

        if (repository.existsByCorreoAndIdNot(correo, id)) {
            throw new BusinessRuleException("Ya existe otro analista con el correo: " + correo);
        }

        analista.setNombre(request.nombre().trim());
        analista.setCorreo(correo);

        return AnalistaResponse.from(analista);
    }

    @Transactional
    public void deleteAnalista(UUID id) {
        repository.delete(searchAnalista(id));
    }

    private Analista searchAnalista(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Analista", id));
    }
}
