package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SportDTO;
import dto.SportTeamDTO;
import dto.SportTeamsDTO;
import dto.SportsDTO;
import errorhandling.MissingInputException;
import facades.SportFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 *
 * @author christianmadsen
 */
@Path("sports")
public class SportResource {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final SportFacade FACADE = SportFacade.getSportFacade(EMF);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String addSport(String sport) throws MissingInputException {
        SportDTO sDTO = GSON.fromJson(sport, SportDTO.class);
        SportDTO sAdded = FACADE.addSport(sDTO);
        return GSON.toJson(sAdded);
    }
    
    @Path("/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllSports() {
        SportsDTO ssDTO = FACADE.getAllSports();
        return GSON.toJson(ssDTO);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path("teams/{sport}")
    public String addSportTeam(@PathParam("sport") String sport, String sTeam) {
        SportTeamDTO stDTO = GSON.fromJson(sTeam, SportTeamDTO.class);
        SportTeamDTO stAdded = FACADE.addSportTeam(stDTO, sport);
        return GSON.toJson(stAdded);
    }
    
    @Path("teams/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllSportTeams() {
        SportTeamsDTO stsDTO = FACADE.getAllSportTeams();
        return GSON.toJson(stsDTO);
    }
    
    @Path("teams/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String deleteSportTeam(@PathParam("id") int id) {
        SportTeamDTO stDeleted = FACADE.deleteSportTeam(id);
        return GSON.toJson(stDeleted);
    }

    @Path("teams/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String editSportTeam(@PathParam("id") int id, String sportTeam) {
        SportTeamDTO stDTO = GSON.fromJson(sportTeam, SportTeamDTO.class);
        SportTeamDTO stEdited = FACADE.editSportTeam(stDTO, id);
        return GSON.toJson(stEdited);
    }
    
}
