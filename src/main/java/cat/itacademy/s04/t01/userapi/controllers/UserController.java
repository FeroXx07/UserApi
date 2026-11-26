package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceNotFound;
import cat.itacademy.s04.t01.userapi.mappers.UserMapper;
import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.services.UserService;
import cat.itacademy.s04.t01.userapi.utility.UUID_Utility;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Later use a @Valid @RequestBody createUserDto object
    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity
                .created(URI.create("/users/" + response.id()))
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam Optional<String> name) {
        return name
                .map(s -> ResponseEntity.ok(userService.getAllWithName(s)))
                .orElseGet(() -> ResponseEntity.ok(userService.getAll()));

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(userService.getWithId(id));
    }
}
