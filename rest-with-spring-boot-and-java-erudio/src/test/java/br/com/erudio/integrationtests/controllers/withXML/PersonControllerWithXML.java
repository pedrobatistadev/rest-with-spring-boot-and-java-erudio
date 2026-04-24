package br.com.erudio.integrationtests.controllers.withXML;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerWithXML extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static RequestSpecification specification2;
    private static XmlMapper objectMapperToXML;
    private static PersonDTO person;

    @Autowired
    private PersonRepository repository;

    @BeforeAll
    static void setUp() {
        objectMapperToXML = new XmlMapper();
        objectMapperToXML.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO result = objectMapperToXML.readValue(content, PersonDTO.class);
        assertEquals("Grasieli", result.getFirstName());
    }

    @Test
    @Order(5)
    void findAll() throws JsonProcessingException {
        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        System.out.println(repository.findAll());

        List<PersonDTO> people = objectMapperToXML.readValue(content, new TypeReference<List<PersonDTO>>() {});

        assertTrue(people.size() == 1);
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        init();

        specification = new RequestSpecBuilder()
                .addHeaders(header())
                .setBasePath("/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO retorno = objectMapperToXML.readValue(content, PersonDTO.class);
        person = retorno;
        assertTrue(retorno.getId() > 0);

    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        person.setFirstName("Grasieli");

        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .body(person)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO retorno = objectMapperToXML.readValue(content, PersonDTO.class);
        person = retorno;
        assertEquals("Grasieli", retorno.getFirstName());
    }

    @Test
    @Order(4)
    void disablePerson() throws JsonProcessingException {
        specification2 = new RequestSpecBuilder()
                .addHeaders(header())
                .setBasePath("/person/v1/atu")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String content = given(specification2)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO result = objectMapperToXML.readValue(content, PersonDTO.class);
        person = result;

        assertFalse(result.getEnabled());
    }

    @Test
    @Order(6)
    void delete() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    private Map<String,String> header() {
        return Map.of(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB,
                TestConfigs.ACCEPT,TestConfigs.MEDIATYPEXML);
    }

    private void init() {
        person.setFirstName("Pedro");
        person.setLastName("Batista");
        person.setGender("Mas");
        person.setEnabled(true);
        person.setAddress("Toledo");
    }
}