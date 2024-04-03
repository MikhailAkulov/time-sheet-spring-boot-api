package ru.gb.controllers;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.api.WorkShift;
import ru.gb.repository.WorkShiftRepository;
import ru.gb.service.WorkShiftService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WorkShiftControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    WorkShiftRepository workShiftRepository;
    @Autowired
    private WorkShiftService workShiftService;
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
                .uri("api/workshift")
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
                .uri("api/workshift")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testFindWorkShiftByIdSuccess() {
        WorkShift expected = workShiftRepository.save(
                new WorkShift(1L, LocalDate.of(2024, 3, 1), 8, "ordinary")
        );

        JUnitWorkShiftResponse responseBody = webTestClient.get()
                .uri("api/workshift/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitWorkShiftResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getEmployeeId(), responseBody.getEmployeeId());
        Assertions.assertEquals(expected.getWorkShiftDate(), responseBody.getWorkShiftDate());
        Assertions.assertEquals(expected.getWorkHours(), responseBody.getWorkHours());
        Assertions.assertEquals(expected.getNote(), responseBody.getNote());
    }

    @Test
    void testFindWorkShiftByIdNotFound() {
        workShiftRepository.save(
                new WorkShift(1L, LocalDate.of(2024, 3, 1), 8, "none")
        );

        Optional<Long> max = workShiftRepository.findAll()
                .stream().map(WorkShift::getId)
                .max(Comparator.naturalOrder());

        long nonExistingId = max.orElse(1L);
        nonExistingId++;

        webTestClient.get()
                .uri("api/workshift/" + nonExistingId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testSaveWorkShiftSuccess() {
        JUnitWorkShiftResponse request = new JUnitWorkShiftResponse();
        request.setEmployeeId(1L);
        request.setWorkShiftDate(LocalDate.of(2024, 3, 1));
        request.setWorkHours(8);
        request.setNote("ordinary");

        JUnitWorkShiftResponse responseBody = webTestClient.post()
                .uri("api/workshift")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitWorkShiftResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(request.getEmployeeId(), responseBody.getEmployeeId());
        Assertions.assertEquals(request.getWorkShiftDate(), responseBody.getWorkShiftDate());
        Assertions.assertEquals(request.getWorkHours(), responseBody.getWorkHours());
        Assertions.assertEquals(request.getNote(), responseBody.getNote());
    }

    @Test
    void testSaveWorkShiftFail() {
        webTestClient.post()
                .uri("api/workshift")
                .bodyValue(workShiftRepository.save(new WorkShift()))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(NullPointerException.class);
    }

    @Test
    void testDeleteWorkShiftSuccess() {
        workShiftRepository.saveAll(List.of(
                new WorkShift(1L, LocalDate.of(2024, 3, 1), 8, "ordinary"),
                new WorkShift(2L, LocalDate.of(2024, 3, 1), 6, "ordinary")
        ));

        Optional<Long> max = workShiftRepository.findAll()
                .stream().map(WorkShift::getId)
                .max(Comparator.naturalOrder());

        long deletedWorkShiftId = max.orElse(1L);

        webTestClient.delete()
                .uri("api/workshift/" + deletedWorkShiftId)
                .exchange()
                .expectStatus().isOk();

        Assertions.assertFalse(workShiftRepository.existsById(deletedWorkShiftId));
    }

    @Test
    void testDeleteWorkShiftNotFound() {
        workShiftRepository.saveAll(List.of(
                new WorkShift(1L, LocalDate.of(2024, 3, 1), 8, "ordinary"),
                new WorkShift(2L, LocalDate.of(2024, 3, 1), 6, "ordinary")
        ));

        Optional<Long> max = workShiftRepository.findAll()
                .stream().map(WorkShift::getId)
                .max(Comparator.naturalOrder());

        long nonExistingId = max.orElse(1L);
        nonExistingId++;

        webTestClient.delete()
                .uri("api/workshift/" + nonExistingId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAllWorkShiftsByEmployeeIdSuccess() {
        workShiftRepository.saveAll(List.of(
                new WorkShift(1L, LocalDate.of(2024, 3, 1), 8, "ordinary"),
                new WorkShift(1L, LocalDate.of(2024, 3, 2), 6, "none"),
                new WorkShift(2L, LocalDate.of(2024, 3, 2), 8, "ordinary"),
                new WorkShift(2L, LocalDate.of(2024, 3, 3), 4, "none"),
                new WorkShift(3L, LocalDate.of(2024, 3, 4), 6, "ordinary")
        ));

        long expectedEmployeeId = 2L;
        List<WorkShift> expectedEmployeeWorkShifts = workShiftService.showAllWorkShiftsByEmployee(expectedEmployeeId);

        List<JUnitWorkShiftResponse> responseBody = webTestClient.get()
                .uri("api/workshift/employee/" + expectedEmployeeId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitWorkShiftResponse>>() {})
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expectedEmployeeWorkShifts.size(), responseBody.size());
        for (JUnitWorkShiftResponse workShiftResponse : responseBody) {
            boolean foundId = expectedEmployeeWorkShifts.stream()
                    .filter(it -> Objects.equals(it.getId(), workShiftResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getEmployeeId(), workShiftResponse.getEmployeeId()));
            Assertions.assertTrue(foundId);
        }
    }

    @Test
    void testGetAllWorkShiftsByEmployeeIdNotFound() {
        workShiftRepository.saveAll(List.of(
                new WorkShift(1L, LocalDate.of(2024, 3, 1), 8, "ordinary"),
                new WorkShift(2L, LocalDate.of(2024, 3, 2), 8, "ordinary"),
                new WorkShift(3L, LocalDate.of(2024, 3, 4), 6, "ordinary")
        ));

        Optional<Long> max = workShiftRepository.findAll()
                .stream().map(WorkShift::getId)
                .max(Comparator.naturalOrder());

        long nonExistingId = max.orElse(1L);
        nonExistingId++;

        webTestClient.get()
                .uri("api/workshift/employee/" + nonExistingId)
                .exchange()
                .expectStatus().isNotFound();
    }

}
