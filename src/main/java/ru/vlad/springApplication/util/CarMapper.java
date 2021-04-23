package ru.vlad.springApplication.util;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.vlad.springApplication.dto.CarDto;

import java.util.function.BiFunction;

@Component
@AllArgsConstructor
public class CarMapper implements BiFunction<Row, Object, CarDto> {

    @Override
    public CarDto apply(Row row, Object o) {
        Long id = row.get("id", Long.class);
        String brand = row.get("brand", String.class);
        Long wheelsId = row.get("wheels_id", Long.class);
        Long transmissionId = row.get("transmission_id", Long.class);
        Long engineId = row.get("engine_id", Long.class);
        Long optionId = row.get("option_id", Long.class);
        return new CarDto(id, brand, wheelsId, transmissionId, engineId, optionId);
    }
}
