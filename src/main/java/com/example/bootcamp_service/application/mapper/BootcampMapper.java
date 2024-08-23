package com.example.bootcamp_service.application.mapper;

import com.example.bootcamp_service.domain.entity.Bootcamp;
import com.example.bootcamp_service.domain.dto.BootcampDTO;
import org.springframework.stereotype.Component;

@Component
public class BootcampMapper {

    public Bootcamp toEntity(BootcampDTO bootcampDTO) {
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setName(bootcampDTO.getName());
        bootcamp.setDescription(bootcampDTO.getDescription());
        return bootcamp;
    }

    public BootcampDTO toDTO(Bootcamp bootcamp) {
        BootcampDTO bootcampDTO = new BootcampDTO();
        bootcampDTO.setName(bootcamp.getName());
        bootcampDTO.setDescription(bootcamp.getDescription());
        return bootcampDTO;
    }
}
