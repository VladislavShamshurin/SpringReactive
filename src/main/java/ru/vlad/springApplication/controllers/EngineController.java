package ru.vlad.springApplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Engines;
import ru.vlad.springApplication.services.impl.EngineServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/engine")
public class EngineController {

    public static final String REDIRECT_ENGINE_LIST = "redirect:/engine/list";
    private final EngineServiceImpl engineService;

    public EngineController(EngineServiceImpl engineService) {
        this.engineService = engineService;
    }

    @GetMapping("/create")
    public Mono<Rendering> engineCreateView() {
        return Mono.just(Rendering
                .view("engine_create")
                .modelAttribute("engine", new Engines())
                .build());
    }

    @PostMapping("/create")
    public Mono<Rendering> engineCreate(Engines engine) {
        return engineService.create(engine)
                .then(Mono.just(
                        Rendering
                                .view(REDIRECT_ENGINE_LIST)
                                .build()));
    }

    @GetMapping("/list")
    public Mono<Rendering> listEngines() {
        return Mono.just(Rendering
                .view("engine_list")
                .modelAttribute("engines", engineService.readAll())
                .build());
    }

    @PostMapping("/delete/{id}")
    public Mono<Rendering> engineDelete(@PathVariable("id") long id) {
        return engineService.delete(id).then(Mono.just(Rendering
                .view(REDIRECT_ENGINE_LIST)
                .build()));
    }

    @GetMapping("/update/{id}")
    public Mono<Rendering> engineUpdateView(@PathVariable long id) {
        return Mono.just(Rendering
                .view("engine_edit")
                .modelAttribute("engine", engineService.read(id))
                .build());
    }

    @PostMapping("/update/{id}")
    public Mono<Rendering> engineUpdate(@Valid Engines engine) {
        return engineService.update(engine, engine.getId())
                .then(Mono.just(Rendering
                        .view(REDIRECT_ENGINE_LIST)
                        .build()));
    }
}
