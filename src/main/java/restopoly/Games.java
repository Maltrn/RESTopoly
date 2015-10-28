package restopoly;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 27.10.15.
 */
public class Games {

    private ArrayList<Game> games = new ArrayList<Game>();

    public Games(){}

    public ArrayList<Game> getGames() {
        return games;
    }

    public Game getGame(String gameid) {
        for(Game game : games){
            if(game.getGameid().equals(gameid)) return game;
        }
        return null;
    }
    public void addGame(Game game){
        games.add(game);
    }

}
