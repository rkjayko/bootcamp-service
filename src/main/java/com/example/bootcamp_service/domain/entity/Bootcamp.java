package com.example.bootcamp_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("bootcamps")
public class Bootcamp {
    @Id
    private Long id;

    @Column("nombre")
    private String name;

    @Column("descripcion")
    private String description;

}