/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.SportTeam;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
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
