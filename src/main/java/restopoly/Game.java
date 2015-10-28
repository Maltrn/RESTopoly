package restopoly;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Krystian.Graczyk on 27.10.15.
 */
public class Game {

    private String gameid;
    private ArrayList<Player> players = new ArrayList<Player>();
    private Components components;
    private boolean started = false;

    public Game(){

        this.gameid = UUID.randomUUID().toString();
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public Player getPlayer(String playerid){
        for(Player player : players){
            if(player.getPlayerid().equals(playerid)) return player;
        }
        return null;
    }

    public String getGameid() {
        return gameid;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public String toString() {
        return
                "gameid:'" + gameid + '\'' +
                ", players:" + players +
                ", started:" + started +
                '}';
    }
}
