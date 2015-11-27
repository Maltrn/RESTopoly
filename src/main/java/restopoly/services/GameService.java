package restopoly.services;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.CustomExclusionStrategy;
import restopoly.Service;
import restopoly.resources.*;

import static spark.Spark.*;

public class GameService {
    public static void main(String[] args) {

        Games games = new Games();


        get("/games", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.ready"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.position"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.uri"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.started"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.components"))
                    .create();
            return gson.toJson(games.getGames());
        });

        post("/games", (req, res) -> {
            res.status(201);
            res.header("Content-Type", "application/json");
            Game game = new Game();
            Components components = game.getComponents();
            components.setGame(req.queryParams("gameUri"));
            components.setDice(req.queryParams("diceUri"));
            components.setBank(req.queryParams("bankUri"));
            games.addGame(game);
            Unirest.put("http://vs-docker.informatik.haw-hamburg.de:18192/banks/"+"/"+game.getGameid()).asString();
            Unirest.put("http://vs-docker.informatik.haw-hamburg.de:18193/boards/"+"/"+game.getGameid()).asString();


            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.players"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.started"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.components"))
                    .create();
            return gson.toJson(game);
        });

        get("/games/:gameid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder()
                    .create();
            return gson.toJson(games.getGame(req.params(":gameid")));
        });

        get("/games/:gameid/players", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Game game = games.getGame(req.params(":gameid"));
            Gson gson = new GsonBuilder().create();
            return gson.toJson(game.getPlayers());
        });

        get("/games/:gameid/players/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Game game = games.getGame(req.params(":gameid"));
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.position"))
                    .create();
            return gson.toJson(game.getPlayer(req.params(":playerid")));
        });

        put("/games/:gameid/players/:playerid", (req, res) -> {
            Player player = new Player(req.params(":playerid"));
            player.setName(req.queryParams("name"));
            player.setUri(req.queryParams("uri"));
            Game game = games.getGame(req.params(":gameid"));
            game.addPlayer(player);
            return "";
        });

        delete("/games/:gameid/players/:playerid", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            game.deletePlayer(req.params(":playerid"));
            return "";
        });

        put("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = games.getGame(req.params(":gameid"));
            Player player = game.getPlayer(req.params(":playerid"));
            player.setReady(true);
            for (Player p : game.getPlayers()) {
                if (!p.isReady()) return player;
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


        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "GAMES")
                    .queryString("description", "Games Service")
                    .queryString("service", "games")
                    .queryString("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/18191/games")
                    .body(new Gson().toJson(new Service("GAMES", "Games Service", "games", "https://vs-docker.informatik.haw-hamburg.de/ports/18191/games")))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();

        }
    }
}
