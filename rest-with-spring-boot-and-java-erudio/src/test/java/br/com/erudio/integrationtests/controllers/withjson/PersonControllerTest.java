package br.com.erudio.integrationtests.controllers.withjson;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.Mapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerTest extends AbstractIntegrationTest {

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
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
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

        assertTrue(result.getId() > 0);
    }

    @Test
    @Order(2)
    void createWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_WRONG)
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
                    .statusCode(403)
                .extract()
                    .body()
                .asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
                .setBasePath("/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

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
    @Order(4)
    void findByIdWithWrongOrigin() throws JsonProcessingException {

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_WRONG)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .setBasePath("/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void createV2() {
    }

    @Test
    void findAll() {
    }

    private void mockPerson() {
        person.setFirstName("Klaus");
        person.setLastName("Mikaelson22");
        person.setAddress("New Orleans - Luisiana - EUA");
        person.setGender("Male");
    }
}