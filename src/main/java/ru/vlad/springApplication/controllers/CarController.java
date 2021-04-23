package ru.vlad.springApplication.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.aspect.Validation;
import ru.vlad.springApplication.dto.CarDto;
import ru.vlad.springApplication.dto.CarDtoWrapper;
import ru.vlad.springApplication.services.impl.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {
    public static final String CARS = "car_create";
    public static final String WHEELS_MODEL = "wheelsModel";
    public static final String ENGINES_MODEL = "enginesModel";
    public static final String TRANSMISSIONS_MODEL = "transmissionsModel";
    public static final String OTHER_OPTION_MODEL = "otherOptionModel";
    private final CarServiceImpl carService;
    private final WheelsServiceImpl wheelsService;
    private final EngineServiceImpl engineService;
    private final TransmissionServiceImpl transmissionService;
    private final OtherOptionServiceImpl otherOptionService;

    @GetMapping
    public Mono<Rendering> createForm() {
        return Mono.just(Rendering
                .view(CARS)
                .modelAttribute("car", new CarDto())
                .modelAttribute(WHEELS_MODEL, wheelsService.readAll())
                .modelAttribute(ENGINES_MODEL, engineService.readAll())
                .modelAttribute(TRANSMISSIONS_MODEL, transmissionService.readAll())
                .modelAttribute(OTHER_OPTION_MODEL, otherOptionService.readAll())
                .build());
    }

    @Validation
    @PostMapping("/create_car")
    public Mono<Rendering> createCar(CarDto carDto) {
        CarDtoWrapper carDtoWrapper = new CarDtoWrapper(carDto);
        return carService.create(carDtoWrapper.getCarDto())
                .then(carService.executeCreateQuery(carDtoWrapper.getCarDto(), carDtoWrapper.getCarDto().getOtherOptionId()))
                .then(Mono.just(Rendering
                        .view("redirect:/cars")
                        .build()));
    }

    @GetMapping("/price")
    public Mono<Rendering> getPrice(@RequestParam("price") BigDecimal price) {
        return Mono.just(Rendering
                .view("car_price")
                .modelAttribute("price", price)
                .build());
    }

    @GetMapping("/edit/{id}")
    public Mono<Rendering> editCar(@PathVariable long id) {
        return Mono.just(Rendering
                .view("car_edit")
                .modelAttribute("car", carService.read(id).map(carService::convertCarToDto))
                .modelAttribute(WHEELS_MODEL, wheelsService.readAll())
                .modelAttribute(ENGINES_MODEL, engineService.readAll())
                .modelAttribute(TRANSMISSIONS_MODEL, transmissionService.readAll())
                .modelAttribute(OTHER_OPTION_MODEL, otherOptionService.readAll())
                .build());
    }

    @PostMapping(value = "/update/{id}")
    public Mono<Rendering> updateCar(CarDto car) {
        return carService.update(car)
                .then(carService.executeUpdateQuery(car, car.getOtherOptionId()))
                .then(Mono.just(Rendering
                        .view("redirect:/cars/list")
                        .build()));
    }

    @PostMapping("/delete/{id}")
    public Mono<Rendering> deleteCar(@PathVariable long id) {
        return carService.delete(id).then(Mono.just(Rendering
                .view("redirect:/cars/list")
                .build()));
    }

    @GetMapping("/list")
    public Mono<Rendering> readAll() {
        return Mono.just(Rendering
                .view("car_list")
                .modelAttribute("cars", carService.readAll())
                .build());
    }
}
