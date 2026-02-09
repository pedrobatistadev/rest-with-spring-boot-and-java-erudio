package br.com.erudio.testHateoas;

import br.com.erudio.model.Person;

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
}
