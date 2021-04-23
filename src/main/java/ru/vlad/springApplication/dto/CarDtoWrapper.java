package ru.vlad.springApplication.dto;

import lombok.Data;
import ru.vlad.springApplication.validation.annotation.CheckName;

@Data
public class CarDtoWrapper {
    @CheckName
    private CarDto carDto;

    public CarDtoWrapper(CarDto carDto) {
        this.carDto = carDto;
    }
}
