// com.example.moviesphere.exception.ResourceNotFoundException.java
package com.example.moviesphere.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Tells Spring to automatically return a 404 status when this exception is thrown
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}