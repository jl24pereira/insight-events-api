package com.jlpereira.api.eventos.repository;

import java.util.Optional;
import java.util.UUID;

import com.jlpereira.api.eventos.domain.Evento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {

    @Override
    @EntityGraph(attributePaths = "categoria")
    Page<Evento> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "categoria")
    Optional<Evento> findWithCategoriaById(UUID id);

    boolean existByCategoriaId(UUID categoriaId);

}
