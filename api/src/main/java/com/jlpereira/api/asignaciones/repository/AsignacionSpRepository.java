package com.jlpereira.api.asignaciones.repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

@Repository
public class AsignacionSpRepository {

    private final SimpleJdbcCall jdbcCall;

    public AsignacionSpRepository(DataSource dataSource) {
        this.jdbcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("sp_asignar_evento")
                .declareParameters(
                        new SqlParameter("p_evento_id", Types.OTHER),
                        new SqlParameter("p_analista_id", Types.OTHER),
                        new SqlParameter("p_usuario", Types.VARCHAR),
                        new SqlOutParameter("p_asignacion_id", Types.OTHER));
    }

    public UUID asignar(UUID eventoId, UUID analistaId, String usuario) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_evento_id", eventoId);
        params.put("p_analista_id", analistaId);
        params.put("p_usuario", usuario);

        Map<String, Object> resultado = jdbcCall.execute(params);
        return (UUID) resultado.get("p_asignacion_id");
    }
}
