package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.api.Employee;

import java.time.LocalDate;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findEmployeeByFirstName(String firstName);
    Employee findEmployeeByMiddleName(String middleName);
    Employee findEmployeeByLastName(String lastName);
    Employee findEmployeeByDateOfBirth(LocalDate dateOfBirth);

}

