package br.com.erudio.services;

import br.com.erudio.data.dto.v1.PersonDTO;
import br.com.erudio.exception.RequiredObjectNullException;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import br.com.erudio.testHateoas.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    private MockPerson input;

    @InjectMocks
    private PersonServices services;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Person person = input.mockEntity(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        var result = services.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("pedro123", result.getPassword());
        assertNotNull(result.getPassword());
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("GET")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/person/v1") && link.getType().equals("POST")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("PUT")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("DELETE")));

    }

    @Test
    void create() {
        PersonDTO dto = input.mockDTO(1L);
        dto.setId(null);

        Person persisted = input.mockEntity(1L);

        when(repository.save(any(Person.class))).thenReturn(persisted);
        var result = services.create(dto);

        assertNotNull(result.getId());

    }

    @Test
    void update() {
        Person person = input.mockEntity(1L);
        person.setFirstName("Leonardo");
        PersonDTO dto = input.mockDTO(1L);
        dto.setFirstName("Jorge");
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(any(Person.class))).thenReturn(person);
        PersonDTO result = services.update(dto, 1L);

        assertEquals("Jorge", result.getFirstName());


    }

    @Test
    void delete() {
        Person person = input.mockEntity(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        services.delete(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository).delete(person);
    }

    @Test
    void findAll() {
        List<Person> people = input.mockList();
        when(repository.findAll()).thenReturn(people);

        List<PersonDTO> result = services.findAll();

        verify(repository).findAll();
        PersonDTO fivePerson = result.get(5);
        assertEquals("Female", fivePerson.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectNullException.class, () -> {
            services.create(null);
        });

        String excpectedMenssage = "it is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(excpectedMenssage));

    }

}
