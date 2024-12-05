package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
        }
    }

    @Test
    void shouldConnectToDatabase() {
        assertThat(userRepository.count()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldAuthenticateUserWithValidCredentials() throws Exception {
        // Préparation des données utilisateur
        userRepository.save(new User()
                .setEmail("test@example.com")
                .setFirstName("Test")
                .setLastName("User")
                .setPassword(passwordEncoder.encode("password")) // "password"
                .setAdmin(false));

        String loginPayload = "{"
                + "\"email\": \"test@example.com\","
                + "\"password\": \"password\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.admin").value(false));
    }

    @Test
    void shouldFailAuthenticationWithInvalidCredentials() throws Exception {
        String invalidLoginPayload = "{"
                + "\"email\": \"wrong@example.com\","
                + "\"password\": \"wrongpassword\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLoginPayload))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        String registerPayload = "{"
                + "\"email\": \"newuser@example.com\","
                + "\"firstName\": \"New\","
                + "\"lastName\": \"User\","
                + "\"password\": \"securepassword\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void shouldFailToRegisterUserWithExistingEmail() throws Exception {
        userRepository.save(new User()
                .setEmail("existinguser@example.com")
                .setFirstName("Existing")
                .setLastName("User")
                .setPassword("somepassword")
                .setAdmin(false));

        String existingUserPayload = "{"
                + "\"email\": \"existinguser@example.com\","
                + "\"firstName\": \"Existing\","
                + "\"lastName\": \"User\","
                + "\"password\": \"securepassword\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(existingUserPayload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: Email is already taken!"));
    }


}
