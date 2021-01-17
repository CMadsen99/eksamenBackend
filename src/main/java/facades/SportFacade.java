/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.SportDTO;
import dto.SportTeamDTO;
import dto.SportTeamsDTO;
import dto.SportsDTO;
import entities.Sport;
import entities.SportTeam;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Acer
 */
public class SportFacade {
    
     private static EntityManagerFactory emf;

    private static SportFacade instance;

    private SportFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static SportFacade getSportFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SportFacade();
        }
        return instance;
    }
    
    public SportDTO addSport(SportDTO sDTO) {
            
        EntityManager em = emf.createEntityManager();

        try {

            Sport sport = new Sport(sDTO.getSportName(), sDTO.getSportDescription());

            em.getTransaction().begin();

            em.persist(sport);

            em.getTransaction().commit();

            return new SportDTO(sport);
        } finally {
            em.close();
        }
    }
    
    public SportsDTO getAllSports() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Sport> query = em.createQuery("SELECT s FROM Sport s", entities.Sport.class);
            List<Sport> sports = query.getResultList();
            SportsDTO all = new SportsDTO(sports);
            return all;
        } finally {
            em.close();
        }
    }
    
    public SportTeamDTO addSportTeam(SportTeamDTO stDTO, String sportName) {
            
        EntityManager em = emf.createEntityManager();

        try {

            SportTeam sTeam = new SportTeam(stDTO.getPricePerYear(), stDTO.getTeamName(), stDTO.getMinAge(), stDTO.getMaxAge());
            
            Sport sport = em.find(Sport.class, sportName);
            sport.addSportTeam(sTeam);

            em.getTransaction().begin();

            em.persist(sport);

            em.getTransaction().commit();

            return new SportTeamDTO(sTeam);
        } finally {
            em.close();
        }
    }
        
    public SportTeamsDTO getAllSportTeams() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<SportTeam> query = em.createQuery("SELECT st FROM SportTeam st", entities.SportTeam.class);
            List<SportTeam> sportTeams = query.getResultList();
            SportTeamsDTO all = new SportTeamsDTO(sportTeams);
            return all;
        } finally {
            em.close();
        }
    }
}
