package ru.vlad.springApplication.services.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Transmissions;
import ru.vlad.springApplication.repository.TransmissionRepository;

@Service
public class TransmissionServiceImpl {

    private final TransmissionRepository transmissionRepository;

    public TransmissionServiceImpl(TransmissionRepository transmissionRepository) {
        this.transmissionRepository = transmissionRepository;
    }

    public Mono<Transmissions> create(Transmissions transmission) {
        return transmissionRepository.save(transmission);
    }

    public Flux<Transmissions> readAll() {
        return transmissionRepository.findAll();
    }

    public Mono<Transmissions> read(Long id) {
        return transmissionRepository.findById(id);
    }

    public Mono<Transmissions> update(Transmissions transmission, Long id) {
        transmission.setId(id);
        return transmissionRepository.save(transmission);
    }

    public Mono<Void> delete(Long id) {
        return transmissionRepository.deleteById(id);
    }
}
