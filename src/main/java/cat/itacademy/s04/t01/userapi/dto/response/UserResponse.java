package cat.itacademy.s04.t01.userapi.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserResponse(@NotNull UUID id, @NotBlank String name, @Email @NotBlank String email) {
}
