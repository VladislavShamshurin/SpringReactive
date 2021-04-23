package ru.vlad.springApplication.validation.validators;

import reactor.core.publisher.Mono;

public interface MyCustomValidator<T> {
    Mono<Boolean> isValidReactive(T value);
}
