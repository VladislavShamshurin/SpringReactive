package ru.vlad.springApplication.services.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.OtherOptions;
import ru.vlad.springApplication.repository.OtherOptionRepository;

@Service
public class OtherOptionServiceImpl {

    private final OtherOptionRepository otherOptionRepository;

    public OtherOptionServiceImpl(OtherOptionRepository otherOptionRepository) {
        this.otherOptionRepository = otherOptionRepository;
    }

    public Mono<OtherOptions> create(OtherOptions otherOption) {
        return otherOptionRepository.save(otherOption);
    }

    public Flux<OtherOptions> readAll() {
        return otherOptionRepository.findAll();
    }

    public Mono<OtherOptions> read(Long id) {
        return otherOptionRepository.findById(id);
    }

    public Mono<OtherOptions> update(OtherOptions otherOption, Long id) {
        otherOption.setId(id);
        return otherOptionRepository.save(otherOption);
    }

    public Mono<Void> delete(Long id) {
        return otherOptionRepository.deleteById(id);
    }

}
