/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.SportDTO;
import dto.SportTeamDTO;
import entities.Sport;
import entities.SportTeam;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Acer
 */
public class SportFacadeTest {
    
    private static EntityManagerFactory emf;
    private static SportFacade facade;
    Sport s1;
    Sport s2;
    Sport s3;
    SportTeam st1;
    SportTeam st2;
    SportTeam st3;
        
    public SportFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = SportFacade.getSportFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("SportTeam.deleteAllRows").executeUpdate();
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            s1 = new Sport("Fodbold", "Spark til bolden");
            s2 = new Sport("Håndbold", "Kast bolden");
            s3 = new Sport("Basketbold", "Skyd bolden");
            
            st1 = new SportTeam(600, "BSF", 5, 99);
            st2 = new SportTeam(900, "Skovlunde Håndbold", 9, 75);
            st3 = new SportTeam(550, "Ballerup Basket", 7, 82);

            em.getTransaction().begin();
            s1.addSportTeam(st1);
            s2.addSportTeam(st2);
            s3.addSportTeam(st3);

            em.createNamedQuery("SportTeam.deleteAllRows").executeUpdate();
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();

            em.persist(s1);
            em.persist(s2);
            em.persist(s3);

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
    public void testGetAllSports() {
        assertEquals(3, facade.getAllSports().getAll().size(), "Expect three sports");
    }
    
    @Test
    public void testGetAllSportTeams() {
        assertEquals(3, facade.getAllSportTeams().getAll().size(), "Expect three sportteams");
    }
    
    @Test
    public void testAddSport() {
        SportDTO sDTO = new SportDTO("Baseball", "Sving battet");
        SportDTO sAdded = facade.addSport(sDTO);
        assertEquals(sDTO.getSportName(), sAdded.getSportName(), "Expect the same sportname");
        assertEquals(4, facade.getAllSports().getAll().size(), "Except four sports");
    }
    
    @Test
    public void testAddSportTeam() {
        SportTeamDTO stDTO = new SportTeamDTO(789, "KB", 5, 99);
        SportTeamDTO stAdded = facade.addSportTeam(stDTO, s1.getSportName());
        assertEquals(stDTO.getPricePerYear(), stAdded.getPricePerYear(), "Expect the same price per year");
        assertEquals(4, facade.getAllSportTeams().getAll().size(), "Except four sport teams");
    }
    
    @Test
    public void testEditSportTeam() {
        SportTeamDTO stDTO = new SportTeamDTO(st1);
        stDTO.setTeamName("KB");
        SportTeamDTO stEdited = facade.editSportTeam(stDTO, st1.getId());
        assertEquals(stEdited.getTeamName(), stDTO.getTeamName(), "Except the same team name");
    }
    
    @Test
    public void testDeleteSportTeam() {
        SportTeamDTO stDTO = facade.deleteSportTeam(st3.getId());
        assertEquals(2, facade.getAllSportTeams().getAll().size(), "Excepts two sport teams");
    }
    
}
