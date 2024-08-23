package com.example.bootcamp_service.application.service;

import com.example.bootcamp_service.domain.dto.BootcampDTO;
import com.example.bootcamp_service.domain.dto.BootcampResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampService {
    Mono<Void> createBootcamp(BootcampDTO bootcampDTO);
    Flux<BootcampResponseDTO> listBootcamps(String sortDirection, int page, int size);
    }