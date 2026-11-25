package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
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

    private String requestJson;

    @BeforeEach
    void beforeEach() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("Nin", "andreu.nin@example.com");
        User userEntity = new User("Nin", "andreu.nin@example.com");
        UserResponse userResponse = new UserResponse(userEntity.getId(), "Nin", "andreu.nin@example.com");

        requestJson = objectMapper.writeValueAsString(userCreateRequest);

        given(userMapper.toEntity(userCreateRequest))
                .willReturn(userEntity);

        given(userMapper.toResponse(userEntity))
                .willReturn(userResponse);
    }

    @Test
    void shouldReturnEmptyList_whenNoUsersExist() throws Exception {
        // Simula GET /users
        // Espera un array buit
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
    void givenNonExistingUser_WhenCreateUserRequest_thenReturnUser() throws Exception {
        // Simula POST /users amb JSON
        // Espera que torni el mateix usuari amb UUID no nul
        ResultActions postResultActions = mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Nin"))
                .andExpect(jsonPath("$.email").value("andreu.nin@example.com"));

        UserResponse userPostResponse = extractResponse(postResultActions, UserResponse.class);

        // Assert restful URI response
        postResultActions.andExpect(header().string("Location", "/users/" + userPostResponse.id()));
    }

    @Test
    void givenExistingUser_WhenGetUser_thenReturnUser() throws Exception {
        // Primer afegeix un usuari amb POST
        // Després GET /users/{id} i comprova que torni aquest usuari
        ResultActions postResultActions = mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Nin"))
                .andExpect(jsonPath("$.email").value("andreu.nin@example.com"));

        UserResponse userPostResponse = extractResponse(postResultActions, UserResponse.class);

        ResultActions getResultActions = mockMvc
                .perform(get("/users/" + userPostResponse.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userPostResponse.id().toString()))
                .andExpect(jsonPath("$.name").value("Nin"))
                .andExpect(jsonPath("$.email").value("andreu.nin@example.com"));
    }

    @Test
    void givenNonExistingUser_WhenGetUser_thenReturnNotFound() {
        // Simula GET /users/{id} amb un id aleatori
        // Espera 404
    }

    @Test
    void givenExistingUsers_WhenGetUserWithQueryName_thenReturnUsersFiltered() {
        // Afegeix dos usuaris amb POST
        // Fa GET /users?name=jo i comprova que només torni els que contenen "jo"
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
