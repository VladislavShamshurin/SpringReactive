package ru.vlad.springApplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Transmissions;
import ru.vlad.springApplication.services.impl.TransmissionServiceImpl;

@Controller
@RequestMapping("/transmission")
public class TransmissionController {

    public static final String REDIRECT_TRANSMISSION_LIST = "redirect:/transmission/list";
    private final TransmissionServiceImpl transmissionService;

    public TransmissionController(TransmissionServiceImpl transmissionService) {
        this.transmissionService = transmissionService;
    }

    @GetMapping("/create")
    public Mono<Rendering> transmissionCreateView(@ModelAttribute("transmission") Transmissions transmission) {
        return Mono.just(Rendering
                .view("transmission_create")
                .build());
    }

    @PostMapping("/create")
    public Mono<Rendering> transmissionCreate(Transmissions transmission) {
        return transmissionService.create(transmission).then(Mono.just(Rendering
                .view(REDIRECT_TRANSMISSION_LIST)
                .build()));
    }

    @GetMapping("/list")
    public Mono<Rendering> listTransmissions() {
        return Mono.just(Rendering.view("transmission_list")
                .modelAttribute("transmissions", transmissionService.readAll())
                .build());
    }

    @PostMapping("/delete/{id}")
    public Mono<Rendering> transmissionDelete(@PathVariable("id") long id) {
        return transmissionService.delete(id).then(Mono.just(Rendering
                .view(REDIRECT_TRANSMISSION_LIST)
                .build()));
    }

    @GetMapping("/update/{id}")
    public Mono<Rendering> transmissionUpdateView(@PathVariable("id") long id) {
        return Mono.just(Rendering
                .view("transmission_edit")
                .modelAttribute("transmission", transmissionService.read(id))
                .build());
    }


    @PostMapping("/update/{id}")
    public Mono<Rendering> transmissionUpdate(Transmissions transmission) {
        return transmissionService.update(transmission, transmission.getId()).then(Mono.just(Rendering
                .view(REDIRECT_TRANSMISSION_LIST)
                .build()));
    }
}
