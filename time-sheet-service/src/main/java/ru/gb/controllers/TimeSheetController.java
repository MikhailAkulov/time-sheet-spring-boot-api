package ru.gb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.api.TimeSheet;
import ru.gb.providers.EmployeeProvider;
import ru.gb.providers.WorkShiftProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/timesheet")
public class TimeSheetController {

    private final EmployeeProvider employeeProvider;
    private final WorkShiftProvider workShiftProvider;
    private final List<TimeSheet> timeSheets;

    public TimeSheetController(EmployeeProvider employeeProvider, WorkShiftProvider workShiftProvider) {
        this.employeeProvider = employeeProvider;
        this.workShiftProvider = workShiftProvider;
        this.timeSheets = new ArrayList<>();
        dataGenerate();
    }

    @GetMapping
    public List<TimeSheet> getAll() {
        return timeSheets;
    }

    /**
     * Генерация данных для проверки работоспособности
     */
    private void dataGenerate() {
        for (long i = 1; i < 5; i++) {
            TimeSheet timeSheet = new TimeSheet();
            timeSheet.setId(UUID.randomUUID());
            timeSheet.setEmployee(employeeProvider.getEmployeeById(i));
            timeSheet.setWorkShift(workShiftProvider.getWorkShiftByEmployee(i));

            timeSheets.add(timeSheet);
        }
    }
}
