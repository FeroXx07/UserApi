package cat.itacademy.s04.t01.userapi.dto.mappers;

import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

    User toEntity(UserCreateRequest request);
}
