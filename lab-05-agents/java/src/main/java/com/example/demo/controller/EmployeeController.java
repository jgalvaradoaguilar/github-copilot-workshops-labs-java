package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Entrada: GET /api/employees");
        List<Employee> employees = employeeService.getAllEmployees();
        logger.info("Salida: Retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        logger.info("Entrada: GET /api/employees/{}", id);
        Employee employee = employeeService.getEmployeeById(id);
        logger.info("Salida: {}", employee);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        logger.info("Entrada: GET /api/employees/email/{}", email);
        Employee employee = employeeService.findEmployeeByEmail(email);
        logger.info("Salida: {}", employee);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        logger.info("Entrada: POST /api/employees");
        Employee createdEmployee = employeeService.saveEmployee(employee);
        logger.info("Salida: {}", createdEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        logger.info("Entrada: PUT /api/employees/{}", id);
        Employee existingEmployee = employeeService.getEmployeeById(id);
        existingEmployee.setName(employee.getName());
        existingEmployee.setSurname(employee.getSurname());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPosition(employee.getPosition());
        existingEmployee.setDepartment(employee.getDepartment());
        Employee updatedEmployee = employeeService.saveEmployee(existingEmployee);
        logger.info("Salida: {}", updatedEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Entrada: DELETE /api/employees/{}", id);
        employeeService.deleteEmployee(id);
        logger.info("Salida: void");
        return ResponseEntity.noContent().build();
    }
}