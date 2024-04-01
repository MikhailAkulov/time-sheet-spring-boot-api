package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.api.WorkShift;
import ru.gb.controllers.WorkShiftRequest;
import ru.gb.repository.WorkShiftRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;

    public List<WorkShift> showAllWorkShifts() {
        return workShiftRepository.findAll();

//        List<WorkShift> workShifts = workShiftRepository.findAll();
//        if (workShifts.isEmpty()) {
//            throw new NoSuchElementException("Список отработанных смен пуст");
//        }
//        return workShifts;
    }

    public List<WorkShift> showAllWorkShiftsByEmployee(long id) {
        List<WorkShift> employeeWorkShifts = workShiftRepository.findAll().stream()
                .filter(it -> it.getEmployeeId() == id).toList();
        if (employeeWorkShifts.isEmpty()) {
            throw new NoSuchElementException("Не найдены рабочие смены сотрудника с id: \"" + id + "\"");
        }
        return employeeWorkShifts;
    }

    public WorkShift addNewWorkShift(WorkShiftRequest request) {
        if (workShiftRepository.findWorkShiftByEmployeeId(request.getEmployeeId()) != null &&
                workShiftRepository.findWorkShiftByWorkShiftDate(request.getWorkShiftDate()) != null) {
            throw new IllegalArgumentException("Данная рабочая смена сотрудника уже внесена");
        }

        WorkShift workShift = new WorkShift(request.getEmployeeId(), request.getWorkShiftDate(), request.getWorkHours(),
                request.getNote());
        workShiftRepository.save(workShift);
        return workShift;
    }

    public WorkShift showWorkShiftInfoById(long id) {
        return workShiftRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("Не найдена рабочая смена с id: \"" + id + "\""));
    }

    public WorkShift deleteWorkShiftById(long id) {
        WorkShift workShift = showWorkShiftInfoById(id);
        if (workShift == null) {
            throw new NoSuchElementException("Не найдена рабочая смена с id: \"" + id + "\"");
        }
        workShiftRepository.deleteById(id);
        return workShift;
    }

}
