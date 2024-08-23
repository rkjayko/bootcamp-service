package com.example.bootcamp_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityBootcampRequestDTO {
    private Long bootcampId;
    private List<Long> capabilityIds;
}
