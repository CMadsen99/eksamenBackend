package dto;

import entities.SportTeam;

/**
 *
 * @author christianmadsen
 */
public class SportTeamDTO {
    
    int id;
    int pricePerYear;
    String teamName;
    int minAge;
    int maxAge;

    public SportTeamDTO(int pricePerYear, String teamName, int minAge, int maxAge) {
        this.pricePerYear = pricePerYear;
        this.teamName = teamName;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }
    
    public SportTeamDTO(SportTeam sportTeam) {
        this.id = sportTeam.getId();
        this.pricePerYear = sportTeam.getPricePerYear();
        this.teamName = sportTeam.getTeamName();
        this.minAge = sportTeam.getMinAge();
        this.maxAge = sportTeam.getMaxAge();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    
    
    public int getPricePerYear() {
        return pricePerYear;
    }

    public void setPricePerYear(int pricePerYear) {
        this.pricePerYear = pricePerYear;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
        
}
