package ru.vlad.springApplication.validation.validators;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.dto.CarDto;
import ru.vlad.springApplication.validation.annotation.CheckName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
@Component("NameCheckValidator")
public class NameCheckValidator implements ConstraintValidator<CheckName, Object>, MyCustomValidator<Object> {
    private final R2dbcEntityTemplate entityTemplate;

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return true;
    }

    @Override
    public Mono<Boolean> isValidReactive(Object value) {
        CarDto carDto = (CarDto) value;
        return entityTemplate
                .exists(
                        Query.query(
                                Criteria.where("brand").is(carDto.getBrand())), CarDto.class);
    }
}

