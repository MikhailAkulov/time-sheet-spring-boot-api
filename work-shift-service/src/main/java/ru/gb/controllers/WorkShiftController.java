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
import ru.gb.api.WorkShift;
import ru.gb.service.WorkShiftService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/workshift")
@Tag(name = "Рабочие смены", description = "Управление сервисом рабочих смен сотрудников")
public class WorkShiftController {

    private final WorkShiftService workShiftService;

    @GetMapping()
    @Operation(summary = "get all work shifts",
            description = "Загружает список отработанных сотрудниками смен",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка смен", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ExceptionMapper.class)))
                    }),
                    @ApiResponse(responseCode = "404", description = "Список смен пуст", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
                    })
            })
    public ResponseEntity<List<WorkShift>> getAllWorkShifts() {
        log.info("Получен запрос актуального списка рабочих смен сотрудников");

        final List<WorkShift> workShifts;
        try {
            workShifts = workShiftService.showAllWorkShifts();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(workShifts);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get info about work shift",
            description = "Загружает информацию о запрашиваемой рабочей смене",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор рабочей смены")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение рабочей смены", content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = Employee.class)
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "Рабочая смена не найдена в системе", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
                    })
            }
    )
    public ResponseEntity<WorkShift> getWorkShiftInfoById(@PathVariable long id) {
        log.info("Получен запрос информации о рабочей смене: Id = {}", id);

        final WorkShift workShift;
        try {
            workShift = workShiftService.showWorkShiftInfoById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(workShift);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete work shift",
            description = "Удаляет рабочую смену из системы по Id",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор рабочей смены")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Смена успешно удалена из системы", content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = Employee.class)
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "Смена не найдена в системе", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
                    })
            }
    )
    public ResponseEntity<WorkShift> deleteWorkShiftById(@PathVariable long id) {
        log.info("Получен запрос на удаление рабочей смены: Id = {}", id);

        final WorkShift workShift;
        try {
            workShift = workShiftService.deleteWorkShiftById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(workShift);
    }

    @PostMapping()
    @Operation(
            summary = "add new work shift",
            description = "Добавляет новую рабочую смену сотрудника в систему",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Смена успешно добавлена в систему", content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = Employee.class)
                            )
                    }),
                    @ApiResponse(responseCode = "405", description = "Добавление смены не выполнено", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))
                    })
            }
    )
    public ResponseEntity<WorkShift> addNewWorkShift(@RequestBody WorkShiftRequest request) {
        log.info("Получен запрос на добавление рабочей смены сотрудника: employeeId = {} workShiftDate = {} " +
                "workHours = {} note = {}", request.getEmployeeId(), request.getWorkShiftDate(), request.getWorkHours(),
                request.getNote());

        final WorkShift workShift;
        try {
            workShift = workShiftService.addNewWorkShift(request);
        } catch (NullPointerException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(workShift);
    }

    @GetMapping("/employee/{id}")
    @Operation(summary = "get all work shifts by employee", description = "Загружает список рабочих смен сотрудника")
    public ResponseEntity<List<WorkShift>> getEmployeeWorkShift(@PathVariable long id) {
        log.info("Получен запрос информации о рабочих сменах сотрудника с id = {}", id);

        final List<WorkShift> employeeWorkShifts;
        try {
            employeeWorkShifts = workShiftService.showAllWorkShiftsByEmployee(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeWorkShifts);
    }

}
