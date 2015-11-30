package restopoly.services;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Service;
import restopoly.resources.*;

import java.util.ArrayList;

import static spark.Spark.*;

public class GameService {
    public static void main(String[] args) {

        ArrayList<Game> games = new ArrayList<Game>();
        String bankaddress= "http://vs-docker.informatik.haw-hamburg.de:18192/banks/";
        String boardaddress = "http://vs-docker.informatik.haw-hamburg.de:18193/boards/";
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
            return gson.toJson(games);
        });

        post("/games", (req, res) -> {
            res.status(201);
            res.header("Content-Type", "application/json");
            Game game = new Game();
            Components components = game.getComponents();
            components.setGame(req.queryParams("gameUri"));
            components.setDice(req.queryParams("diceUri"));
            components.setBank(req.queryParams("bankUri"));
            components.setBoard(req.queryParams("boardUri"));
            components.setEvent(req.queryParams("eventUri"));
            games.add(game);
            Unirest.put(bankaddress+"/"+game.getGameid()).asString();
            Unirest.put(boardaddress+"/"+game.getGameid()).asString();


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
            for(Game game : games){
                if(game.getGameid().equals(req.params(":gameid"))) return gson.toJson(game);
            }
            return "";
        });

        get("/games/:gameid/players", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            Gson gson = new GsonBuilder().create();
            return gson.toJson(game.getPlayers());
        });

        get("/games/:gameid/players/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.position"))
                    .create();
            return gson.toJson(game.getPlayer(req.params(":playerid")));
        });

        put("/games/:gameid/players/:playerid", (req, res) -> {
            Player player = new Player(req.params(":playerid"));
            player.setName(req.queryParams("name"));
            player.setUri(req.queryParams("uri"));
            player.setPosition(0);
            Unirest.put(boardaddress+"/"+req.params(":gameid")+"/players/"+req.params(":playerid")).
                    body(new Gson().toJson(player)).asString();
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            game.addPlayer(player);
            return "";
        });

        delete("/games/:gameid/players/:playerid", (req, res) -> {
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            game.deletePlayer(req.params(":playerid"));
            return "";
        });

        put("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
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
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
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
