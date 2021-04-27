package ru.vlad.springApplication.models;

import lombok.Data;
import ru.vlad.springApplication.dto.CarDtoWrapper;

@Data
public class DataObj {
    private final String validName;
    private final CarDtoWrapper object;
}
