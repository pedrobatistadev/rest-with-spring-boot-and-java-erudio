package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.dto.v1.PersonDTO;
import br.com.erudio.data.dto.v2.PersonDTOV2;
import br.com.erudio.exception.BadRequestException;
import br.com.erudio.exception.FileStorageException;
import br.com.erudio.exception.RequiredObjectNullException;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.file.exporter.contract.FileExporter;
import br.com.erudio.file.exporter.factory.FileExporterFactory;
import br.com.erudio.file.importer.contract.FileImporter;
import br.com.erudio.file.importer.factory.FileImporterFactory;
import br.com.erudio.mapper.ObjectMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static br.com.erudio.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    public PersonRepository repository;

    @Autowired
    public PersonMapper personMap;

    @Autowired
    public FileImporterFactory importer;

    @Autowired
    public FileExporterFactory exporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable page) {
        logger.warn("Finding all People!");

        var people = repository.findAll(page);

        return buildPagedModel(page, people);
    }

    public Resource exportFile(Pageable page, String acceptHeader) {
        logger.warn("Finding all People!");

        var people = repository.findAll(page).map((x) -> ObjectMapper.parseObject(x, PersonDTO.class)).getContent();

        try {
            FileExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportFile(people);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export !", e);
        }
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable page) {
        logger.warn("Finding People by Name!");

        var people = repository.findByName(firstName, page);

        return buildPagedModel(page, people);
    }

    public PersonDTO findById(Long id) {
        if (id == null) {
            throw new RequiredObjectNullException();
        }
        logger.warn("Finding one Person!");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        var result = parseObject(entity, PersonDTO.class);
        result.setBirthDay(new Date());
        result.setPassword("pedro123");
        //result.setPhoneNumber("(44) 99931-3342");

        Hateoas(result);

        return result;
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) {
            throw new RequiredObjectNullException();
        }
        logger.warn("Creating Person");

        var entity = parseObject(person, Person.class);

        var hate = parseObject(repository.save(entity), PersonDTO.class);

        Hateoas(hate);

        return hate;

    }

    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid File!");

        try(InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename()).orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter importer = this.importer.getImporter(filename);

            List<Person> entities = importer.importFile(inputStream).stream()
                    .map(dto -> repository.save(parseObject(dto, Person.class))).toList();

            return entities.stream().map((x) -> {
                PersonDTO dto = ObjectMapper.parseObject(x, PersonDTO.class);
                Hateoas(dto);
                return dto;
            }).toList();

        } catch(Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
    }


    public PersonDTO update(PersonDTO person, Long id) {
        if (id == null || person == null) {
            throw new RequiredObjectNullException();
        }
        logger.warn("Updating Person");

        Person up = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));

        up.setFirstName(person.getFirstName());
        up.setLastName(person.getLastName());
        up.setAddress(person.getAddress());
        up.setGender(person.getGender());

        var hate = parseObject(repository.save(up), PersonDTO.class);

        Hateoas(hate);

        return hate;
    }

    public void delete(Long id) {

        if (id == null) {
            throw new RequiredObjectNullException();
        }

        logger.warn("Deleting Person");

        Person del = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        repository.delete(del);
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {

        logger.warn("Disabling Person");

        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        repository.disablePerson(id);

        Person entity = repository.findById(id).get();
        PersonDTO dto = parseObject(entity, PersonDTO.class);

        return dto;

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
        result.add(linkTo(methodOn(PersonController.class)).slash("massCreation").withRel("massCreation").withType("POST"));
        result.add(linkTo(methodOn(PersonController.class).update(result, result.getId())).withRel("update").withType("PUT"));
        result.add(linkTo(methodOn(PersonController.class).disablePerson(result.getId())).withRel("disable").withType("PATCH"));
        result.add(linkTo(methodOn(PersonController.class).delete(result.getId())).withRel("delete").withType("DELETE"));
        result.add(linkTo(methodOn(PersonController.class).exportPage(1,12,"asc",null)).withRel("exportPage").withType("GET").withTitle("Export People"));
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable page, Page<Person> people) {
        var peopleWithLinks = people.map((x) -> {
            var dto = parseObject(x, PersonDTO.class);
            Hateoas(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
                .findAll(page.getPageNumber(), page.getPageSize(), String.valueOf(page.getSort()))).withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }
}
