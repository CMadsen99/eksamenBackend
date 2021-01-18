package dto;

import entities.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christianmadsen
 */
public class UsersDTO {

    List<UserDTO> all = new ArrayList();

    public UsersDTO(List<User> userEntities) {
        userEntities.forEach((u) -> {
            all.add(new UserDTO(u));
        });
    }

    public List<UserDTO> getAll() {
        return all;
    }

}
