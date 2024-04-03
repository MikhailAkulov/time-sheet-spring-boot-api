package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.api.Employee;
import ru.gb.controllers.EmployeeRequest;
import ru.gb.repository.EmployeeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> showAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new NoSuchElementException("Список сотрудников пуст");
        }
        return employees;
    }

    public Employee addNewEmployee(EmployeeRequest request) {
        if (employeeRepository.findEmployeeByLastName(request.getLastName()) != null &&
                employeeRepository.findEmployeeByFirstName(request.getFirstName()) != null &&
                employeeRepository.findEmployeeByMiddleName(request.getMiddleName()) != null &&
                employeeRepository.findEmployeeByDateOfBirth(request.getDateOfBirth()) != null) {
            throw new IllegalArgumentException("Данный сотрудник уже существует");
        }
        Employee employee = new Employee(request.getFirstName(), request.getMiddleName(), request.getLastName(),
                request.getDateOfBirth());
        employeeRepository.save(employee);
        return employee;
    }

    public Employee showEmployeeInfo(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найден сотрудник с id: \"" + id + "\""));
    }

    public Employee deleteEmployee(long id) {
        Employee employee = showEmployeeInfo(id);
        if (employee == null) {
            throw new NoSuchElementException("Не найден сотрудник с id: \"" + id + "\"");
        }
        employeeRepository.deleteById(id);
        return employee;
    }

}
