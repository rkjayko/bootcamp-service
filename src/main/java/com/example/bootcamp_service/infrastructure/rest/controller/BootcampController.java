package com.example.bootcamp_service.infrastructure.rest.controller;


import com.example.bootcamp_service.application.service.IBootcampService;
import com.example.bootcamp_service.domain.dto.BootcampDTO;
import com.example.bootcamp_service.domain.dto.BootcampResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/bootcamps")
@Tag(name = "Bootcamp", description = "Bootcamp management APIs")
public class BootcampController {

    private final IBootcampService bootcampService;

    public BootcampController(IBootcampService bootcampService) {
        this.bootcampService = bootcampService;
    }

    @PostMapping
    @Operation(summary = "Create a new bootcamp", description = "Creates a new bootcamp with the provided details")
    public Mono<Void> createBootcamp(@RequestBody @Valid BootcampDTO bootcampDTO) {
        return bootcampService.createBootcamp(bootcampDTO);
    }

    @GetMapping()
    @Operation(summary = "Get all bootcamps", description = "Retrieves all bootcamps")
    public Flux<BootcampResponseDTO> getBootcamps(
            @RequestParam(name = "sort", defaultValue = "asc") String sortDirection,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return bootcampService.listBootcamps(sortDirection, page, size);
    }
}
