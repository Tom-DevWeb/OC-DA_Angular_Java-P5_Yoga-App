package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private SignupRequest signUpRequest;
    private User user;

    @BeforeEach
    void setUp() {
        // Initialisation des objets partagés entre les tests
        loginRequest = new LoginRequest();
        signUpRequest = new SignupRequest();
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");
        signUpRequest.setPassword("password123");
        user = new User("test@example.com", "Doe", "John", "encodedPassword", false);
    }

    @Test
    void testAuthenticateUser_ShouldReturnJwtResponse_WhenValidLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        UserDetailsImpl userDetails =
                new UserDetailsImpl(1L, email, "John", "Doe", false, null);
        Authentication authentication = mock(Authentication.class);

        // Comportement des mocks
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(JwtResponse.class, response.getBody());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals(email, jwtResponse.getUsername());
    }

    @Test
    void testRegisterUser_ShouldReturnError_WhenEmailAlreadyTaken() {
        // Arrange
        String email = "test@example.com";
        signUpRequest.setEmail(email);

        // Comportement du mock pour simuler un email déjà pris
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertInstanceOf(MessageResponse.class, response.getBody());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }

    @Test
    void testRegisterUser_ShouldReturnSuccess_WhenEmailIsAvailable() {
        // Arrange
        String email = "new@example.com";
        signUpRequest.setEmail(email);

        // Créer un utilisateur mocké à partir du signUpRequest
        User user = new User(email, signUpRequest.getLastName(), signUpRequest.getFirstName(),
                "encodedPassword", false); // Utiliser un mot de passe encodé simulé

        // Simuler un email disponible
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");


        // Simuler l'enregistrement de l'utilisateur et le retour d'un utilisateur enregistré
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(MessageResponse.class, response.getBody());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
    }
}
