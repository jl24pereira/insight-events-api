package com.jlpereira.api.analistas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Jose Luis Pereira
 */
public record AnalistaRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 150, message = "El nombre no puede exceder 150 caracteres") String nombre,

        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no tiene un formato válido") @Size(max = 180, message = "El correo no puede exceder 180 caracteres") String correo) {
    public String correoNormalizado() {
        return correo == null ? null : correo.trim().toLowerCase();
    }
}
