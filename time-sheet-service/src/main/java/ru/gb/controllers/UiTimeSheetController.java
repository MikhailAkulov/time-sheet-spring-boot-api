package ru.gb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
@Tag(name = "UI")
public class UiTimeSheetController {

    @Autowired
    private TimeSheetController timeSheetController;

    @GetMapping("/timesheets")
    @Operation(
            summary = "get list of all employee time sheets",
            description = "Загружает страницу со списком табелей сотрудников, внесённых в систему"
    )
    public String getEmployeeTimeSheets(Model model) {
        model.addAttribute("timesheets", timeSheetController.getAll());
        return "timesheets";
    }

}
