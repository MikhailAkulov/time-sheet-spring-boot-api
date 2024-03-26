//package ru.gb;
//
//import com.github.javafaker.Faker;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//import ru.gb.api.Employee;
//import ru.gb.repository.EmployeeRepository;
//
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class TestEmployeeDataGenerator {
//
//    private final EmployeeRepository employeeRepository;
//
//
//    public TestEmployeeDataGenerator(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//    /**
//     * Генерация списка сотрудников для проверки
//     */
//    @PostConstruct
//    void generateData() {
//        Faker faker = new Faker();
//        List<Employee> employeeList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            employeeList.add(new Employee(faker.name().firstName(), faker.name().firstName(), faker.name().lastName(),
//                    faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
//        }
//        employeeRepository.saveAll(employeeList);
//    }
//
//}
