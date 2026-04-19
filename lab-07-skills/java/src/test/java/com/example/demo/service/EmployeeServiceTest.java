package com.example.demo.service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.exception.EmployeeServiceException;
import com.example.demo.exception.InvalidEmployeeException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employeeService = new EmployeeService(employeeRepository);
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
    }

    @Test
    public void testGetAllEmployees() {
        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Jane Doe");
        employee2.setEmail("jane.doe@example.com");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee, employee2));

        List<Employee> employees = employeeService.getAllEmployees();
        assertThat(employees).hasSize(2).extracting(Employee::getName).contains(employee.getName(),
                employee2.getName());
    }

    @Test
    public void testGetEmployeeById() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.getEmployeeById(employee.getId());
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getName()).isEqualTo(employee.getName());
    }

    @Test
    public void testSaveEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
    }

    @Test
    public void testDeleteEmployee() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(employee.getId());

        employeeService.deleteEmployee(employee.getId());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }

    @Test
    public void testFindEmployeeByEmail() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findEmployeeByEmail(employee.getEmail());
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getEmail()).isEqualTo(employee.getEmail());
    }

    // Edge case tests for enhanced coverage

    @Test
    public void testGetAllEmployeesEmptyList() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList());

        List<Employee> employees = employeeService.getAllEmployees();
        assertThat(employees).isEmpty();
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(999L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testGetEmployeeByIdInvalid() {
        assertThatThrownBy(() -> employeeService.getEmployeeById(-1L))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("positive");
    }

    @Test
    public void testGetEmployeeByIdNull() {
        assertThatThrownBy(() -> employeeService.getEmployeeById(null))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("positive");
    }

    @Test
    public void testFindEmployeeByEmailNotFound() {
        when(employeeRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findEmployeeByEmail("nonexistent@example.com"))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testFindEmployeeByEmailNull() {
        assertThatThrownBy(() -> employeeService.findEmployeeByEmail(null))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("null or empty");
    }

    @Test
    public void testFindEmployeeByEmailEmpty() {
        assertThatThrownBy(() -> employeeService.findEmployeeByEmail(""))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("null or empty");
    }

    @Test
    public void testSaveEmployeeNull() {
        assertThatThrownBy(() -> employeeService.saveEmployee(null))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("null");
    }

    @Test
    public void testSaveEmployeeNullEmail() {
        employee.setEmail(null);
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("email");
    }

    @Test
    public void testSaveEmployeeEmptyEmail() {
        employee.setEmail("");
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("email");
    }

    @Test
    public void testSaveEmployeeNullName() {
        employee.setName(null);
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("name");
    }

    @Test
    public void testSaveEmployeeEmptyName() {
        employee.setName("");
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("name");
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(999L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testDeleteEmployeeInvalidId() {
        assertThatThrownBy(() -> employeeService.deleteEmployee(-1L))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("positive");
    }

    @Test
    public void testDeleteEmployeeNullId() {
        assertThatThrownBy(() -> employeeService.deleteEmployee(null))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("positive");
    }

    // Repository exception handling tests

    @Test
    public void testGetAllEmployeesRepositoryException() {
        when(employeeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> employeeService.getAllEmployees())
                .isInstanceOf(EmployeeServiceException.class)
                .hasMessageContaining("Failed to retrieve employees");
    }

    @Test
    public void testGetEmployeeByIdRepositoryException() {
        when(employeeRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(EmployeeServiceException.class)
                .hasMessageContaining("Failed to retrieve employee");
    }

    @Test
    public void testSaveEmployeeRepositoryException() {
        when(employeeRepository.save(employee)).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(EmployeeServiceException.class)
                .hasMessageContaining("Failed to save employee");
    }

    @Test
    public void testDeleteEmployeeRepositoryException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doThrow(new RuntimeException("Database error")).when(employeeRepository).deleteById(1L);

        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(EmployeeServiceException.class)
                .hasMessageContaining("Failed to delete employee");
    }

    @Test
    public void testFindEmployeeByEmailRepositoryException() {
        when(employeeRepository.findByEmail(employee.getEmail()))
                .thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> employeeService.findEmployeeByEmail(employee.getEmail()))
                .isInstanceOf(EmployeeServiceException.class)
                .hasMessageContaining("Failed to find employee");
    }

    // Whitespace and trim tests for email and name validation

    @Test
    public void testSaveEmployeeWhitespaceOnlyEmail() {
        employee.setEmail("   ");
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("email");
    }

    @Test
    public void testSaveEmployeeWhitespaceOnlyName() {
        employee.setName("   ");
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("name");
    }

    @Test
    public void testFindEmployeeByEmailWhitespaceOnly() {
        assertThatThrownBy(() -> employeeService.findEmployeeByEmail("   "))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("null or empty");
    }

    // Boundary value tests

    @Test
    public void testGetEmployeeByIdZero() {
        assertThatThrownBy(() -> employeeService.getEmployeeById(0L))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("positive");
    }

    @Test
    public void testDeleteEmployeeByIdZero() {
        assertThatThrownBy(() -> employeeService.deleteEmployee(0L))
                .isInstanceOf(InvalidEmployeeException.class)
                .hasMessageContaining("positive");
    }

    @Test
    public void testGetEmployeeByIdPositiveNumber() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1L);
        assertThat(result).isEqualTo(employee);
    }

    @Test
    public void testGetEmployeeByIdLargeNumber() {
        Employee largeIdEmployee = new Employee();
        largeIdEmployee.setId(Long.MAX_VALUE);
        largeIdEmployee.setName("Test");
        largeIdEmployee.setEmail("test@example.com");

        when(employeeRepository.findById(Long.MAX_VALUE)).thenReturn(Optional.of(largeIdEmployee));

        Employee result = employeeService.getEmployeeById(Long.MAX_VALUE);
        assertThat(result.getId()).isEqualTo(Long.MAX_VALUE);
    }

    // Test for successful save with various scenarios

    @Test
    public void testSaveEmployeeWithAllFields() {
        employee.setSurname("TestSurname");
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo(employee.getName());
        assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());
        assertThat(savedEmployee.getSurname()).isEqualTo(employee.getSurname());
    }

    // Verify repository interactions

    @Test
    public void testGetAllEmployeesInvokeRepository() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList());
        employeeService.getAllEmployees();
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testFindEmployeeByEmailInvokeRepository() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        employeeService.findEmployeeByEmail(employee.getEmail());
        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
    }

    @Test
    public void testGetRepositoryInteractions() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail("test@test.com")).thenReturn(Optional.of(employee));

        employeeService.getAllEmployees();
        employeeService.getEmployeeById(1L);
        employeeService.findEmployeeByEmail("test@test.com");

        verify(employeeRepository, times(1)).findAll();
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findByEmail("test@test.com");
    }
}