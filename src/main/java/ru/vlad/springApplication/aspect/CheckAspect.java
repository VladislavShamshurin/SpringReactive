package ru.vlad.springApplication.aspect;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.dto.CarDto;
import ru.vlad.springApplication.dto.CarDtoWrapper;
import ru.vlad.springApplication.validation.validators.MyCustomValidator;
import ru.vlad.springApplication.validation.validators.NameCheckValidator;

import java.lang.reflect.Field;
import java.net.BindException;
import java.util.*;
import java.util.stream.Stream;

@Aspect
@Component
@AllArgsConstructor
public class CheckAspect {
    public final ApplicationContext applicationContext;

    @Pointcut("@annotation(Validation)")
    public void executeMethod() {
    }

    @Around("executeMethod()")
    public Object validMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        return executeLogic(joinPoint, joinPoint.getArgs()[0]);
    }

    public Object executeLogic(ProceedingJoinPoint joinPoint, Object object) throws Throwable {
        CarDtoWrapper carDtoWrapper = new CarDtoWrapper((CarDto) object);
        Map<Object, String> map = new HashMap<>();
        Set<String> setString = validate(carDtoWrapper, map)
            .map(field -> {
                Set<String> set = new HashSet<>();
                    for (Map.Entry<Object, String> entry : map.entrySet()) {
                        if (applicationContext.getBean(entry.getValue()) instanceof MyCustomValidator) {
                            MyCustomValidator<Object> myCustomValidator =
                                    (MyCustomValidator<Object>) applicationContext.getBean(entry.getValue());
                            CarDto carDto = ((CarDtoWrapper)entry.getKey()).getCarDto();
                            myCustomValidator
                            .isValidReactive(carDto)
                            .map(value -> {
                                if (value)
                                    set.add(carDto.getBrand());
                                return value;
                            }).subscribe();
                        }
                    }
                    return set;
                })
            .orElseThrow(() -> new BindException("Error"));
        return setString.isEmpty() ? (Mono<?>) joinPoint.proceed() : Mono.error(() -> new BindException("Error"));
    }



    public Optional<Field> validate(CarDtoWrapper carDtoWrapper, Map<Object, String> map) {
        Class<? extends CarDtoWrapper> clazz = carDtoWrapper.getClass();
        return Stream
            .of(clazz.getDeclaredFields())
            .peek(field -> {
                Stream
                        .of(field.getAnnotations())
                        .filter(annotation -> annotation.toString().contains("Valid"))
                        .findAny()
                        .ifPresent(annotation -> {
                    CarDtoWrapper obj;
                    try {
                        field.setAccessible(true);
                        obj = (CarDtoWrapper) field.get(carDtoWrapper);
                        if (obj != null) {
                            validate(obj, map);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
                if (Stream
                        .of(field.getAnnotations())
                        .anyMatch(annotation -> annotation.toString().contains("CheckName"))) {
                    field.setAccessible(true);
                    NameCheckValidator nameCheckValidator =
                            (NameCheckValidator) applicationContext.getBean("NameCheckValidator");
                    map.put(carDtoWrapper, nameCheckValidator.getClass().getSimpleName());
                }
        })
                .findAny();
    }
}
