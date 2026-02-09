package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    public PersonRepository repository;

    @Autowired
    public PersonMapper personMap;

    public List<PersonDTO> findAll() {
        logger.warn("Finding all People!");

        var entity = ObjectMapper.parseListObject(repository.findAll(), PersonDTO.class);

        entity.forEach(p -> Hateoas(p));

        return entity;
    }

    public PersonDTO findById(Long id) {
        logger.warn("Finding one Person!");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        var result = ObjectMapper.parseObject(entity, PersonDTO.class);
        result.setBirthDay(new Date());
        result.setPassword("pedro123");
        //result.setPhoneNumber("(44) 99931-3342");

        Hateoas(result);

        return result;
    }

    public PersonDTO create(PersonDTO person) {
        logger.warn("Creating Person");

        var entity = ObjectMapper.parseObject(person, Person.class);

        var hate = ObjectMapper.parseObject(repository.save(entity), PersonDTO.class);

        Hateoas(hate);

        return hate;

    }


    public PersonDTO update(PersonDTO person, Long id) {
        logger.warn("Updating Person");

        Person up = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));

        up.setFirstName(person.getFirstName());
        up.setLastName(person.getLastName());
        up.setAddress(person.getAddress());
        up.setGender(person.getGender());

        var hate = ObjectMapper.parseObject(repository.save(up), PersonDTO.class);

        Hateoas(hate);

        return hate;
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

    private static void Hateoas(PersonDTO result) {
        result.add(linkTo(methodOn(PersonController.class).findById(result.getId())).withSelfRel().withType("GET"));
        result.add(linkTo(methodOn(PersonController.class).create(result)).withRel("create").withType("POST"));
        result.add(linkTo(methodOn(PersonController.class).update(result, result.getId())).withRel("update").withType("PUT"));
        result.add(linkTo(methodOn(PersonController.class).delete(result.getId())).withRel("delete").withType("DELETE"));
    }
}
