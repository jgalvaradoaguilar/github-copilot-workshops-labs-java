package com.example.demo.service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.exception.EmployeeServiceException;
import com.example.demo.exception.InvalidEmployeeException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EmployeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving all employees", e);
            throw new EmployeeServiceException("Failed to retrieve employees", e);
        }
    }

    public Employee getEmployeeById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidEmployeeException("Employee ID must be a positive number");
        }
        try {
            return employeeRepository.findById(id)
                    .orElseThrow(() -> new EmployeeNotFoundException(id));
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving employee by ID: {}", id, e);
            throw new EmployeeServiceException("Failed to retrieve employee with ID: " + id, e);
        }
    }

    public Employee saveEmployee(Employee employee) {
        if (employee == null) {
            throw new InvalidEmployeeException("Employee object cannot be null");
        }
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee email is required");
        }
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee name is required");
        }
        try {
            return employeeRepository.save(employee);
        } catch (Exception e) {
            logger.error("Error saving employee: {}", employee, e);
            throw new EmployeeServiceException("Failed to save employee", e);
        }
    }

    public void deleteEmployee(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidEmployeeException("Employee ID must be a positive number");
        }
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new EmployeeNotFoundException(id));
            employeeRepository.deleteById(id);
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting employee with ID: {}", id, e);
            throw new EmployeeServiceException("Failed to delete employee with ID: " + id, e);
        }
    }

    public List<Employee> searchEmployees(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllEmployees();
        }
        try {
            logger.info("Searching employees with term: {}", searchTerm);
            return employeeRepository.searchByNameOrEmail(searchTerm);
        } catch (Exception e) {
            logger.error("Error searching employees with term: {}", searchTerm, e);
            throw new EmployeeServiceException("Failed to search employees", e);
        }
    }
}
