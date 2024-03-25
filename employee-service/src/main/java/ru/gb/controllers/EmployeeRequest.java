package ru.gb.controllers;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRequest {

    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;

}
