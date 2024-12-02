package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Initialisation du mapper
        userMapper = Mappers.getMapper(UserMapper.class);

        // Initialisation de l'entité User
        user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("hashedPassword123")
                .admin(true)
                .createdAt(LocalDateTime.of(2023, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2023, 1, 2, 15, 30))
                .build();

        // Initialisation du DTO UserDto
        userDto = new UserDto(
                2L,
                "jane.doe@example.com",
                "Doe",
                "Jane",
                false,
                "password123",
                LocalDateTime.of(2023, 2, 1, 12, 0),
                LocalDateTime.of(2023, 2, 2, 16, 0)
        );
    }

    @Test
    void shouldMapUserToUserDto() {
        // Act
        UserDto mappedDto = userMapper.toDto(user);

        // Assert
        assertEquals(user.getId(), mappedDto.getId());
        assertEquals(user.getEmail(), mappedDto.getEmail());
        assertEquals(user.getLastName(), mappedDto.getLastName());
        assertEquals(user.getFirstName(), mappedDto.getFirstName());
        assertEquals(user.getPassword(), mappedDto.getPassword());
        assertEquals(user.isAdmin(), mappedDto.isAdmin());
        assertEquals(user.getCreatedAt(), mappedDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), mappedDto.getUpdatedAt());
    }

    @Test
    void shouldMapUserDtoToUser() {
        // Act
        User mappedUser = userMapper.toEntity(userDto);

        // Assert
        assertEquals(userDto.getId(), mappedUser.getId());
        assertEquals(userDto.getEmail(), mappedUser.getEmail());
        assertEquals(userDto.getLastName(), mappedUser.getLastName());
        assertEquals(userDto.getFirstName(), mappedUser.getFirstName());
        assertEquals(userDto.getPassword(), mappedUser.getPassword());
        assertEquals(userDto.isAdmin(), mappedUser.isAdmin());
        assertEquals(userDto.getCreatedAt(), mappedUser.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), mappedUser.getUpdatedAt());
        // Champ ignoré (password)
        assertEquals(userDto.getPassword(), mappedUser.getPassword());
    }

    @Test
    void shouldHandleNullValuesGracefully() {
        assertNull(userMapper.toDto((User) null));
        assertNull(userMapper.toEntity((UserDto) null));
    }
}
