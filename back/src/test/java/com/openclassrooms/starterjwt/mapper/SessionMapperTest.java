package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private Session session;
    private SessionDto sessionDto;
    private Date dateNow = new Date();

    @BeforeEach
    void setUp() {
        // Setup Teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Setup Users
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Jane");
        user1.setLastName("Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Mike");
        user2.setLastName("Smith");

        // Setup session
        session = new Session();
        session.setId(1L);
        session.setName("Session 1");
        session.setDate(dateNow);
        session.setDescription("Description of session 1");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        // Setup SessionDto
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session 1");
        sessionDto.setDate(dateNow);
        sessionDto.setTeacher_id(1L); // Associe l'ID du professeur
        sessionDto.setDescription("Description of session 1");
        sessionDto.setUsers(Arrays.asList(1L, 2L)); // Associe les IDs des utilisateurs
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());

        // Mock TeacherService and UserService for getting teacher and users by ID
        lenient().when(teacherService.findById(1L)).thenReturn(teacher);
        lenient().when(userService.findById(1L)).thenReturn(user1);
        lenient().when(userService.findById(2L)).thenReturn(user2);
    }

    @Test
    void shouldMapSessionToSessionDto() {
        // Act
        SessionDto mappedDto = sessionMapper.toDto(session);

        // Assert
        assertEquals(session.getId(), mappedDto.getId());
        assertEquals(session.getName(), mappedDto.getName());
        assertEquals(session.getDate().getTime(), mappedDto.getDate().getTime());
        assertEquals(session.getDescription(), mappedDto.getDescription());
        assertEquals(session.getTeacher().getId(), mappedDto.getTeacher_id());
        assertEquals(session.getUsers().size(), mappedDto.getUsers().size());
        assertTrue(mappedDto.getUsers().contains(1L));
        assertTrue(mappedDto.getUsers().contains(2L));
    }

    @Test
    void shouldMapSessionDtoToSession() {
        // Act
        Session mappedSession = sessionMapper.toEntity(sessionDto);

        // Assert
        assertEquals(sessionDto.getId(), mappedSession.getId());
        assertEquals(sessionDto.getName(), mappedSession.getName());
        assertEquals(sessionDto.getDate().getTime(), mappedSession.getDate().getTime());
        assertEquals(sessionDto.getDescription(), mappedSession.getDescription());
        assertEquals(sessionDto.getTeacher_id(), mappedSession.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), mappedSession.getUsers().size());
        assertTrue(mappedSession.getUsers().stream().anyMatch(user -> user.getId().equals(1L)));
        assertTrue(mappedSession.getUsers().stream().anyMatch(user -> user.getId().equals(2L)));
    }

    @Test
    void toEntity_shouldReturnNullWhenSessionDtoIsNull() {
        // Act
        Session mappedSession = sessionMapper.toEntity((SessionDto) null);

        // Assert
        assertNull(mappedSession);
    }

    @Test
    void toDto_shouldReturnNullWhenSessionIsNull() {
        // Act
        SessionDto mappedDto = sessionMapper.toDto((Session) null);

        // Assert
        assertNull(mappedDto);
    }
}
