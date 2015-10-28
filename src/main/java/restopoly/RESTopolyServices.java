package restopoly;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static spark.Spark.*;

public class RESTopolyServices {
    public static void main( String[] args) {
        Games games = new Games();

        get("/dice", (req, res) -> {
            res.status(200);
            res.header("Content-Type","application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(new Roll());
        });

        get("/games", (req, res) -> {
            res.status(200);
            res.header("Content-Type","application/json");
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.Player.ready"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.Player.position"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.Player.uri"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.Game.started"))
                    .create();
            return gson.toJson(games.getGames());
        });

        post("/games", (req, res) -> {
            res.status(201);
            res.header("Content-Type","application/json");
            Game game = new Game();
            games.addGame(game);;
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.Game.players"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.Game.started"))
                    .create();
            return gson.toJson(game);
        });

        get("/games/:gameid", (req, res) -> {
            res.status(200);
            res.header("Content-Type","application/json");
            Gson gson = new GsonBuilder()
                    .create();
            return gson.toJson(games.getGame(req.params(":gameid")));
        });

        get("/games/:gameid/players", (req, res) -> {
            res.status(200);
            res.header("Content-Type","application/json");
            Game game = games.getGame(req.params(":gameid"));
            Gson gson = new GsonBuilder().create();
            return gson.toJson(game.getPlayers());
        });

        get("/games/:gameid/players/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type","application/json");
            Game game = games.getGame(req.params(":gameid"));
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.Player.position"))
                    .create();
            return gson.toJson(game.getPlayer(req.params(":playerid")));
        });

        put("/games/:gameid/players/:playerid", (req, res) -> {
            Player player = new Player(req.params(":playerid"));
            player.setName(req.queryParams("name"));
            player.setUri(req.queryParams("uri"));
            Game game = games.getGame(req.params(":gameid"));
            game.addPlayer(player);
            return null;
        });

        delete("/games/:gameid/players/:playerid", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));;
            game.deletePlayer(req.params(":playerid"));
            return null;
        });

        put("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            Player player = game.getPlayer(req.params(":playerid"));
            player.setReady(true);
            for(Player p : game.getPlayers()){
                if(!p.isReady()) return player;
            }
            game.setStarted(true);
            Gson gson = new GsonBuilder().create();
            return gson.toJson(player);
        });

        get("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            Player player = game.getPlayer(req.params(":playerid"));
            Gson gson = new GsonBuilder().create();
            return gson.toJson(player.isReady());
        });


     /*   get("/boards/:gameid/players/:playerid/roll", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            Player player = game.getPlayer(req.params(":playerid"));
            return player.isReady();
        });

        */

    }
}
