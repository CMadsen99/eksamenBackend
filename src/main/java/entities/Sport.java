package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author christianmadsen
 */
@Entity
@NamedQuery(name = "Sport.deleteAllRows", query = "DELETE from Sport")
public class Sport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sport_name", length = 25)
    private String sportName;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sport_description")
    private String sportDescription;
    
    @OneToMany(mappedBy = "sport", cascade = CascadeType.PERSIST)
    List<SportTeam> sportTeams;

    public Sport() {
    }

    public Sport(String sportName, String sportDescription) {
        this.sportName = sportName;
        this.sportDescription = sportDescription;
        this.sportTeams = new ArrayList<>();
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

    public List<SportTeam> getSportTeams() {
        return sportTeams;
    }
    
    public void addSportTeam(SportTeam sportTeam) {
        if (sportTeam != null) {
            this.sportTeams.add(sportTeam);
            sportTeam.setSport(this);
        }
    }


    
}
