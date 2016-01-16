package restopoly.DTO;

import restopoly.resources.Place;
import restopoly.resources.Player;

/**
 * Created by final-work on 15.01.16.
 */
public class PlayerDTO {
    private String id;
    private Place place;
    private int position;

    public PlayerDTO(Player player){

        this.id = player.getId();
    }
}
