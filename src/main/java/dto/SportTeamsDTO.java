package dto;

import entities.SportTeam;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christianmadsen
 */
public class SportTeamsDTO {
    
    List<SportTeamDTO> all = new ArrayList();

    public SportTeamsDTO(List<SportTeam> sportTeamEntities) {
        sportTeamEntities.forEach((st) -> {
            all.add(new SportTeamDTO(st));
        });
    }

    public List<SportTeamDTO> getAll() {
        return all;
    }
    
}
