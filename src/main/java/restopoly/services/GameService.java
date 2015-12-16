package restopoly.services;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Ports;
import restopoly.util.Service;
import restopoly.resources.*;

import java.util.ArrayList;

import static spark.Spark.*;

public class GameService implements Ports{
    private static ArrayList<Game> games = new ArrayList<Game>();

    public static void main(String[] args) {

        Mutex mutex = new Mutex();


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
            mutex.addGame(game.getGameid());
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
            mutex.addPlayer(game.getGameid(), player.getId());
            return "";
        });

        delete("/games/:gameid/players/:playerid", (req, res) -> {
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            game.deletePlayer(req.params(":playerid"));
            mutex.removePlayer(req.params(":gameid"), req.params(":playerid"));
            return "";
        });

//      TODO - Mutex berücksichtigen?
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
//          TODO - Rückgabe laut raml -> nichts
//            return gson.toJson(player);
            return "";

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

//       gets the currently active player that shall take action
//        res 200 - { id:mario, name:"Mario", uri:"http://localhost:4567/player/mario", ready:false }
        get("/games/:gameid/players/current", (req, res) -> {
            res.status(400);
            String gameid = req.params(":gameid");
            String playerid = mutex.getNextPlayer(gameid);
            if (!playerid.isEmpty()) {
                res.status(200);
                Gson gson = new Gson();
                return gson.toJson(getGame(gameid).getPlayer(playerid));
            }
            return "";
        });

//      gets the player holding the turn mutex
//      res 200 - { id:mario, name:"Mario", uri:"http://localhost:4567/player/mario", ready:false }
//      res 404
        get("/games/:gameid/players/turn", (req, res) -> {
            res.status(400);
            String gameid = req.params(":gameid");
            String playerid = mutex.playerWithMutex(gameid);
            if (!playerid.isEmpty()) {
                res.status(200);
                Gson gson = new Gson();
                return gson.toJson(getGame(gameid).getPlayer(playerid));
            }
            return "";
        });

//      TODO - tries to aquire the turn mutex
//      responses: 200 - already holding the mutex,
//      201 - aquired the mutex,
//      409 - already aquired by an other player
        put("/games/:gameid/players/:playerid/turn", (req, res) -> {
            res.status(409);
            String gameid = req.params(":gameid");
            String playerid  = req.params(":playerid");

            if(mutex.playerHasMutex(gameid,playerid)){
                res.status(200);
            }
            if (mutex.isMutexFree(gameid)){
                res.status(201);
                mutex.changeMutexToPlayer(gameid,playerid);
            }
            return "";
        });

//      releases the mutex
//      res - keine
        delete("/games/:gameid/players/turn", (req, res) -> {
            String gameid = req.params(":gameid");
            if(getGame(gameid) != null){
                mutex.addTurn(gameid, mutex.playerWithMutex(gameid));
            }
            return "";
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

    public static Game getGame(String gameid){
        for (Game game : games){
            if(game.getGameid().equals(gameid)){
                return game;
            }
        }
        return null;
    }
}
