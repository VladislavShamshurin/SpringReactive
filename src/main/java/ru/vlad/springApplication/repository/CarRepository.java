package ru.vlad.springApplication.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vlad.springApplication.dto.CarDto;

@Repository
public interface CarRepository extends ReactiveCrudRepository<CarDto, Long> {

}
