package com.example.bootcamp_service.domain.dto;

import com.example.bootcamp_service.infrastructure.adapter.in.CapabilityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampResponseDTO {

    private String name;
    private String description;
    private List<CapabilityDTO> capabilities;

}
