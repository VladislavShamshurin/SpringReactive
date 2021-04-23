package ru.vlad.springApplication.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vlad.springApplication.models.Users;
import ru.vlad.springApplication.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;

    public Mono<Users> create(Users user) {
        return userRepository.save(user);
    }

    public Flux<Users> readAll() {
        return userRepository.findAll();
    }

    public Mono<Users> read(Long id) {
        return userRepository.findById(id);
    }

    public Mono<Users> update(Users user, Long id) {
        user.setId(id);
        return userRepository.save(user);
    }

    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }
}
