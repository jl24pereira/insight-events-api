package com.jlpereira.api.analistas.repository;

import java.util.UUID;

import com.jlpereira.api.analistas.domain.Analista;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalistaRepository extends JpaRepository<Analista, UUID> {

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdNot(String correo, UUID id);
}
