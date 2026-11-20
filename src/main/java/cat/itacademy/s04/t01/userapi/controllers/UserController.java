package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.dto.mappers.UserMapper;
import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserMapper userMapper;
    private List<User> users;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
        users = new ArrayList<>();
    }

    @GetMapping("")
    public String getUsers() {
        return users.toString();
    }

    // Later use a @Valid @RequestBody createUserDto object
    @PostMapping("")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        users.add(user);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
    }
}
