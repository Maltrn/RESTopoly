package restopoly.services;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Components;
import restopoly.resources.Game;
import restopoly.resources.Mutex;
import restopoly.resources.Player;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Service;

import java.util.ArrayList;

import static spark.Spark.*;

public class GameService{

    private static ArrayList<Game> games = new ArrayList<>();

    public static void main(String[] args) {

//        port(9090);

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
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.uri"))
                    .create();
            return gson.toJson(games);
        });

        post("/games", (req, res) -> {

            Game reqGame = new Gson().fromJson(req.body().toString(),Game.class);

            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.players"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.started"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.components"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.uri"))
                    .create();

            if (reqGame == null){
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
            Unirest.put(restopoly.util.Ports.BANKSADDRESS + "/"+game.getGameid()).asString();
            Unirest.put(restopoly.util.Ports.BOARDSADDRESS + "/"+game.getGameid()).asString();
//                Unirest.put("http://localhost:8090/boards/"+game.getGameid()).asString();
                return gson.toJson(game);
            }
            games.add(reqGame);
            mutex.addGame(reqGame.getGameid());
//            Unirest.put("http://localhost:8090/boards/"+reqGame.getGameid()).asString();
            Unirest.put(restopoly.util.Ports.BANKSADDRESS + "/"+reqGame.getGameid()).asString();
            Unirest.put(restopoly.util.Ports.BOARDSADDRESS + "/"+reqGame.getGameid()).asString();
            return gson.toJson(reqGame);
        });

        get("/games/:gameid", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.components"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.uri"))
                    .create();
            for(Game game : games){
                if(game.getGameid().equals(req.params(":gameid"))){
                    res.status(200);
                    return gson.toJson(game);
                }
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

        get("/games/:gameid/players/turn", (req, res) -> {
            res.status(400);
            String gameid = req.params(":gameid");
            String playerid = mutex.playerWithMutex(gameid);

            if (playerid != null && !playerid.isEmpty()) {
                res.status(200);
                Gson gson = new Gson();
                return gson.toJson(getGame(gameid).getPlayer(playerid));
            }
            return "";
        });

// ------------------------------- Aufgabe 2.2 A ; 2 --------------------------------------------------------
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
            player.setPosition(0);
            Unirest.put(restopoly.util.Ports.BOARDSADDRESS+"/"+req.params(":gameid")+"/players/"+req.params(":playerid")).body(new Gson().toJson(player)).asString();
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            game.addPlayer(player);
            mutex.addPlayer(game.getGameid(), player.getId());
            return "";
        });

        delete("/games/:gameid/players/turn", (req, res) -> {
            String gameid = req.params(":gameid");
            if(getGame(gameid) != null){
                mutex.addTurn(mutex.playerWithMutex(gameid), gameid);
            }
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

        put("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))) {
                    game = g;
                }
            }

            Player player = game.getPlayer(req.params(":playerid"));

            String gameId = req.params(":gameid");

            for (Player p : game.getPlayers()) {
                if (!p.isReady()) p.setReady(true);
                player.setReady(true);
            }

            if(!mutex.isMutexFree(gameId)) {
                mutex.releaseMutex(gameId);
            }

            return "";

        });

        get("/games/:gameid/players/:playerid/ready", (req, res) -> {
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))){
                    game = g;
                }
            }
            Player player = game.getPlayer(req.params(":playerid"));
            Gson gson = new GsonBuilder().create();
            return gson.toJson(player.isReady());
        });

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

        put("/games/:gameid/players/:playerid/turn", (req, res) -> {
//          TODO - Player wird als RequestBody übergeben - weitere Verwendung?
//            Player player = new Gson().fromJson(req.body().toString(),Player.class);
            res.status(409);
            String gameid = req.params(":gameid");
            String playerid  = req.params(":playerid");

            if(mutex.mutexBlockedByPlayer(gameid, playerid)){
                res.status(200);
            }
            if (mutex.isMutexFree(gameid)){
                res.status(201);
                mutex.changeMutexToPlayer(gameid, playerid);
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
                    .queryString("uri", restopoly.util.Ports.GAMESADDRESS)
                    .body(new Gson().toJson(new Service("GAMES", "Games Service", "games", restopoly.util.Ports.GAMESADDRESS)))
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
