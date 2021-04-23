package ru.vlad.springApplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/create")
    public Mono<Rendering> createList() {
        return Mono.just(Rendering.view("admin_createList").build());
    }
}
