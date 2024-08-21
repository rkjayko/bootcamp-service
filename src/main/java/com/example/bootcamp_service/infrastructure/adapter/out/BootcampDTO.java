package com.example.bootcamp_service.infrastructure.adapter.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampDTO {
    private String name;
    private String description;
    private List<Long> capabilityIds;

}