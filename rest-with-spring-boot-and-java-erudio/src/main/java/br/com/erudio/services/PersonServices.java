package br.com.erudio.services;

import br.com.erudio.data.dto.v1.PersonDTO;
import br.com.erudio.data.dto.v2.PersonDTOV2;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.mapper.ObjectMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    public PersonRepository repository;

    @Autowired
    public PersonMapper personMap;

    public List<PersonDTO> findAll() {
        logger.warn("Finding all People!");

        var entity = repository.findAll();

        return ObjectMapper.parseListObject(entity, PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.warn("Finding one Person!");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        var result = ObjectMapper.parseObject(entity, PersonDTO.class);
        result.setBirthDay(new Date());

        return result;
    }

    public PersonDTO create(PersonDTO person) {
        logger.warn("Creating Person");

        var entity = ObjectMapper.parseObject(person, Person.class);

        return ObjectMapper.parseObject(repository.save(entity), PersonDTO.class);

    }


    public PersonDTO update(PersonDTO person, Long id) {
        logger.warn("Updating Person");

        Person up = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));

        up.setFirstName(person.getFirstName());
        up.setLastName(person.getLastName());
        up.setAddress(person.getAddress());
        up.setGender(person.getGender());

        return ObjectMapper.parseObject(repository.save(up), PersonDTO.class);
    }

    public void delete(Long id) {

        logger.warn("Deleting Person");

        Person del = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        repository.delete(del);
    }

    //-- Version 2 --

    public PersonDTOV2 createV2(PersonDTOV2 person) {
        logger.warn("Creating Person V2");

        Person per = personMap.convertDTOtoEntity(person);

        return personMap.convertEntityToDTO(repository.save(per));

    }
}
