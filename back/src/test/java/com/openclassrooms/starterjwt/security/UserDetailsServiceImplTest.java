package com.openclassrooms.starterjwt.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        // Préparer le mock du repository
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        // Exécuter le test
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("john.doe@example.com");

        // Vérifier les résultats
        assertNotNull(userDetails);
        assertEquals("john.doe@example.com", userDetails.getUsername());  // Vérification de l'email
        assertEquals("John", userDetails.getFirstName());  // Vérification du prénom
        assertEquals("Doe", userDetails.getLastName());    // Vérification du nom de famille
        assertEquals("password123", userDetails.getPassword()); // Vérification du mot de passe
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        // Préparer le mock du repository pour retourner un Optional vide
        when(userRepository.findByEmail("non.existent@example.com")).thenReturn(Optional.empty());

        // Vérifier que la méthode lève une exception UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("non.existent@example.com");
        });
    }
}
