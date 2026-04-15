package br.com.erudio.integrationtests.controllers.withXML;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerWithXML extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapperToXML;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        objectMapperToXML = new XmlMapper();
        objectMapperToXML.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
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

        assertEquals("Grasieli", retorno.getFirstName());
    }

    @Test
    void disablePerson() {
    }

    @Test
    void delete() {
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