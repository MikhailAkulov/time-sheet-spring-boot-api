package ru.gb.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@Schema(description = "Сотрудник")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Идентификатор")
    private Long id;

    @Column(name = "firstname")
    @Schema(description = "Имя")
    private String firstName;

    @Column(name = "middlename")
    @Schema(description = "Отчество")
    private String middleName;

    @Column(name = "lastname")
    @Schema(description = "Фамилия")
    private String lastName;

    @Column(name = "date_of_birth")
    @Schema(description = "Дата рождения")
    private LocalDate dateOfBirth;

    public Employee(String firstName, String middleName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

}
