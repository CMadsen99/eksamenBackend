package dto;

import entities.Sport;

/**
 *
 * @author christianmadsen
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
