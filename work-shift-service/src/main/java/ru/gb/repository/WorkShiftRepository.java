package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.api.WorkShift;

import java.time.LocalDate;

@Repository
public interface WorkShiftRepository extends JpaRepository<WorkShift, Long> {

    WorkShift findWorkShiftByEmployeeId(long employeeId);
    WorkShift findWorkShiftByWorkShiftDate(LocalDate workShiftDate);

}
