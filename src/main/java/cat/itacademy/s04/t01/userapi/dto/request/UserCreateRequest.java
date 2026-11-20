package cat.itacademy.s04.t01.userapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(@NotBlank String name, @Email @NotBlank String email) {
}
