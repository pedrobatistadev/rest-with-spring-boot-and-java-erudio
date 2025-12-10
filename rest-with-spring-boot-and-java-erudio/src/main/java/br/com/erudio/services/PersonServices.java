package br.com.erudio.services;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.mapper.ObjectMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<PersonDTO> findAll() {
        logger.info("Finding all People!");

        var entity = repository.findAll();

        return ObjectMapper.parseListObject(entity, PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("NotFound"));

        return ObjectMapper.parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating Person");

        var entity = ObjectMapper.parseObject(person, Person.class);

        return ObjectMapper.parseObject(repository.save(entity), PersonDTO.class);

    }

    public PersonDTO update(PersonDTO person, Long id) {
        logger.info("Updating Person");

        Person up = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));

        up.setFirstName(person.getFirstName());
        up.setLastName(person.getLastName());
        up.setAddress(person.getAddress());
        up.setGender(person.getGender());

        return ObjectMapper.parseObject(repository.save(up), PersonDTO.class);
    }

    public void delete(Long id) {

        logger.info("Deleting Person");

        Person del = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("NotFound"));

        repository.delete(del);
    }
}
