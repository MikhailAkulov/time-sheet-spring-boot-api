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
import ru.gb.api.WorkShift;
import ru.gb.repository.WorkShiftRepository;
import ru.gb.service.WorkShiftService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WorkShiftControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    WorkShiftRepository workShiftRepository;
    @Autowired
    WorkShiftService workShiftService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitWorkShiftResponse {
        private Long id;
        private Long employeeId;
        private LocalDate workShiftDate;
        private Integer workHours;
        private String note;
    }

    @BeforeEach
    void clean() {
        workShiftRepository.deleteAll();
    }

    @Test
    void testGetAllWorkShiftsSuccess() {
        workShiftRepository.saveAll(List.of(
                new WorkShift(1L, LocalDate.of(2024, 3, 1), 8, "ordinary"),
                new WorkShift(2L, LocalDate.of(2024, 3, 1), 6, "ordinary"),
                new WorkShift(3L, LocalDate.of(2024, 3, 1), 4, "ordinary")
        ));

        List<WorkShift> expected = workShiftRepository.findAll();

        List<JUnitWorkShiftResponse> responseBody = webTestClient.get()
                .uri("api/wirkshift")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitWorkShiftResponse>>() {})
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitWorkShiftResponse workShiftResponse : responseBody) {
            boolean foundEmployeeId = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), workShiftResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getEmployeeId(), workShiftResponse.getEmployeeId()));
            boolean foundWorkShiftDate = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), workShiftResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getWorkShiftDate(), workShiftResponse.getWorkShiftDate()));
            boolean foundWorkHours = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), workShiftResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getWorkHours(), workShiftResponse.getWorkHours()));
            boolean foundNote = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), workShiftResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getNote(), workShiftResponse.getNote()));

            Assertions.assertTrue(foundEmployeeId);
            Assertions.assertTrue(foundWorkShiftDate);
            Assertions.assertTrue(foundWorkHours);
            Assertions.assertTrue(foundNote);
        }
    }

    @Test
    void testGetAllWorkShiftsFail() {
        webTestClient.get()
                .uri("api/wirkshift")
                .exchange()
                .expectStatus().isNotFound();
    }

}