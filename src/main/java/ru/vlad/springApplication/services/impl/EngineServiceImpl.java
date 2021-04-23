package ru.vlad.springApplication.services.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Engines;
import ru.vlad.springApplication.repository.EngineRepository;

@Service
public class EngineServiceImpl {

    protected final EngineRepository engineRepository;

    public EngineServiceImpl(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

    public Mono<Engines> create(Engines model) {
        return engineRepository.save(model);
    }

    public Flux<Engines> readAll() {
        return engineRepository.findAll();
    }

    public Mono<Engines> read(Long id) {
        return engineRepository.findById(id);
    }

    public Mono<Engines> update(Engines model, Long id) {
        model.setId(id);
        return engineRepository.save(model);
    }

    public Mono<Void> delete(Long id) {
        return engineRepository.deleteById(id);
    }
}
