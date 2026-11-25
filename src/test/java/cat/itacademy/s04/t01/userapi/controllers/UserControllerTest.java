package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserMapper userMapper;

    @Test
    @DisplayName("Simula GET /users. Espera un array buit")
    void shouldReturnEmptyList_whenNoUsersExist() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()")
                        .value(0))
                .andExpect(content()
                        .json("[]"));
    }

    @Test
    @DisplayName("Simula POST /users amb JSON. Espera que torni el mateix usuari amb UUID no nul")
    void givenNonExistingUser_WhenCreateUserRequest_thenReturnUser() throws Exception {
        ResultActions postResultActions = performPost("Nin", "andreu.nin@example.com");
        UserResponse userPostResponse = extractResponse(postResultActions, UserResponse.class);

        // Assert restful URI response
        postResultActions.andExpect(header().string("Location", "/users/" + userPostResponse.id()));
    }

    @Test
    @DisplayName("Primer afegeix un usuari amb POST Després GET /users/{id} i comprova que torni aquest usuari")
    void givenExistingUser_WhenGetUser_thenReturnUser() throws Exception {
        UserResponse userPostResponse = extractResponse(performPost("Nin", "andreu.nin@example.com"), UserResponse.class);
        performGetWithId(userPostResponse.id().toString(), userPostResponse.name(), userPostResponse.email());
    }

    @Test
    @DisplayName("Simula GET /users/{id} amb un id aleatori. Espera 404")
    void givenNonExistingUser_WhenGetUser_thenReturnNotFound() throws Exception {
        ResultActions getResultActions = mockMvc
                .perform(get("/users/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Afegeix dos usuaris amb POST. Fa GET /users?name=jo i comprova que només torni els que contenen \"jo\"")
    void givenExistingUsers_WhenGetUserWithQueryName_thenReturnUsersFiltered() throws Exception {
        UserResponse userJon = extractResponse(performPost("Jo", "jon@exampl.com"), UserResponse.class);
        UserResponse userPostResponse = extractResponse(performPost("Nin", "andreu.nin@example.com"), UserResponse.class);
        UserResponse userJordan = extractResponse(performPost("Jo", "jordan@exampl.com"), UserResponse.class);

        ResultActions getUsersResultAction = mockMvc
                .perform(get("/users?name=jo"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<UserResponse> userResponseList = extractResponseList(getUsersResultAction, UserResponse.class);
        assertThat(userResponseList.size()).isEqualTo(2);
        assertThat(userResponseList).contains(userJon, userJordan);
        assertThat(userResponseList).doesNotContain(userPostResponse);
    }

    private void performGetWithId(String id, String name, String email) throws Exception {
        ResultActions getResultActions = mockMvc
                .perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Nin"))
                .andExpect(jsonPath("$.email").value("andreu.nin@example.com"));
    }

    private ResultActions performPost(String name, String email) throws Exception {
        String postJsonRequest = mockEntityAndDtoToMapper(name, email);
        return mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email));
    }

    private String mockEntityAndDtoToMapper(String name, String email) {
        UserCreateRequest userCreateRequest = new UserCreateRequest(name, email);
        User userEntity = new User(name, email);
        UserResponse userResponse = new UserResponse(userEntity.getId(), name, email);

        given(userMapper.toEntity(userCreateRequest))
                .willReturn(userEntity);

        given(userMapper.toResponse(userEntity))
                .willReturn(userResponse);

        return objectMapper.writeValueAsString(userCreateRequest);
    }

    private <T> List<T> extractResponseList(
            ResultActions resultActions,
            Class<T> responseType
    ) throws Exception {

        String content = resultActions.andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(
                content,
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, responseType)
        );
    }

    private <T> T extractResponse(
            ResultActions resultActions,
            Class<T> responseType
    ) throws Exception {

        String content = resultActions.andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(content, responseType);
    }
}
