package com.example.bootcamp_service.infrastructure.adapter.in;

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
