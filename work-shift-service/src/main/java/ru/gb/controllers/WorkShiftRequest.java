package ru.gb.controllers;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkShiftRequest {

    private Long employeeId;
    private LocalDate workShiftDate;
    private Integer workHours;
    private String note;

}
