package cat.itacademy.s04.t01.userapi.repositories.impl;

import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceNotFound;
import cat.itacademy.s04.t01.userapi.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {

    List<User> data;

    public InMemoryUserRepository() {
        data = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        data.add(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return Collections.unmodifiableList(data);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return data
                .stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> searchByName(String name) {
        return data
                .stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return data
                .stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public void deleteAll() {
        data.clear();
    }
}
