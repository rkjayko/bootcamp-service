package com.example.bootcamp_service.application.useCase;

import com.example.bootcamp_service.application.mapper.BootcampMapper;
import com.example.bootcamp_service.infrastructure.adapter.out.BootcampDTO;
import com.example.bootcamp_service.domain.entity.Bootcamp;

import com.example.bootcamp_service.infrastructure.adapter.in.CapabilityServiceAdapter;
import com.example.bootcamp_service.domain.exception.BootcampException;
import com.example.bootcamp_service.domain.repository.BootcampRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
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
        Bootcamp bootcamp = bootcampMapper.toEntity(bootcampDTO);

        if (bootcamp == null) {
            return Mono.error(new BootcampException("Error al mapear BootcampDTO a Bootcamp"));
        }

        List<Long> capabilityIds = bootcampDTO.getCapabilityIds();

        if (capabilityIds.isEmpty() || capabilityIds.size() > 4) {
            return Mono.error(new BootcampException("El bootcamp debe tener entre 1 y 4 capacidades."));
        }

        return bootcampRepository.findByName(bootcamp.getName())
                .flatMap(existingBootcamp -> Mono.error(new BootcampException("Ya existe un bootcamp con el nombre proporcionado.")))
                .switchIfEmpty(
                        bootcampRepository.save(bootcamp)
                                .flatMap(savedBootcamp ->
                                        serviceAdapter.saveBootcampCapabilities(savedBootcamp.getId(), capabilityIds)
                                                .then(Mono.just(savedBootcamp)))
                                .map(bootcampMapper::toDTO)
                                .then()
                ).then();
    }
}
