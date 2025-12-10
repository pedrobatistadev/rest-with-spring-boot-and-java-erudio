package br.com.erudio.mapper.custom;

import br.com.erudio.data.dto.v2.PersonDTOV2;
import br.com.erudio.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getAddress());
        dto.setBirthday(new Date());

        return dto;
    }

    public Person convertDTOtoEntity(PersonDTOV2 person) {
        Person p = new Person();
        p.setFirstName(person.getFirstName());
        p.setLastName(person.getLastName());
        p.setAddress(person.getAddress());
        p.setGender(person.getGender());

        return p;
    }
}
