package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
    }

    @Test
    void testFindAll() {
        // Préparer les données retournées par le mock
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));

        // Exécuter le test
        List<Teacher> teachers = teacherService.findAll();

        // Vérifier les résultats
        assertNotNull(teachers);
        assertEquals(1, teachers.size());
        assertEquals("John", teachers.get(0).getFirstName());
        assertEquals("Doe", teachers.get(0).getLastName());

    }

    @Test
    void testFindByIdFound() {
        // Préparer les données retournées par le mock
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Exécuter le test
        Teacher foundTeacher = teacherService.findById(1L);

        // Vérifier les résultats
        assertNotNull(foundTeacher);
        assertEquals("John", foundTeacher.getFirstName());
        assertEquals("Doe", foundTeacher.getLastName());
    }

    @Test
    void testFindByIdNotFound() {
        // Préparer les données retournées par le mock
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // Exécuter le test
        Teacher foundTeacher = teacherService.findById(1L);

        // Vérifier que le résultat est nul
        assertNull(foundTeacher);
    }
}
