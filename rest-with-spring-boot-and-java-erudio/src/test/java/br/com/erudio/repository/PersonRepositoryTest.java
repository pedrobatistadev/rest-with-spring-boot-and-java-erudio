package br.com.erudio.repository;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository repository;

    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(2)
    void disablePerson() {
        Long id = person.getId();

        repository.disablePerson(id);

        var result = repository.findById(id).get();
        person = result;

        assertNotNull(person);
        assertFalse(person.getEnabled());

    }

    @Test
    @Order(1)
    void findByName() {
        String name = "and";
        Pageable page = PageRequest.of(0,12, Sort.by(Sort.Direction.ASC, "id"));

        person = repository.findByName(name,page).getContent().get(0);

        assertNotNull(person);
        assertEquals("Amandie", person.getFirstName());
        assertEquals("Rankling", person.getLastName());

    }
}