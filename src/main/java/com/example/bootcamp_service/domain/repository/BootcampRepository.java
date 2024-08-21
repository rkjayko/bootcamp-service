package com.example.bootcamp_service.domain.repository;

import com.example.bootcamp_service.domain.entity.Bootcamp;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BootcampRepository extends ReactiveCrudRepository<Bootcamp, Long> {
    Mono<Bootcamp> findByName(String name);
}