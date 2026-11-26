package cat.itacademy.s04.t01.userapi.services.impl;

import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceAlreadyExists;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceNotFound;
import cat.itacademy.s04.t01.userapi.mappers.UserMapper;
import cat.itacademy.s04.t01.userapi.repositories.UserRepository;
import cat.itacademy.s04.t01.userapi.services.UserService;
import cat.itacademy.s04.t01.userapi.utility.UUID_Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository repository;

    public UserServiceImpl(UserMapper userMapper, UserRepository repository) {
        this.userMapper = userMapper;
        this.repository = repository;
    }

    @Override
    public UserResponse create(UserCreateRequest createReq) {
        String email = createReq.email();

        if (repository.existsByEmail(email)) {
            throw new ResourceAlreadyExists("User already exists with email: " + email);
        }

        User toSave = userMapper.toEntity(createReq);
        User savedUser = repository.save(toSave);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> getAllWithName(String name) {
        return repository.searchByName(name)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getWithId(String id) {
        Optional<User> user = repository.findById(UUID_Utility.fromString(id));

        if (user.isEmpty()) {
            throw new ResourceNotFound("User not found with id: " + id);
        }

        return userMapper.toResponse(user.get());
    }
}
