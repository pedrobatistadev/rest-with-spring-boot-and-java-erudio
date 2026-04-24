package br.com.erudio.integrationtests.controllers.withYml;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.controllers.withYml.mapper.YAMLMapper;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerWithYml extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static RequestSpecification specification2;
    private static YAMLMapper objectMapperToYaml;
    private static PersonDTO person;

    @Autowired
    private PersonRepository repository;

    @BeforeAll
    static void setUp() {
        objectMapperToYaml = new YAMLMapper();
        person = new PersonDTO();
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        var content = given().config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapperToYaml);

        assertEquals("Grasieli", content.getFirstName());
    }

    @Test
    @Order(5)
    void findAll() throws JsonProcessingException {
        var content = given().config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO[].class, objectMapperToYaml);

        List<PersonDTO> people = Arrays.asList(content);

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

        var retorno = given().config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .body(person, objectMapperToYaml)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapperToYaml);

        person = retorno;
        assertTrue(retorno.getId() > 0);

    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        person.setFirstName("Grasieli");

        var retorno = given().config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .body(person, objectMapperToYaml)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapperToYaml);

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

        var result = given().config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification2)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapperToYaml);

        person = result;

        assertFalse(result.getEnabled());
    }

    @Test
    @Order(6)
    void delete() throws JsonProcessingException{
        var content = given().config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
        ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    private Map<String,String> header() {
        return Map.of(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB,
                TestConfigs.ACCEPT,TestConfigs.MEDIATYPEYAML);
    }

    private void init() {
        person.setFirstName("Pedro");
        person.setLastName("Batista");
        person.setGender("Mas");
        person.setEnabled(true);
        person.setAddress("Toledo");
    }
}