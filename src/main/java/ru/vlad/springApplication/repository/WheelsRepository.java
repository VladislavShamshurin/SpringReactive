package ru.vlad.springApplication.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vlad.springApplication.models.Wheels;

@Repository
public interface WheelsRepository extends ReactiveCrudRepository<Wheels, Long> {
}
