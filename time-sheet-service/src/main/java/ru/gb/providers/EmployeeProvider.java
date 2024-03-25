package ru.gb.providers;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.api.Employee;

@Service
public class EmployeeProvider {

    private final WebClient webClient;

    public EmployeeProvider(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction) {
        webClient = WebClient.builder()
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }

    public Employee getEmployeeById(long id) {
        Employee employeeId = webClient.get()
                .uri("http://employee-service/api/employee/{id}", id)
                .retrieve()
                .bodyToMono(Employee.class)
                .block();
        return employeeId;
    }

}
