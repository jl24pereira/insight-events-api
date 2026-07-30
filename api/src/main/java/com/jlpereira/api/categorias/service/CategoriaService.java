package com.jlpereira.api.categorias.service;

import java.text.MessageFormat;
import java.util.UUID;

import com.jlpereira.api.categorias.domain.Categoria;
import com.jlpereira.api.categorias.dto.CategoriaRequest;
import com.jlpereira.api.categorias.dto.CategoriaResponse;
import com.jlpereira.api.categorias.repository.CategoriaRepository;
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
public class CategoriaService {

    private final CategoriaRepository repository;

    public Page<CategoriaResponse> listCategoria(Pageable pageable) {
        return repository.findAll(pageable).map(CategoriaResponse::from);
    }

    public CategoriaResponse getCategoria(UUID id) {
        return CategoriaResponse.from(searchCategoria(id));
    }

    @Transactional
    public CategoriaResponse createCategoria(CategoriaRequest request) {
        if (repository.existsByNombreIgnoreCase(request.nombre())) {
            throw new BusinessRuleException(
                    MessageFormat.format("Ya existe categoria con el nombre: {0}", request.nombre()));
        }

        Categoria categoria = Categoria.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .build();

        return CategoriaResponse.from(repository.save(categoria));
    }

    @Transactional
    public CategoriaResponse updateCategoria(UUID id, CategoriaRequest request) {
        Categoria categoria = searchCategoria(id);

        if (repository.existsByNombreIgnoreCaseAndIdNot(request.nombre(), id))
            throw new BusinessRuleException(
                    MessageFormat.format("Ya existe categoria con el nombre: {0}", request.nombre()));

        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());

        return CategoriaResponse.from(categoria);
    }

    @Transactional
    public void deleteCategoria(UUID id) {
        Categoria categoria = searchCategoria(id);
        repository.delete(categoria);
    }

    public Categoria getCategoriaEntity(UUID id) {
        return searchCategoria(id);
    }

    private Categoria searchCategoria(UUID id) {
        return repository.findById(id).orElseThrow(() -> ResourceNotFoundException.de("Categoria", id));
    }
}
