/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Sport;

/**
 *
 * @author Acer
 */
public class SportDTO {
    
    String sportName;
    String sportDescription;

    public SportDTO(String sportName, String sportDescription) {
        this.sportName = sportName;
        this.sportDescription = sportDescription;
    }
    
    public SportDTO(Sport sport) {
        this.sportName = sport.getSportName();
        this.sportDescription = sport.getSportDescription();
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportDescription() {
        return sportDescription;
    }

    public void setSportDescription(String sportDescription) {
        this.sportDescription = sportDescription;
    }
    
    
    
}
