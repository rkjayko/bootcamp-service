package com.example.bootcamp_service.infrastructure.adapter.in;

import com.example.bootcamp_service.domain.dto.CapabilityBootcampRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CapabilityServiceAdapter {

    @Value("${capability.service.url}")
    private String capabilityServiceUrl;

    private final WebClient webClient;

    public CapabilityServiceAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<Void> saveBootcampCapabilities(Long bootcampId, List<Long> capabilityIds) {
        CapabilityBootcampRequestDTO requestDTO = new CapabilityBootcampRequestDTO(bootcampId, capabilityIds);
        return webClient.post()
                .uri(capabilityServiceUrl + "/capability-bootcamp")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<CapabilitiesAndTechnologiesDTO> getAllCapabilitiesAndTechnologiesById(Long bootcampId) {
        return webClient.get()
                .uri(capabilityServiceUrl + "/{bootcampId}", bootcampId)
                .retrieve()
                .bodyToFlux(CapabilitiesAndTechnologiesDTO.class);
    }
}
