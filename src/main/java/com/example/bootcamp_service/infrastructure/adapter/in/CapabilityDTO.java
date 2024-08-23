package com.example.bootcamp_service.infrastructure.adapter.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityDTO {
    private String name;
    private String description;
    private List<TechnologyDTO> technologies;
}
