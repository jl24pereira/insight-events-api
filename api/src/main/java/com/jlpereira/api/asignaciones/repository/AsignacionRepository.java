package com.jlpereira.api.asignaciones.repository;

import java.util.List;
import java.util.UUID;

import com.jlpereira.api.asignaciones.domain.Asignacion;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, UUID> {

    @EntityGraph(attributePaths = { "evento", "analista" })
    List<Asignacion> findByAnalistaId(UUID analistaId);

}
