package br.com.erudio.services;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices services;

    @Mock
    PersonRepository repository;

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
        assertNotNull(result.getPassword());
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("GET")));
        assertEquals("Adress test1", person.getAddress());

    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAll() {
    }
}