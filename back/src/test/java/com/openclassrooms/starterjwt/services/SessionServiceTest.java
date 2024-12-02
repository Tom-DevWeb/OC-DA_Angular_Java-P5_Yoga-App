package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup des objets de test
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>(Arrays.asList(user)));
    }

    @Test
    void testCreateSession() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        Session createdSession = sessionService.create(session);

        // Assert
        assertNotNull(createdSession);
        assertEquals(session.getId(), createdSession.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testDeleteSession() {
        // Arrange
        doNothing().when(sessionRepository).deleteById(anyLong());

        // Act
        sessionService.delete(1L);

        // Assert
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllSessions() {
        // Arrange
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session));

        // Act
        List<Session> sessions = sessionService.findAll();

        // Assert
        assertFalse(sessions.isEmpty());
        assertEquals(1, sessions.size());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetSessionById() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // Act
        Session foundSession = sessionService.getById(1L);

        // Assert
        assertNotNull(foundSession);
        assertEquals(session.getId(), foundSession.getId());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateSession() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        Session updatedSession = sessionService.update(1L, session);

        // Assert
        assertNotNull(updatedSession);
        assertEquals(session.getId(), updatedSession.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipateInSession() {
        // Arrange
        User newUser = new User();
        newUser.setId(2L);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(newUser));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        sessionService.participate(1L, 2L);

        // Assert
        assertTrue(session.getUsers().contains(newUser));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipateInSession_UserNotFound() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void testParticipateInSession_SessionNotFound() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void testNoLongerParticipateInSession() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        sessionService.noLongerParticipate(1L, 1L);

        // Assert
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipateInSession_UserNotFound() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void testNoLongerParticipateInSession_BadRequest() {
        // Arrange
        session.setUsers(Arrays.asList(user));
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 2L));
    }
}
