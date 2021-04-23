package ru.vlad.springApplication.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vlad.springApplication.models.Engines;

@Repository
public interface EngineRepository extends ReactiveCrudRepository<Engines, Long> {
}
