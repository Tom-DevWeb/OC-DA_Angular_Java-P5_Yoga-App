package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    void testFindByIdFound() {
        // Préparer les données retournées par le mock
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Exécuter le test
        User foundUser = userService.findById(1L);

        // Vérifier les résultats
        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirstName());  // Vérification du prénom
        assertEquals("Doe", foundUser.getLastName());    // Vérification du nom de famille
        assertEquals("john.doe@example.com", foundUser.getEmail()); // Vérification de l'email
    }

    @Test
    void testFindByIdNotFound() {
        // Préparer les données retournées par le mock
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Exécuter le test
        User foundUser = userService.findById(1L);

        // Vérifier que le résultat est nul
        assertNull(foundUser);
    }

    @Test
    void testDelete() {
        // Exécuter le test de suppression
        userService.delete(1L);

        // Vérifier que la méthode deleteById du repository a été appelée une fois
        verify(userRepository, times(1)).deleteById(1L);
    }
}
