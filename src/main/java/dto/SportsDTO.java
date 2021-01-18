package dto;

import entities.Sport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christianmadsen
 */
public class SportsDTO {
    
    List<SportDTO> all = new ArrayList();

    public SportsDTO(List<Sport> sportEntities) {
        sportEntities.forEach((s) -> {
            all.add(new SportDTO(s));
        });
    }

    public List<SportDTO> getAll() {
        return all;
    }
    
}
