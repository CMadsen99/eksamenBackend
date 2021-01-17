/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Sport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
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
