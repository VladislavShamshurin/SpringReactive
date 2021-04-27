package ru.vlad.springApplication.aspect;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.dto.CarDto;
import ru.vlad.springApplication.dto.CarDtoWrapper;
import ru.vlad.springApplication.models.DataObj;
import ru.vlad.springApplication.validation.validators.MyCustomValidator;
import ru.vlad.springApplication.validation.validators.NameCheckValidator;

import java.lang.reflect.Field;
import java.net.BindException;
import java.util.*;
import java.util.stream.Collectors;
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
    public Object validMethodCall(ProceedingJoinPoint joinPoint) {
        return executeLogic(joinPoint);
    }

    private Mono<?> executeLogic(ProceedingJoinPoint joinPoint) {
        Map<DataObj, String> map = new HashMap<>();
        callTheRecursion(new CarDtoWrapper((CarDto) joinPoint.getArgs()[0]), map);
        List<Mono<String>> monoList = createMonoList(map);
        return Flux
                .merge(monoList)
                .collectList()
        .flatMap(list -> {
            try {
                return !list.isEmpty() ? Mono.error(() -> new BindException(list.toString())) : (Mono<?>) joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return Mono.error(() -> new BindException("Error"));
        });
    }

    private List<Mono<String>> createMonoList(Map<DataObj, String> map) {
        return map
                .entrySet()
                .stream()
                .map(e -> {
            DataObj dataObj = e.getKey();
            if (applicationContext.getBean(dataObj.getValidName()) instanceof MyCustomValidator) {
                MyCustomValidator<Object> myCustomValidator =
                        (MyCustomValidator<Object>) applicationContext.getBean(dataObj.getValidName());
                return myCustomValidator.isValidReactive(dataObj.getObject().getCarDto())
                        .mapNotNull(value -> value ? e.getValue() : null);
            }
            return Mono.just("");
        })
                .collect(Collectors.toList());
    }

    private void callTheRecursion(CarDtoWrapper carDtoWrapper, Map<DataObj, String> map) {
        Class<? extends CarDtoWrapper> clazz = carDtoWrapper.getClass();
        Stream
            .of(clazz.getDeclaredFields())
            .forEach(field -> {
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
                                callTheRecursion(obj, map);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    });
            fillTheMap(field, map, carDtoWrapper);
        });
    }

    private void fillTheMap(Field field, Map<DataObj, String> map, CarDtoWrapper carDtoWrapper) {
        if (Stream
                .of(field.getAnnotations())
                .anyMatch(annotation -> annotation.toString().contains("CheckName"))) {
            field.setAccessible(true);
            NameCheckValidator nameCheckValidator =
                    (NameCheckValidator) applicationContext.getBean("NameCheckValidator");
            map.put(new DataObj(nameCheckValidator.getClass().getSimpleName(), carDtoWrapper),
                    field.getName());
        }
    }
}
