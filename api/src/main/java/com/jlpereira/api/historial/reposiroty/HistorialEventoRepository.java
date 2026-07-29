package com.jlpereira.api.historial.reposiroty;

import java.util.UUID;

import com.jlpereira.api.historial.domain.HistorialEvento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialEventoRepository extends JpaRepository<HistorialEvento, UUID> {

    @Query("""
            select h
            from HistorialEvento h
            join fetch h.evento e
            join fetch e.categoria
            where e.id = :eventoId
            order by h.fecha desc, h.id desc
            """)
    List<HistorialEvento> buscarHistorialCompleto(@Param("eventoId") UUID eventoId);
}
