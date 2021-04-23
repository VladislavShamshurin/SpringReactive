package ru.vlad.springApplication.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.dto.CarDto;
import ru.vlad.springApplication.models.*;
import ru.vlad.springApplication.repository.CarRepository;
import ru.vlad.springApplication.util.CarMapper;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Service
@AllArgsConstructor
public class CarServiceImpl {
    private final CarRepository carRepository;
    private final DatabaseClient databaseClient;
    private final CarMapper carMapper;
    private final R2dbcEntityTemplate entityTemplate;
    private final EngineServiceImpl ENGINE_SERVICE;
    private final WheelsServiceImpl WHEELS_SERVICE;
    private final TransmissionServiceImpl TRANSMISSION_SERVICE;
    private final OtherOptionServiceImpl OTHER_OPTION_SERVICE;

    public Mono<CarDto> create(@Valid CarDto car) {
        return entityTemplate.insert(car);
    }

    public Mono<Void> executeCreateQuery(CarDto car, Long optionId) {
        String query = "INSERT INTO cars_other_options (car_id, option_id) VALUES (:car_id, :option_id)";
        return databaseClient
                .sql(query)
                .bind("car_id", car.getId())
                .bind("option_id", optionId)
                .fetch()
                .all()
                .then();
    }

    public Flux<Cars> readAll() {
        String query = "SELECT * FROM car INNER JOIN cars_other_options ON car.id = cars_other_options.car_id";
        return gettingFluxCars(databaseClient
                .sql(query)
                .map(carMapper::apply)
                .all());
    }

    public Mono<Cars> read(Long id) {
        return Mono
                .from(
                        gettingFluxCars(entityTemplate.selectOne(
                                Query.query(Criteria.where("id").is(id)), CarDto.class)
                                .flux()));
    }

    public Mono<Void> validate(long id) {
        return entityTemplate.exists(Query.query(Criteria.where("id").is(id)), CarDto.class)
                .flatMap(value -> value ? Mono.empty() : Mono.error(() -> new ValidationException("Validation exception")));
    }

    public Flux<Cars> gettingFluxCars(Flux<CarDto> flux) {
        return flux.map(this::convertDtoToEmptyCar)
                .flatMap(car ->
                        ENGINE_SERVICE.read(car.getEngine().getId())
                                .map(engines ->
                                {
                                    car.setEngine(engines);
                                    return car;
                                }))
                .flatMap(car ->
                        WHEELS_SERVICE.read(car.getWheels().getId())
                                .map(wheels ->
                                {
                                    car.setWheels(wheels);
                                    return car;
                                }))
                .flatMap(car ->
                        TRANSMISSION_SERVICE.read(car.getTransmission().getId())
                                .map(transmissions ->
                                {
                                    car.setTransmission(transmissions);
                                    return car;
                                }))

                .flatMap(car ->
                        OTHER_OPTION_SERVICE.read(car.getOtherOptions().get(0).getId())
                                .map(otherOptions ->
                                {
                                    car.setOtherOptions(List.of(otherOptions));
                                    return car;
                                }));
    }

    public Mono<Void> update(CarDto car) {
        return validate(car.getId())
                .then(entityTemplate.update(car)
                        .then());
    }

    public Mono<Void> executeUpdateQuery(CarDto car, Long opt) {
        String query = "UPDATE cars_other_options SET option_id = :optId WHERE car_id = :opt";
        return databaseClient
                .sql(query)
                .bind("optId", car.getOtherOptionId())
                .bind("opt", opt)
                .then();
    }

    public Mono<Void> delete(Long id) {
        return carRepository.deleteById(id);
    }

    public Cars convertDtoToEmptyCar(CarDto carDto) {
        Engines engines = new Engines();
        engines.setId(carDto.getEngineId());
        Wheels wheels = new Wheels();
        wheels.setId(carDto.getWheelsId());
        Transmissions transmissions = new Transmissions();
        transmissions.setId(carDto.getTransmissionId());
        OtherOptions options = new OtherOptions();
        options.setId(carDto.getOtherOptionId());
        return new Cars(carDto.getId(), carDto.getBrand(), wheels, transmissions, engines, List.of(options));
    }

    public CarDto convertCarToDto(Cars cars) {
        return new CarDto(cars.getId(), cars.getBrand(), cars.getWheels().getId(), cars.getTransmission().getId(),
                cars.getEngine().getId(), cars.getOtherOptions().get(0).getId());
    }
}
