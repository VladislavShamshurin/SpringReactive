package ru.vlad.springApplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.OtherOptions;
import ru.vlad.springApplication.services.impl.OtherOptionServiceImpl;

@Controller
@RequestMapping("/otherOption")
public class OtherOptionsController {

    private static final String REDIRECT_TO_OPTION_LIST = "redirect:/otherOption/list";
    private final OtherOptionServiceImpl otherOptionService;

    public OtherOptionsController(OtherOptionServiceImpl otherOptionService) {
        this.otherOptionService = otherOptionService;
    }

    @GetMapping("/create")
    public Mono<Rendering> otherOptionCreateView(@ModelAttribute("otherOption") OtherOptions otherOption) {
        return Mono.just(Rendering
                .view("otheroption_create")
                .build());
    }

    @PostMapping("/create")
    public Mono<Rendering> otherOptionCreate(OtherOptions otherOption) {
        return Mono.just(Rendering
                .view(REDIRECT_TO_OPTION_LIST)
                .build());
    }

    @GetMapping("/list")
    public Mono<Rendering> listOtherOptions() {
        return Mono.just(Rendering
                .view("otheroption_list")
                .modelAttribute("otherOptions", otherOptionService.readAll())
                .build());
    }

    @PostMapping("/delete/{id}")
    public Mono<Rendering> otherOptionDelete(@PathVariable("id") long id) {
        return otherOptionService.delete(id).then(Mono.just(Rendering
                .view(REDIRECT_TO_OPTION_LIST)
                .build()));
    }

    @GetMapping("/update/{id}")
    public Mono<Rendering> otherOptionUpdateView(@PathVariable("id") long id) {
        return Mono.just(Rendering
                .view("otheroption_edit")
                .modelAttribute("otherOption", otherOptionService.read(id))
                .build());
    }

    @PostMapping("/update/{id}")
    public Mono<Rendering> otherOptionUpdate(OtherOptions otherOption) {
        return otherOptionService.update(otherOption, otherOption.getId()).then(Mono.just(Rendering
                .view(REDIRECT_TO_OPTION_LIST)
                .build()));
    }
}
