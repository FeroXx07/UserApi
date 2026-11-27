package cat.itacademy.s04.t01.userapi.services;

import cat.itacademy.s04.t01.userapi.RequestValidator;
import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceAlreadyExists;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceNotFound;
import cat.itacademy.s04.t01.userapi.mappers.UserMapper;
import cat.itacademy.s04.t01.userapi.repositories.UserRepository;
import cat.itacademy.s04.t01.userapi.services.impl.UserServiceImpl;
import cat.itacademy.s04.t01.userapi.utility.UUID_Utility;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Spy
    private RequestValidator requestValidator = new RequestValidator(
            Validation.buildDefaultValidatorFactory().getValidator()
    );

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
    void createUser_ShouldCreateWhenInputIsValid() {
        UserCreateRequest createRequest = new UserCreateRequest("ABC","ABC@XYZ.com");
        UserResponse createResponse = userService.create(createRequest);

        assertThat(createResponse.id()).isNotNull();
        assertThat(createResponse.email()).isEqualTo("ABC@XYZ.com");
        assertThat(createResponse.name()).isEqualTo("ABC");
    }

    @Test
    void createUser_ShouldThrowWhenInputIsInValid() {
        UserCreateRequest createRequestInvalidA = new UserCreateRequest("ABC","ABCXYZ.com");
        UserCreateRequest createRequestInvalidB = new UserCreateRequest("","ABC@XYZ.com");

        Throwable thrownA = catchThrowable(() -> userService.create(createRequestInvalidA));
        Throwable thrownB = catchThrowable(() -> userService.create(createRequestInvalidB));

        then(thrownA)
                .isInstanceOf(ConstraintViolationException.class);

        then(thrownB)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void getUserById_ShouldThrowWhenDoesNotExist() {
        UUID id = UUID.randomUUID();

        given(userRepository.findById(id))
                .willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> userService.getWithId(id.toString()));

        then(thrown)
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("User not found with id:");
    }
    @Test
    void getUserById_ShouldThrowInvalidId() {
        String emptyId = "";
        Throwable thrownEmpty = catchThrowable(() -> userService.getWithId(emptyId));
        then(thrownEmpty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null or empty");

        String invalidId = "ASDASD-ASDASD-123123-343";
        Throwable thrownInvalid = catchThrowable(() -> userService.getWithId(invalidId));
        then(thrownInvalid)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserByName_ShouldThrowInvalidInput() {
        String emptyName = "";
        Throwable thrownEmpty = catchThrowable(() -> userService.getAllWithName(emptyName));
        then(thrownEmpty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null or empty");
    }
}
