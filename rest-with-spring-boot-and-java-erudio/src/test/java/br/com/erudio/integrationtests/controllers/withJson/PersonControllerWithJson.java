package br.com.erudio.integrationtests.controllers.withJson;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.dto.wrappers.json.WrapperPersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerWithJson extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeaders(header())
                .setBasePath("/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                .asString();

        PersonDTO result = objectMapper.readValue(content, PersonDTO.class);
        person = result;

        assertNotNull(result);
        assertEquals("Klaus", result.getFirstName());
        assertEquals("New Orleans - Luisiana - EUA", result.getAddress());

        assertTrue(person.getId() > 0);
    }

    @Test
    @Order(2)
    void findById() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO result = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(result.getId());
        assertEquals("Klaus", result.getFirstName());
    }

    @Test
    @Order(3)
    void disable() throws JsonProcessingException {
        RequestSpecification specification2 = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
                .setBasePath("/person/v1/atu")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO result = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(result.getId());
        assertEquals("Klaus", result.getFirstName());
        assertFalse(result.getEnabled());
    }

    @Test
    @Order(4)
    void update() throws JsonProcessingException {
        person.setFirstName("Elijah");

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .body(person)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO result = objectMapper.readValue(content, PersonDTO.class);

        assertEquals("Elijah", person.getFirstName());
    }

    @Test
    @Order(5)
    void findAll() throws JsonProcessingException {
        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO people = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> result = people.getEmbedded().getPeople();

        PersonDTO personOne = result.get(0);

        assertEquals("Mei", personOne.getFirstName());

        assertNotNull(result);

    }

    @Test
    @Order(6)
    void findByName() throws JsonProcessingException {
        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("firstName", "and")
                .queryParams(page())
                .when()
                .get("/findByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO dto = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> result = dto.getEmbedded().getPeople();

        PersonDTO self = result.get(0);

        assertNotNull(self);
        assertEquals("Amandie", self.getFirstName());

    }

    @Test
    @Order(7)
    void delete() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    private void mockPerson() {
        person.setFirstName("Klaus");
        person.setLastName("Mikaelson22");
        person.setAddress("New Orleans - Luisiana - EUA");
        person.setGender("Male");
        person.setEnabled(true);
    }

    private Map<String,String> header() {
        return Map.of(
                TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB,
                TestConfigs.ACCEPT, TestConfigs.MEDIATYPEJSON
        );
    }

    private Map<String,Object> page() {
        return Map.of("page",0,
                "size",12,
                "direction","asc");
    }
}