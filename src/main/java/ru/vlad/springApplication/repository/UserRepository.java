package ru.vlad.springApplication.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vlad.springApplication.models.Users;

@Repository
public interface UserRepository extends ReactiveCrudRepository<Users, Long> {
    Users findByName(String name);
}
