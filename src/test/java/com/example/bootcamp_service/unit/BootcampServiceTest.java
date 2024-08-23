package com.example.bootcamp_service.unit;

import com.example.bootcamp_service.application.mapper.BootcampMapper;
import com.example.bootcamp_service.application.service.BootcampServiceImpl;
import com.example.bootcamp_service.domain.entity.Bootcamp;
import com.example.bootcamp_service.domain.exception.BootcampException;
import com.example.bootcamp_service.domain.port.BootcampRepository;
import com.example.bootcamp_service.domain.dto.BootcampDTO;
import com.example.bootcamp_service.infrastructure.adapter.in.CapabilityServiceAdapter;
import com.example.bootcamp_service.domain.dto.BootcampResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class BootcampServiceTest {
    
    private static final String BOOTCAMP_NAME = "bootcamp 1";
    
    private static final String BOOTCAMP_DESCRIPTION = "Description 1";

    private static final Long BOOTCAMP_ID = 1L;

    private static final String ERROR_CAPABILITY_MSG = "El bootcamp debe tener entre 1 y 4 capacidades.";
    
    @Mock
    private BootcampRepository bootcampRepository;

    @Mock
    private BootcampMapper bootcampMapper;

    @Mock
    private CapabilityServiceAdapter serviceAdapter;

    @InjectMocks
    private BootcampServiceImpl bootcampService;

    private BootcampDTO bootcampDTO;
    private Bootcamp bootcamp;
    private List<Long> capabilityIds;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        capabilityIds = Arrays.asList(BOOTCAMP_ID, 2L);
        bootcampDTO = new BootcampDTO(BOOTCAMP_NAME, BOOTCAMP_DESCRIPTION, capabilityIds);
        bootcamp = new Bootcamp(BOOTCAMP_ID, BOOTCAMP_NAME, BOOTCAMP_DESCRIPTION);
    }

    @Test
    void createBootcampSuccess() {
        when(bootcampMapper.toEntity(bootcampDTO)).thenReturn(new Bootcamp(BOOTCAMP_ID, BOOTCAMP_NAME, BOOTCAMP_DESCRIPTION));
        when(bootcampRepository.findByName(bootcamp.getName())).thenReturn(Mono.empty());
        when(bootcampRepository.save(bootcamp)).thenReturn(Mono.just(bootcamp));
        when(serviceAdapter.saveBootcampCapabilities(eq(BOOTCAMP_ID), eq(capabilityIds))).thenReturn(Mono.empty());
        when(bootcampMapper.toDTO(bootcamp)).thenReturn(bootcampDTO);

        StepVerifier.create(bootcampService.createBootcamp(bootcampDTO))
                .verifyComplete();
    }
    
    @Test
    void createBootcampInvalidCapabilities() {
        when(bootcampMapper.toEntity(bootcampDTO)).thenReturn(new Bootcamp(BOOTCAMP_ID, BOOTCAMP_NAME, BOOTCAMP_DESCRIPTION));
        bootcampDTO.setCapabilityIds(List.of());
        StepVerifier.create(bootcampService.createBootcamp(bootcampDTO))
                .expectErrorMatches(throwable -> throwable instanceof BootcampException &&
                        throwable.getMessage().equals(ERROR_CAPABILITY_MSG))
                .verify();
    }

    @Test
    void createBootcampMoreThanFourCapabilities() {
        when(bootcampMapper.toEntity(bootcampDTO)).thenReturn(new Bootcamp(BOOTCAMP_ID, BOOTCAMP_NAME, BOOTCAMP_DESCRIPTION));
        bootcampDTO.setCapabilityIds(Arrays.asList(BOOTCAMP_ID, 2L, 3L, 4L, 5L));

        StepVerifier.create(bootcampService.createBootcamp(bootcampDTO))
                .expectErrorMatches(throwable -> throwable instanceof BootcampException &&
                        throwable.getMessage().equals(ERROR_CAPABILITY_MSG))
                .verify();
    }

    @Test
    void testListBootcamps() {

        Bootcamp bootcamp1 = new Bootcamp(1L, BOOTCAMP_NAME, BOOTCAMP_DESCRIPTION);
        Bootcamp bootcamp2 = new Bootcamp(2L, "Python Bootcamp", "Description 2");

        List<Bootcamp> bootcamps = List.of(bootcamp1, bootcamp2);
        Page<Bootcamp> page = new PageImpl<>(bootcamps);

        when(bootcampRepository.findAllBy(any(Pageable.class))).thenReturn(Flux.fromIterable(page));

        when(serviceAdapter.getAllCapabilitiesAndTechnologiesById(anyLong()))
                .thenReturn(Flux.empty());

        Flux<BootcampResponseDTO> result = bootcampService.listBootcamps("asc", 0, 2);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals(BOOTCAMP_NAME))
                .expectNextMatches(dto -> dto.getName().equals("Python Bootcamp"))
                .verifyComplete();
    }
}