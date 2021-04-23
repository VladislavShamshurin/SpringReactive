package ru.vlad.springApplication.services.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Wheels;
import ru.vlad.springApplication.repository.WheelsRepository;

@Service
public class WheelsServiceImpl {

    private final WheelsRepository wheelsRepository;

    public WheelsServiceImpl(WheelsRepository wheelsRepository) {
        this.wheelsRepository = wheelsRepository;
    }

    public Mono<Wheels> create(Wheels wheels) {
        return wheelsRepository.save(wheels);
    }

    public Flux<Wheels> readAll() {
        return wheelsRepository.findAll();
    }

    public Mono<Wheels> read(Long id) {
        return wheelsRepository.findById(id);
    }

    public Mono<Wheels> update(Wheels wheels, Long id) {
        wheels.setId(id);
        return wheelsRepository.save(wheels);
    }

    public Mono<Void> delete(Long id) {
        return wheelsRepository.deleteById(id);
    }
}
