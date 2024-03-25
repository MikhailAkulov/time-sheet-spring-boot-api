package ru.gb.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "workShift")
@Data
@NoArgsConstructor
@Schema(name = "Рабочая смена")
public class WorkShift {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Идентификатор рабочей смены")
    private Long id;

    @Column(name = "employee_id")
    @Schema(description = "Идентификатор сотрудника")
    private Long employeeId;

    @Column(name = "work_shift_date")
    @Schema(description = "Дата рабочей смены")
    private LocalDate workShiftDate;

    @Column(name = "work_hours")
    @Schema(description = "Количество отработанных сотрудником часов за смену")
    private Integer workHours;

    @Column(name = "note")
    @Schema(description = "Примечание (больничный, отпуск, отгул и т.п.)")
    private String note;

    public WorkShift(long employeeId, LocalDate workShiftDate, int workHours, String note) {
        this.employeeId = employeeId;
        this.workShiftDate = workShiftDate;
        this.workHours = workHours;
        this.note = note;
    }

}
