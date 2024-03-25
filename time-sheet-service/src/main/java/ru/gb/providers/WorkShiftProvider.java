package ru.gb.providers;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.api.WorkShift;

import java.util.List;

@Service
public class WorkShiftProvider {

    private final WebClient webClient;

    public WorkShiftProvider(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction) {
        webClient = WebClient.builder()
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }

    public List<WorkShift> getWorkShiftByEmployee(long id) {
        List<WorkShift> workShiftEmployeeId = webClient.get()
                .uri("http://work-shift-service/api/workshift/employee/{id}", id)
                .retrieve()
                .bodyToFlux(WorkShift.class)
                .collectList()
                .block();
        return workShiftEmployeeId;
    }

}
