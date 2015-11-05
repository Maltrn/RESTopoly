package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 29.10.15.
 */
public class Field {

    private Place place;
    private ArrayList<Player> players = new ArrayList<Player>();

    public Field(){}

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
