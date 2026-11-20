package cat.itacademy.s04.t01.userapi.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class User {

    @NotNull
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
