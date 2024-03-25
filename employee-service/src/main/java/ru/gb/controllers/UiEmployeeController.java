package ru.gb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.service.EmployeeService;

@Controller
@RequestMapping("/ui")
@Tag(name = "UI")
public class UiEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    @Operation(
            summary = "get list of all employees",
            description = "Загружает страницу со списком сотрудников, внесённых в систему"
    )
    public String getEmployees(Model model) {
        model.addAttribute("employees", employeeService.showAllEmployees());
        return "employees";
    }
}
