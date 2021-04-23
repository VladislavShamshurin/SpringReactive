package ru.vlad.springApplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Users;
import ru.vlad.springApplication.services.impl.UserServiceImpl;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl serviceInterface;

    public UserController(UserServiceImpl serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @GetMapping("/create")
    public Mono<Rendering> createForm() {
        return Mono.just(Rendering
                .view("user_create")
                .modelAttribute("user", new Users())
                .build());
    }

    @PostMapping(value = "/create")
    public Mono<Rendering> create(Users user) {
        return serviceInterface.create(user).then(Mono.just(Rendering
                .view("redirect:/users/list")
                .build()));
    }

    @GetMapping("/edit/{id}")
    public Mono<Rendering> getUser(@PathVariable long id) {
        return Mono.just(Rendering
                .view("user_edit")
                .modelAttribute("user", serviceInterface.read(id))
                .build());
    }

    @PostMapping(value = "/update/{id}")
    public Mono<Rendering> update(@PathVariable(name = "id") long id, Users user) {
        return serviceInterface.update(user, id).then(Mono.just(Rendering
                .view("redirect:/users/list")
                .build()));
    }

    @PostMapping(value = "/delete/{id}")
    public Mono<Rendering> delete(@PathVariable long id) {
        return serviceInterface.delete(id).then(Mono.just(Rendering
                .view("redirect:/users/list")
                .build()));
    }

    @GetMapping("/list")
    public Mono<Rendering> read() {
        return Mono.just(Rendering
                .view("user_list")
                .modelAttribute("users", serviceInterface.readAll())
                .build());
    }
}
