/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.SportDTO;
import dto.SportTeamDTO;
import dto.UserDTO;
import entities.Role;
import entities.Sport;
import entities.SportTeam;
import entities.User;
import facades.SportFacade;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Acer
 */
public class SportResourceTest {

    private static EntityManagerFactory emf;
    private static SportFacade facade;
    User user;
    User admin;
    Role userRole;
    Role adminRole;
    Sport s1;
    Sport s2;
    SportTeam st1;
    SportTeam st2;

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @BeforeAll
    public static void setUpClass() throws IOException {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        httpServer.start();
        while (!httpServer.isStarted()) {
        }
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("SportTeam.deleteAllRows").executeUpdate();
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            user = new User("user", "hello", "user@mail.dk", "12345678");
            admin = new User("admin", "1234", "admin@mail.dk", "87654321");

            userRole = new Role("user");
            adminRole = new Role("admin");

            s1 = new Sport("Fodbold", "Spark til bolden");
            s2 = new Sport("Håndbold", "Kast bolden");

            st1 = new SportTeam(600, "BSF", 5, 99);
            st2 = new SportTeam(900, "Skovlunde Håndbold", 9, 75);

            em.getTransaction().begin();

            user.addRole(userRole);
            admin.addRole(adminRole);

            s1.addSportTeam(st1);
            s2.addSportTeam(st2);

            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("SportTeam.deleteAllRows").executeUpdate();
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(s1);
            em.persist(s2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testGetAllSports() throws Exception {

        List<SportDTO> sportsDTO;
        sportsDTO = given()
                .contentType("application/json")
                .when()
                .get("/sports/all/").then()
                .extract().body().jsonPath().getList("all", SportDTO.class);

        assertThat(sportsDTO, iterableWithSize(2));
    }

    @Test
    public void testAddSport() throws Exception {

        Sport s3 = new Sport("Baseball", "Sving battet");
        SportDTO s3DTO = new SportDTO(s3);
        login("admin", "1234");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(s3DTO)
                .when()
                .post("/sports")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("sportName", equalTo(s3.getSportName()))
                .body("sportDescription", equalTo(s3.getSportDescription()));

        List<SportDTO> sportsDTO;
        sportsDTO = given()
                .contentType("application/json")
                .when()
                .get("/sports/all/").then()
                .extract().body().jsonPath().getList("all", SportDTO.class);

        assertThat(sportsDTO, iterableWithSize(3));
    }

    @Test
    public void testGetAllSportTeams() throws Exception {

        List<SportTeamDTO> sportTeamsDTO;
        sportTeamsDTO = given()
                .contentType("application/json")
                .when()
                .get("/sports/teams/all/").then()
                .extract().body().jsonPath().getList("all", SportTeamDTO.class);

        assertThat(sportTeamsDTO, iterableWithSize(2));
    }

    @Test
    public void testAddSportTeam() throws Exception {

        SportTeam st3 = new SportTeam(500, "KB", 7, 95);
        st3.setId(0);
        SportTeamDTO st3DTO = new SportTeamDTO(st3);
        login("admin", "1234");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(st3DTO)
                .when()
                .post("/sports/teams/" + s1.getSportName())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("pricePerYear", equalTo(st3.getPricePerYear()))
                .body("teamName", equalTo(st3.getTeamName()))
                .body("minAge", equalTo(st3.getMinAge()))
                .body("maxAge", equalTo(st3.getMaxAge()));

        List<SportTeamDTO> sportTeamsDTO;
        sportTeamsDTO = given()
                .contentType("application/json")
                .when()
                .get("/sports/teams/all/").then()
                .extract().body().jsonPath().getList("all", SportTeamDTO.class);

        assertThat(sportTeamsDTO, iterableWithSize(3));
    }

    @Test
    public void testEditSportTeam() throws Exception {

        SportTeamDTO stDTO = new SportTeamDTO(st1);
        stDTO.setMaxAge(50);
        login("admin", "1234");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(stDTO)
                .when()
                .put("sports/teams/" + st1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("pricePerYear", equalTo(st1.getPricePerYear()))
                .body("teamName", equalTo(st1.getTeamName()))
                .body("minAge", equalTo(st1.getMinAge()))
                .body("maxAge", equalTo(stDTO.getMaxAge()));
    }
    
    @Test
    public void testDeleteSportTeam() throws Exception {
        login("admin", "1234");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .delete("sports/teams/" + st2.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
        
        List<SportTeamDTO> sportTeamsDTO;
        sportTeamsDTO = given()
                .contentType("application/json")
                .when()
                .get("/sports/teams/all/").then()
                .extract().body().jsonPath().getList("all", SportTeamDTO.class);

        assertThat(sportTeamsDTO, iterableWithSize(1));
    }
}
