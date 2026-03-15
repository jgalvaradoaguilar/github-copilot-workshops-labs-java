package com.example.demo.exception;

/**
 * General exception for employee service operations.
 */
public class EmployeeServiceException extends RuntimeException {
    
    public EmployeeServiceException(String message) {
        super(message);
    }
    
    public EmployeeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
