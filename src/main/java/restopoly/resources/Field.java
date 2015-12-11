package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 29.10.15.
 */
public class Field {

    private Place place;
    private ArrayList<Player> players = new ArrayList<Player>();

    public Field(Place place,ArrayList<Player> players){
        this.place = place;
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void deletePlayer(Player player) {
        players.remove(player);
    }


    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }


}
