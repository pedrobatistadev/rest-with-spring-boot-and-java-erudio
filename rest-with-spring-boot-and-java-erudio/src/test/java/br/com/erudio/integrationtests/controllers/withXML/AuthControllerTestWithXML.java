package br.com.erudio.integrationtests.controllers.withXML;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.AccountCredentialsDTO;
import br.com.erudio.integrationtests.dto.TokenDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTestWithXML extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() {

        AccountCredentialsDTO credentials = new AccountCredentialsDTO("pedro.batista", "123456");

        var content = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .body(credentials)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                .as(TokenDTO.class);

        tokenDTO = content;

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());

    }

    @Test
    @Order(2)
    void refreshToken() {

        var content = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("username", tokenDTO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        tokenDTO = content;

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}