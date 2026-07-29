package com.jlpereira.api.shared.exception;

/**
 *
 * @author Jose Luis Pereira
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException de(String resource, Object id) {
        return new ResourceNotFoundException("%s no encontrado: %s".formatted(resource, id));
    }

}
