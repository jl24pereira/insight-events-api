package com.jlpereira.api.shared.exception;

import java.text.MessageFormat;

/**
 *
 * @author Jose Luis Pereira
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException de(String resource, Object id) {
        return new ResourceNotFoundException(MessageFormat.format("{0} no encontrado: {1}", resource, id));
    }

}
