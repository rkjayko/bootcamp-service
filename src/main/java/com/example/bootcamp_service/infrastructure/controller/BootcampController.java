package com.example.bootcamp_service.infrastructure.controller;


import com.example.bootcamp_service.infrastructure.adapter.out.BootcampDTO;
import com.example.bootcamp_service.application.useCase.IBootcampService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/bootcamps")
public class BootcampController {

    private final IBootcampService bootcampService;

    public BootcampController(IBootcampService bootcampService) {
        this.bootcampService = bootcampService;
    }

    @PostMapping
    public Mono<Void> createBootcamp(@RequestBody @Valid BootcampDTO bootcampDTO) {
        return bootcampService.createBootcamp(bootcampDTO);
    }
}
