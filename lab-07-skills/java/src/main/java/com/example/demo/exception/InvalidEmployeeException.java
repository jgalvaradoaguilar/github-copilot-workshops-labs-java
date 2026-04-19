package com.example.demo.exception;

/**
 * Exception thrown when employee data is invalid.
 */
public class InvalidEmployeeException extends RuntimeException {
    
    public InvalidEmployeeException(String message) {
        super(message);
    }
    
    public InvalidEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
