package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class UserControllerIntegTest {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
        }
    }

    @Autowired
    private UserRepository userRepository;

    private User stubUser;

    @BeforeEach
    public void setUp() {
        stubUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("user@example.com")
                .password("password")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testFindUserById_Success() throws Exception {

        userRepository.save(stubUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", stubUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(stubUser.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("user@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("John")));
    }

    @Test
    @WithMockUser
    public void testFindUserById_NotFound() throws Exception {
        Long invalidUserId = 999L; // ID qui n'existe pas

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", invalidUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testFindUserById_BadRequest() throws Exception {

        String invalidId = "abc"; // ID invalide (non numérique)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testDeleteUser_Success() throws Exception {

        userRepository.save(stubUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", stubUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testDeleteUser_NotFound() throws Exception {
        Long invalidUserId = 999L; // ID qui n'existe pas

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", invalidUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testDeleteUser_BadRequest() throws Exception {
        String invalidId = "abc"; // ID invalide (non numérique)

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
