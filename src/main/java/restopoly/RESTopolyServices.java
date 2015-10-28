package restopoly;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import static spark.Spark.*;

public class RESTopolyServices {
    public static void main( String[] args) {
        Games games = new Games();

        get("/games", (req, res) -> {
            res.status(200);
            return games;
        });

        post("/games", (req, res) -> {
            Game game = new Game();
            games.addGame(game);
            res.status(201);
            return "gameid:" + game.getGameid();
        });

        get("/games/:gameid/players", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            return game.getPlayers();
        });

        put("/games/:gameid/players/:playerid", (req, res) -> {
            Player player = new Player(req.params(":playerid"));
            Game game = games.getGame(req.params(":gameid"));
            game.addPlayer(player);
            return player.getPlayerid();
        });

        put("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            Player player = game.getPlayer(req.params(":playerid"));
            player.setReady(true);
            for(Player p : game.getPlayers()){
                if(!p.isReady()) return player;
            }
            game.setStarted(true);
            return player;
        });

        get("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            Player player = game.getPlayer(req.params(":playerid"));
            return player.isReady();
        });


     /*   get("/boards/:gameid/players/:playerid/roll", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            Player player = game.getPlayer(req.params(":playerid"));
            return player.isReady();
        });

        */

        get("/dice", (req, res) -> {
            res.status(200);
            return new Roll();
        });
    }
}
