package ru.vlad.springApplication.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vlad.springApplication.models.Transmissions;

@Repository
public interface TransmissionRepository extends ReactiveCrudRepository<Transmissions, Long> {
}
