package cat.itacademy.s04.t01.userapi.services;

import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserCreateRequest createReq);
    List<UserResponse> getAll();
    List<UserResponse> getAllWithName(String name);
    UserResponse getWithId(String id);
}
