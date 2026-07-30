package com.jlpereira.api.asignaciones.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AsignacionSpRepository {

    private final JdbcTemplate jdbcTemplate;

    public AsignacionSpRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID asignar(UUID eventoId, UUID analistaId, String usuario) {
        return jdbcTemplate.execute((Connection conn) -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "CALL sp_asignar_evento(?, ?, ?, NULL)")) {

                ps.setObject(1, eventoId, Types.OTHER);
                ps.setObject(2, analistaId, Types.OTHER);
                ps.setString(3, usuario);

                boolean tieneResultado = ps.execute();
                if (tieneResultado) {
                    try (ResultSet rs = ps.getResultSet()) {
                        if (rs.next()) {
                            return (UUID) rs.getObject(1);
                        }
                    }
                }
                throw new IllegalStateException(
                        "El procedimiento no devolvió ningún resultado");
            }
        });
    }
}
