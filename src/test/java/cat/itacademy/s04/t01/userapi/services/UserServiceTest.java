package cat.itacademy.s04.t01.userapi.services;

import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceAlreadyExists;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceNotFound;
import cat.itacademy.s04.t01.userapi.repositories.UserRepository;
import cat.itacademy.s04.t01.userapi.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // ERROR PATH
    // Volem comprovar què passa quan l’email ja existeix.
    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "myemail@example.com";

        // GIVEN:
        // - El repositori retorna true quan comprovem si existeix l’email
        given(userRepository.existsByEmail(email))
                .willReturn(true);

        // WHEN:
        // - Intentem crear un usuari amb aquest email usant el Servei
        Throwable thrown = catchThrowable(() -> userService.create(new UserCreateRequest("Luis", email)));

        // THEN:
        // - Comprovar que es llança una excepció EmailAlreadyExistsException
        // - Verificar que NO s’ha cridat al mètode save() del repository
        then(thrown)
                .isInstanceOf(ResourceAlreadyExists.class)
                .hasMessageContaining("User already exists with email:");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @Disabled
    void getUserById_ShouldThrowExceptionWhenDoesNotExist() {
        UUID id = UUID.randomUUID();

        given(userRepository.findById(id))
                .willReturn(Optional.empty());

        /* TODO:
         * Because the unit test class is not in a spring context, and doesn't need to be.
         * There is troubling in injecting an instance of user mapper to the service class.
         * Find solution to inject in mockito only context.
         *  */
        Throwable thrown = catchThrowable(() -> userService.create(new UserCreateRequest("Luis", "any@example.com")));

        then(thrown)
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("User not found with id:");
    }
}
