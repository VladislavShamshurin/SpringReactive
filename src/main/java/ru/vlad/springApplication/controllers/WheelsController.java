package ru.vlad.springApplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Wheels;
import ru.vlad.springApplication.services.impl.WheelsServiceImpl;

@Controller
@RequestMapping("/wheels")
public class WheelsController {

    private static final String REDIRECT_TO_WHEEL_LIST = "redirect:/wheels/list";
    private final WheelsServiceImpl wheelsService;

    public WheelsController(WheelsServiceImpl wheelsService) {
        this.wheelsService = wheelsService;
    }

    @GetMapping("/create")
    public Mono<Rendering> wheelsCreateView(@ModelAttribute("wheels") Wheels wheels) {
        return Mono.just(Rendering
                .view("wheels_create")
                .build());
    }

    @PostMapping("/create")
    public Mono<Rendering> wheelsCreate(Wheels wheels) {
        return wheelsService.create(wheels).then(Mono.just(Rendering
                .view(REDIRECT_TO_WHEEL_LIST)
                .build()));
    }

    @GetMapping("/list")
    public Mono<Rendering> listWheels() {
        return Mono.just(Rendering
                .view("wheels_list")
                .modelAttribute("wheelsList", wheelsService.readAll())
                .build());
    }

    @PostMapping("/delete/{id}")
    public Mono<Rendering> wheelsDelete(@PathVariable("id") long id) {
        return wheelsService.delete(id).then(Mono.just(Rendering
                .view(REDIRECT_TO_WHEEL_LIST)
                .build()));
    }

    @GetMapping("/update/{id}")
    public Mono<Rendering> wheelsUpdateView(@PathVariable("id") long id) {
        return Mono.just(Rendering
                .view("wheels_edit")
                .modelAttribute("wheels", wheelsService.read(id))
                .build());
    }


    @PostMapping("/update/{id}")
    public Mono<Rendering> wheelsOptionUpdate(Wheels wheels) {
        return wheelsService.update(wheels, wheels.getId()).then(Mono.just(Rendering
                .view(REDIRECT_TO_WHEEL_LIST)
                .build()));
    }
}
