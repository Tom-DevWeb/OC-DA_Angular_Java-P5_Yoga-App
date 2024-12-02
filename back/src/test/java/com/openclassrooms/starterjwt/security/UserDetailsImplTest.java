package com.openclassrooms.starterjwt.security;

import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        // Création d'un objet UserDetailsImpl pour les tests
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();
    }

    @Test
    void testGetUsername() {
        assertEquals("john.doe@example.com", userDetails.getUsername());
    }

    @Test
    void testGetFirstName() {
        assertEquals("John", userDetails.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals("Doe", userDetails.getLastName());
    }

    @Test
    void testGetAuthorities() {
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(userDetails, userDetails);
    }

    @Test
    void testEqualsDifferentObject() {
        UserDetailsImpl anotherUserDetails = UserDetailsImpl.builder()
                .id(2L)
                .username("jane.doe@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .admin(false)
                .password("password456")
                .build();

        assertNotEquals(userDetails, anotherUserDetails);
        // Vérifie si les objets sont différents
    }

    @Test
    void testEqualsNullObject() {
        assertNotEquals(null, userDetails);
        // Vérifie que l'objet n'est pas égal à null
    }

    @Test
    void testEqualsDifferentClass() {
        assertNotEquals("String", userDetails);
        // Vérifie que l'objet n'est pas égal à un autre type
    }

    @Test
    void testHashCode() {
        int currentHashCode = userDetails.hashCode();
        assertEquals(currentHashCode, userDetails.hashCode());
    }
}
