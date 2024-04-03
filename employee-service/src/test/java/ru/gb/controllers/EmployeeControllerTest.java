package ru.gb.controllers;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.api.Employee;
import ru.gb.repository.EmployeeRepository;
import ru.gb.service.EmployeeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class EmployeeControllerTest{

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitEmployeeResponse {
        private Long id;
        private String firstName;
        private String middleName;
        private String lastName;
        private LocalDate dateOfBirth;
    }

    @BeforeEach
    void clean() {
        employeeRepository.deleteAll();
    }

    @Test
    void testGetAllEmployeesSuccess() {
        employeeRepository.saveAll(List.of(
                new Employee("Иван", "Иванович", "Иванов", LocalDate.of(1968, 7, 24)),
                new Employee("Петр", "Васильевич", "Пупкин", LocalDate.of(1979, 12, 3)),
                new Employee("Гадя", "Петрович", "Хренова", LocalDate.of(1994, 9, 15))
        ));

        List<Employee> expected = employeeRepository.findAll();

        List<JUnitEmployeeResponse> responseBody = webTestClient.get()
                .uri("api/employee")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitEmployeeResponse>>() {})
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitEmployeeResponse employeeResponse : responseBody) {
            boolean foundFirstName = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), employeeResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getFirstName(), employeeResponse.getFirstName()));
            boolean foundMiddleName = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), employeeResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getMiddleName(), employeeResponse.getMiddleName()));
            boolean foundLastName = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), employeeResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getLastName(), employeeResponse.getLastName()));
            boolean dateOfBirth = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), employeeResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getDateOfBirth(), employeeResponse.getDateOfBirth()));
            Assertions.assertTrue(foundFirstName);
            Assertions.assertTrue(foundMiddleName);
            Assertions.assertTrue(foundLastName);
            Assertions.assertTrue(dateOfBirth);
        }
    }

    @Test
    void testGetAllEmployeesFail() {
        webTestClient.get()
                .uri("api/employee")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testFindEmployeeByIdSuccess() {
        Employee expected = employeeRepository.save(new Employee("Иван", "Иванович", "Иванов", LocalDate.of(1968, 7, 24)));

        JUnitEmployeeResponse responseBody = webTestClient.get()
                .uri("api/employee/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitEmployeeResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getFirstName(), responseBody.getFirstName());
        Assertions.assertEquals(expected.getMiddleName(), responseBody.getMiddleName());
        Assertions.assertEquals(expected.getLastName(), responseBody.getLastName());
        Assertions.assertEquals(expected.getDateOfBirth(), responseBody.getDateOfBirth());
    }

    @Test
    void testFindEmployeeByIdNotFound() {
        employeeRepository.save(new Employee("Иван", "Иванович", "Иванов", LocalDate.of(1968, 7, 24)));

        Long nonExisting = jdbcTemplate.queryForObject("select max(id) from employees", Long.class);
        nonExisting++;

        webTestClient.get()
                .uri("api/employee/" + nonExisting)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testSaveEmployeeSuccess() {
        JUnitEmployeeResponse request = new JUnitEmployeeResponse();
        request.setFirstName("Иван");
        request.setMiddleName("Иванович");
        request.setLastName("Иванов");
        request.setDateOfBirth(LocalDate.of(1968, 7, 24));

        JUnitEmployeeResponse responseBody = webTestClient.post()
                .uri("api/employee")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitEmployeeResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(request.getFirstName(), responseBody.getFirstName());
        Assertions.assertEquals(request.getMiddleName(), responseBody.getMiddleName());
        Assertions.assertEquals(request.getLastName(), responseBody.getLastName());
        Assertions.assertEquals(request.getDateOfBirth(), responseBody.getDateOfBirth());
    }

    @Test
    void testSaveEmployeeFail() {
        webTestClient.post()
                .uri("api/employee")
                .bodyValue(employeeRepository.save(new Employee()))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(IllegalArgumentException.class);
    }

    @Test
    void testDeleteEmployeeSuccess() {
        employeeRepository.saveAll(List.of(
                new Employee("Иван", "Иванович", "Иванов", LocalDate.of(1968, 7, 24)),
                new Employee("Петр", "Васильевич", "Пупкин", LocalDate.of(1979, 12, 3))
        ));

        Long deletedEmployeeId = jdbcTemplate.queryForObject("select max(id) from employees", Long.class);

        webTestClient.delete()
                .uri("api/employee/" + deletedEmployeeId)
                .exchange()
                .expectStatus().isOk();

        Assertions.assertFalse(employeeRepository.existsById(deletedEmployeeId));
    }

    @Test
    void testDeleteEmployeeNotFound() {
        employeeRepository.saveAll(List.of(
                new Employee("Иван", "Иванович", "Иванов", LocalDate.of(1968, 7, 24)),
                new Employee("Петр", "Васильевич", "Пупкин", LocalDate.of(1979, 12, 3))
        ));

        Long nonExistingId = jdbcTemplate.queryForObject("select max(id) from employees", Long.class);
        nonExistingId++;

        webTestClient.delete()
                .uri("api/employee/" + nonExistingId)
                .exchange()
                .expectStatus().isNotFound();
    }

}