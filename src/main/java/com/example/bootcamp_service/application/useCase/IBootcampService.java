package com.example.bootcamp_service.application.useCase;

import com.example.bootcamp_service.infrastructure.adapter.out.BootcampDTO;
import reactor.core.publisher.Mono;

public interface IBootcampService {
    Mono<Void> createBootcamp(BootcampDTO bootcampDTO);
}