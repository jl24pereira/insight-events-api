package com.jlpereira.api.shared.dto;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO
 *
 * @author Jose Luis Pereira
 */
public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String path,
        List<ErrorCampo> errores) {
    public record ErrorCampo(
            String campo,
            String descripcion) {
    }

    public static ErrorResponse of(int status, String error, String mensaje, String path) {
        return new ErrorResponse(OffsetDateTime.now(), status, error, mensaje, path, List.of());
    }
}
