package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        // Initialisation du mapper
        teacherMapper = Mappers.getMapper(TeacherMapper.class);

        // Initialisation de l'entité Teacher
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.of(2023, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2023, 1, 2, 15, 30))
                .build();

        // Initialisation du DTO TeacherDto
        teacherDto = new TeacherDto(
                2L,
                "Jane",
                "Doe",
                LocalDateTime.of(2023, 2, 1, 12, 0),
                LocalDateTime.of(2023, 2, 2, 16, 0)
        );
    }

    @Test
    void shouldMapTeacherToTeacherDto() {
        // Act
        TeacherDto mappedDto = teacherMapper.toDto(teacher);

        // Assert
        assertEquals(teacher.getId(), mappedDto.getId());
        assertEquals(teacher.getLastName(), mappedDto.getLastName());
        assertEquals(teacher.getFirstName(), mappedDto.getFirstName());
        assertEquals(teacher.getCreatedAt(), mappedDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), mappedDto.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherDtoToTeacher() {
        // Act
        Teacher mappedTeacher = teacherMapper.toEntity(teacherDto);

        // Assert
        assertEquals(teacherDto.getId(), mappedTeacher.getId());
        assertEquals(teacherDto.getLastName(), mappedTeacher.getLastName());
        assertEquals(teacherDto.getFirstName(), mappedTeacher.getFirstName());
        assertEquals(teacherDto.getCreatedAt(), mappedTeacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), mappedTeacher.getUpdatedAt());
    }

    @Test
    void shouldHandleNullValuesGracefully() {
        // Act & Assert
        assertNull(teacherMapper.toDto((Teacher) null));
        assertNull(teacherMapper.toEntity((TeacherDto) null));
    }
}
