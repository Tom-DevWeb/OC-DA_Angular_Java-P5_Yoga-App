package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
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
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class SessionControllerIntegTest {

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
    private SessionService sessionService;

    private Session stubSession;

    @Autowired
    private UserRepository userRepository;

    private User stubUser;

    @BeforeEach
    public void setUp() {
        stubSession = new Session();
        stubSession.setName("Test Session");
        stubSession.setDescription("Test session description");
        stubSession.setDate(new Date());
        stubSession.setTeacher(null);  // Assurez-vous d'ajouter un professeur si nécessaire
        stubSession.setUsers(Collections.emptyList());
        stubSession.setCreatedAt(LocalDateTime.now());
        stubSession.setUpdatedAt(LocalDateTime.now());

        stubUser = User.builder()
                .firstName("User")
                .lastName("Test")
                .email("user@example.com")
                .password("securepassword")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
    }

    @Test
    @WithMockUser
    public void testFindById_Success() throws Exception {
        // Sauvegarde de la session dans la base de données
        Session savedSession = sessionService.create(stubSession);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", savedSession.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(savedSession.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(savedSession.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(savedSession.getDescription())));
    }

    @Test
    @WithMockUser
    public void testFindById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sessions/{id}", 999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testCreateSession_Success() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDescription("A new session for testing");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);  // Utiliser un id de professeur valide si nécessaire

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Session\",\"description\":\"A new session for testing\",\"date\":\"2024-12-05T00:00:00.000+00:00\",\"teacher_id\":1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("New Session")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("A new session for testing")));
    }

    @Test
    @WithMockUser
    public void testUpdateSession_Success() throws Exception {
        Session savedSession = sessionService.create(stubSession);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", savedSession.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Session\",\"description\":\"Updated description\",\"date\":\"2024-12-05T00:00:00.000+00:00\",\"teacher_id\":1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Updated Session")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Updated description")));
    }

    @Test
    @WithMockUser
    public void testUpdateSessionAnyBody_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testDeleteSession_Success() throws Exception {
        Session savedSession = sessionService.create(stubSession);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", savedSession.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testDeleteSession_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", 999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testParticipate_Success() throws Exception {
        Session savedSession = sessionService.create(stubSession);

        userRepository.save(stubUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", savedSession.getId(), stubUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testParticipate_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", 999, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testNoLongerParticipate_Success() throws Exception {

        userRepository.save(stubUser);

        stubSession.setUsers(Collections.singletonList(stubUser));
        Session savedSession = sessionService.create(stubSession);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}/participate/{userId}", savedSession.getId(), stubUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testNoLongerParticipate_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}/participate/{userId}", 999, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testFindAll_Success() throws Exception {
        sessionService.create(stubSession);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }
}
