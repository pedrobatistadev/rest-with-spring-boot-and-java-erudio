package br.com.erudio.testHateoas;

import br.com.erudio.data.dto.v1.PersonDTO;
import br.com.erudio.model.Person;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity (Long number) {
        Person person = new Person();
        person.setId(number);
        person.setAddress("Adress test" + number);
        person.setFirstName("First name test" + number);
        person.setGender((number % 2 == 0) ? "Male" : "Female");
        person.setLastName("Last name test" + number);
        return person;
    }

    public PersonDTO mockDTO (Long number) {
        PersonDTO dto = new PersonDTO();
        dto.setId(number);
        dto.setAddress("Adress testdto" + number);
        dto.setFirstName("First name testdto" + number);
        dto.setGender((number % 2 == 0) ? "Male" : "Female");
        dto.setLastName("Last name testdto" + number);
        dto.setPhoneNumber("Phone testdto" + number);
        dto.setPassword("Senha" + number);
        return dto;
    }

    public List<Person> mockList () {
        List<Person> people = new ArrayList<>();
        for (Long i = 0L; i < 10L; i++) {
            people.add(mockEntity(i));
        }

        return people;
    }

}
