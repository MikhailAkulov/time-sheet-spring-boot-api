package ru.gb.api;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TimeSheet {

    private UUID id;
    private Employee employee;
    private List<WorkShift> workShift;

}
