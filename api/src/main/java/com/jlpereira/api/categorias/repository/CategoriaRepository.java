package com.jlpereira.api.categorias.repository;

import java.util.UUID;

import com.jlpereira.api.categorias.domain.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Jose Luis Pereira
 */
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, UUID id);
}
