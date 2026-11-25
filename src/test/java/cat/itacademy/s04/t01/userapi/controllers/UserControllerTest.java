package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.dto.request.UserCreateRequest;
import cat.itacademy.s04.t01.userapi.dto.response.UserResponse;
import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.mappers.UserMapper;
import cat.itacademy.s04.t01.userapi.utility.UUID_Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

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

    @BeforeEach
    void beforeEach() {

    }

    @Test
    void getUsers_returnsEmptyListInitially() throws Exception {
        // Simula GET /users
        // Espera un array buit
        mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.length()").value(0)).andExpect(content().json("[]"));

    }

    @Test
    void createUser_returnsUserWithId() throws Exception {
        // Simula POST /users amb JSON
        // Espera que torni el mateix usuari amb UUID no nul

        UserCreateRequest userRequest = new UserCreateRequest("Nin", "andreu.nin@example.com");
        User userEntity = new User("Nin", "andreu.nin@example.com");
        UserResponse userResponse = new UserResponse(userEntity.getId(), "Nin", "andreu.nin@example.com");

        String requestJson = objectMapper.writeValueAsString(userRequest);

        when(userMapper.toEntity(userRequest))
                .thenReturn(userEntity);
        when(userMapper.toResponse(userEntity))
                .thenReturn(userResponse);

        ResultActions resultActions = mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Nin"))
                .andExpect(jsonPath("$.email").value("andreu.nin@example.com"));
    }

    @Test
    void getUserById_returnsCorrectUser() {
        // Primer afegeix un usuari amb POST
        // Després GET /users/{id} i comprova que torni aquest usuari
    }

    @Test
    void getUserById_returnsNotFoundIfMissing() {
        // Simula GET /users/{id} amb un id aleatori
        // Espera 404
    }

    @Test
    void getUsers_withNameParam_returnsFilteredUsers() {
        // Afegeix dos usuaris amb POST
        // Fa GET /users?name=jo i comprova que només torni els que contenen "jo"
    }
}
