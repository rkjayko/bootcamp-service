package com.example.bootcamp_service.application.service;

import com.example.bootcamp_service.application.mapper.BootcampMapper;
import com.example.bootcamp_service.domain.dto.BootcampDTO;
import com.example.bootcamp_service.domain.entity.Bootcamp;
import com.example.bootcamp_service.domain.exception.BootcampException;
import com.example.bootcamp_service.domain.port.BootcampRepository;
import com.example.bootcamp_service.infrastructure.adapter.in.*;
import com.example.bootcamp_service.domain.dto.BootcampResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.bootcamp_service.domain.utils.ValidationMessages.*;

@Service
@Slf4j
public class BootcampServiceImpl implements IBootcampService {

    private final BootcampRepository bootcampRepository;
    private final BootcampMapper bootcampMapper;
    private final CapabilityServiceAdapter serviceAdapter;

    public BootcampServiceImpl(BootcampRepository bootcampRepository,
                               BootcampMapper bootcampMapper, CapabilityServiceAdapter serviceAdapter) {
        this.bootcampRepository = bootcampRepository;
        this.bootcampMapper = bootcampMapper;
        this.serviceAdapter = serviceAdapter;
    }

    @Override
    public Mono<Void> createBootcamp(BootcampDTO bootcampDTO) {
        log.info("Access to create Bootcamp");
        Bootcamp bootcamp = bootcampMapper.toEntity(bootcampDTO);
        if (!Objects.isNull(bootcamp)) {
            List<Long> capabilityIds = bootcampDTO.getCapabilityIds();

            if (capabilityIds.isEmpty() || capabilityIds.size() > 4) {
                return Mono.error(new BootcampException(BOOTCAMP_CAPACITY));
            }

            return bootcampRepository.findByName(bootcamp.getName())
                    .flatMap(existingBootcamp -> Mono.error(new BootcampException(BOOTCAMP_EXISTS)))
                    .switchIfEmpty(
                            bootcampRepository.save(bootcamp)
                                    .flatMap(savedBootcamp ->
                                            serviceAdapter.saveBootcampCapabilities(savedBootcamp.getId(), capabilityIds)
                                                    .then(Mono.just(savedBootcamp)))
                                    .map(bootcampMapper::toDTO)
                                    .then()
                    ).then();
        }
        return Mono.error(new BootcampException(BOOTCAMP_DOESNT_EXIST));
    }

    public Flux<BootcampResponseDTO> listBootcamps(String sortDirection, int page, int size) {

        Sort sort = "desc".equalsIgnoreCase(sortDirection) ? Sort.by("name").descending() : Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return bootcampRepository.findAllBy(pageable)
                .flatMap(this::mapBootcampToResponseDTO);
    }

    private Mono<BootcampResponseDTO> mapBootcampToResponseDTO(Bootcamp bootcamp) {
        BootcampResponseDTO bootcampDTO = new BootcampResponseDTO(bootcamp.getName(), bootcamp.getDescription(), null);
        return serviceAdapter.getAllCapabilitiesAndTechnologiesById(bootcamp.getId())
                .collectList()
                .map(capabilitiesAndTechnologies -> {
                    List<CapabilityDTO> capabilities = mapCapabilitiesToDTO(capabilitiesAndTechnologies);
                    bootcampDTO.setCapabilities(capabilities);
                    return bootcampDTO;
                });
    }

    private List<CapabilityDTO> mapCapabilitiesToDTO(List<CapabilitiesAndTechnologiesDTO> capabilitiesAndTechnologies) {
        Map<String, List<TechnologyDTO>> technologiesByCapability = capabilitiesAndTechnologies.stream()
                .collect(Collectors.toMap(
                        CapabilitiesAndTechnologiesDTO::getName,
                        ct -> ct.getTechnologies().stream()
                                .map(tech -> new TechnologyDTO(tech.getName(), tech.getDescription()))
                                .collect(Collectors.toList())
                ));

        return capabilitiesAndTechnologies.stream()
                .map(ct -> new CapabilityDTO(ct.getName(), ct.getDescription(),
                        technologiesByCapability.getOrDefault(ct.getName(), Collections.emptyList())))
                .collect(Collectors.toList());
    }
}
