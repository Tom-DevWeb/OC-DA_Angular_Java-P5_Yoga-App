package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// Active l'extension Mockito pour utiliser @Mock et @InjectMocks
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    // Crée un mock pour UserService, utilisé pour simuler les appels de service dans les tests
    @Mock
    private UserService userService;

    // Crée un mock pour UserMapper, utilisé pour convertir entre User et UserDto
    @Mock
    private UserMapper userMapper;

    // Injecte les mocks dans une instance réelle de UserController
    @InjectMocks
    private UserController userController;

    // Variables pour stocker des données utilisateur utilisées dans les tests
    private User user1;
    private UserDto userDto1;

    // Mock pour représenter les détails d'un utilisateur dans le contexte de sécurité
    @Mock
    private UserDetails userDetails;

    // Initialisation des objets nécessaires avant chaque test
    @BeforeEach
    void setUp() {
        // Crée un utilisateur de test
        user1 = new User("test@gmail.com", "Hosni", "Brahim", "pass@word", false);
        user1.setId(1L); // Définit un ID pour simuler un utilisateur existant

        // Crée un DTO correspondant à l'utilisateur
        userDto1 = new UserDto(
                1L, "test@gmail.com", "Hosni", "Brahim", false,
                "pass@word", LocalDateTime.now(), LocalDateTime.now()
        );

        // Crée un UserDetails correspondant à l'utilisateur
        userDetails = new org.springframework.security.core.userdetails.User(
                user1.getEmail(), // Email comme identifiant
                user1.getPassword(), // Mot de passe
                new ArrayList<>() // Pas de rôles spécifiques (liste vide)
        );
    }

    // Test pour vérifier que findById retourne un utilisateur avec succès
    @Test
    void findById() {
        // Simule la récupération de l'utilisateur par le service
        when(userService.findById(1L)).thenReturn(user1);
        // Simule le mapping de l'utilisateur en DTO
        when(userMapper.toDto(user1)).thenReturn(userDto1);

        // Appelle la méthode findById avec un ID valide
        ResponseEntity<?> response = userController.findById("1");

        // Vérifie que la réponse n'est pas null
        assertNotNull(response);
        // Vérifie que le statut HTTP est 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Vérifie que le corps de la réponse est le DTO attendu
        assertEquals(userDto1, response.getBody());
    }

    // Test pour vérifier que findById retourne NOT_FOUND si l'utilisateur n'existe pas
    @Test
    void findById_shouldReturnNotFoundWhenUserNotFound() {
        // Simule le cas où l'utilisateur n'est pas trouvé
        when(userService.findById(0L)).thenReturn(null);

        // Appelle la méthode avec un ID inexistant
        ResponseEntity<?> response = userController.findById("0");

        // Vérifie que la réponse est NOT_FOUND
        assertNotNull(response);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    // Test pour vérifier que findById retourne BAD_REQUEST pour un ID invalide
    @Test
    void findById_shouldReturnBadRequestWhenIdIsInvalid() {
        // Appelle la méthode avec un ID non numérique
        ResponseEntity<?> response = userController.findById("abc");

        // Vérifie que la réponse est BAD_REQUEST
        assertNotNull(response);
        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    // Test pour vérifier que save fonctionne correctement
    @Test
    void save() {
        // Simule la récupération de l'utilisateur
        when(userService.findById(1L)).thenReturn(user1);

        // Simule le contexte de sécurité avec un utilisateur authentifié
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        // Appelle la méthode save avec un ID valide
        ResponseEntity<?> response = userController.save("1");

        // Vérifie que la réponse est OK
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void save_shouldReturnNotFoundWhenUserNotFound() {
        when(userService.findById(0L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("0");
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }
    @Test
    void save_shouldReturnBadRequestWhenIdIsInvalid() {
        ResponseEntity<?> response = userController.save("054iujuu");
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }
    @Test
    void save_shouldReturnUnauthorizedWhenUserEmailIsInvalid() {
        user1.setEmail("testNotFound@gmail.com");
        when(userService.findById(1L)).thenReturn(user1);
        assertNotNull(user1);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> response = userController.save("1");
        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());

    }
}