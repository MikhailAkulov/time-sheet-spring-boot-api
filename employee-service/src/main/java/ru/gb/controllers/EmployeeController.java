package ru.gb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.gb.api.Employee;
import ru.gb.service.EmployeeService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/employee")
@Tag(name = "Сотрудники", description = "Управление сервисом сотрудников в системе")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping()
    @Operation(summary = "get all employees",
            description = "Загружает список сотрудников, зарегистрированных в системе",
            responses = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка сотрудников", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ExceptionMapper.class)))
            })
//            ,@ApiResponse(responseCode = "404", description = "Список сотрудников пуст", content = {
//                    @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
//            })
    })
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Получен запрос актуального списка сотрудников");

        return new ResponseEntity<>(employeeService.showAllEmployees(), HttpStatus.OK);

//        final List<Employee> employees;
//        try {
//            employees = employeeService.showAllEmployees();
//        } catch (NoSuchElementException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get info about employee",
            description = "Загружает информацию о запрашиваемом сотруднике",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор сотрудника")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение сотрудника", content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = Employee.class)
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "Сотрудник не найден в системе", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
                    })
            }
    )
    public ResponseEntity<Employee> getEmployeeInfoById(@PathVariable long id) {
        log.info("Получен запрос информации о сотруднике: Id = {}", id);

        final Employee employee;
        try {
            employee = employeeService.showEmployeeInfo(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete employee",
            description = "Удаляет сотрудника из системы по Id",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор сотрудника")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сотрудник успешно удалён из системы", content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = Employee.class)
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "Сотрудник не найден в системе", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
                    })
            }
    )
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable long id) {
        log.info("Получен запрос на удаление сотрудника: Id = {}", id);

        final Employee employee;
        try {
            employee = employeeService.deleteEmployee(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @PostMapping()
    @Operation(
            summary = "add new employee",
            description = "Добавляет нового сотрудника в систему",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Сотрудник успешно добавлен в систему", content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = Employee.class)
                            )
                    }),
                    @ApiResponse(responseCode = "405", description = "Добавление сотрудника не выполнено", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
                    })
            }
    )
    public ResponseEntity<Employee> addNewEmployee(@RequestBody EmployeeRequest request) {
        log.info("Получен запрос на добавление сотрудника: firstName = {} middleName = {} lastName = {}",
                request.getFirstName(), request.getMiddleName(), request.getLastName());

        final Employee employee;
        try {
            employee = employeeService.addNewEmployee(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

}
