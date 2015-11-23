package restopoly.resources;

import restopoly.resources.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Krystian.Graczyk on 27.10.15.
 */
public class Game {

    private String gameid;
    private ArrayList<Player> players = new ArrayList<Player>();
    private Components components = new Components();
    private boolean started = false;

    public Game(){

        this.gameid = UUID.randomUUID().toString();
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public Player getPlayer(String playerid){
        for(Player player : players){
            if(player.getId().equals(playerid)) return player;
        }
        return null;
    }

    public boolean deletePlayer(String playerid){
        for(Player player : players){
            if(player.getId().equals(playerid)) {
                players.remove(player);
                return true;
            }
        }
        return false;
    }

    public String getGameid() {
        return gameid;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

}
