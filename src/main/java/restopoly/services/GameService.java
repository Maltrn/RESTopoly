package restopoly.services;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import restopoly.resources.Components;
import restopoly.resources.Game;
import restopoly.resources.Mutex;
import restopoly.resources.Player;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Ports;
import restopoly.util.Service;

import java.util.ArrayList;

import static spark.Spark.*;

public class GameService implements Ports{

    private static ArrayList<Game> games = new ArrayList<>();

    public static void main(String[] args) {

//TODO - später auskommentieren
        port(4567);

        Mutex mutex = new Mutex();

        get("/games", (req, res) -> {
            res.status(HttpStatus.SC_OK);
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
            res.header("Content-Type", "application/json");

            Game reqGame = new Gson().fromJson(req.body().toString(), Game.class);

            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.players"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.started"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.components"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.uri"))
                    .create();

            res.status(HttpStatus.SC_OK);
            if (reqGame == null) {
                res.status(HttpStatus.SC_CREATED);
                reqGame = new Game();
                Components components = reqGame.getComponents();
                components.setGame(req.headers(Ports.GAME_KEY));
                components.setDice(req.headers(Ports.DICE_KEY));
                components.setBank(req.headers(Ports.BANK_KEY));
                components.setBoard(req.headers(Ports.BOARD_KEY)+ "/" + reqGame.getGameid());
                components.setEvent(req.headers(Ports.EVENT_KEY));

                reqGame.setUri(Ports.GAMESADDRESS + "/" + reqGame.getGameid());
            }
            for(Game g: games){
                if(g.getGameid()==reqGame.getGameid()){
                    return "";
                }
            }
            // Client from GUI
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY) + "/" + reqGame.getGameid());
            System.out.println("GameService: " + req.headers(Ports.GAME_KEY) + "/" + reqGame.getGameid());
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY) + "/" + reqGame.getGameid());
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY) + "/" + reqGame.getGameid());
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY) + "/" + reqGame.getGameid());
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY) +"/"+ reqGame.getGameid());

            games.add(reqGame);
            mutex.addGame(reqGame.getGameid());
//            Unirest.put(restopoly.util.Ports.BANKSADDRESS + "/"+reqGame.getGameid())
            Unirest.put(req.headers(Ports.BANK_KEY) + "/"+reqGame.getGameid())
                    .header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY) + "/" + reqGame.getGameid())
                    .header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY))
                    .header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY) + "/" + reqGame.getGameid())
                    .header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY) + "/" + reqGame.getGameid())
                    .header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY)+ "/" + reqGame.getGameid())
                    .asString();

//                Unirest.put(Ports.BOARDSADDRESS + "/" + reqGame.getGameid())
            Unirest.put(req.headers(Ports.BOARD_KEY) + "/" + reqGame.getGameid())
                    .header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY) + "/" + reqGame.getGameid())
                    .header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY))
                    .header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY) + "/" + reqGame.getGameid())
                    .header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY) + "/" + reqGame.getGameid())
                    .header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY) + "/" + reqGame.getGameid())
                    .asString();
            return gson.toJson(reqGame);
        });

        get("/games/:gameid", (req, res) -> {
            res.status(HttpStatus.SC_NOT_FOUND);
            res.header("Content-Type", "application/json");
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));
            res.header(Ports.KEY_GAME_PLAYER, req.headers(Ports.GAME_KEY)+ "/players/");
            res.header(Ports.KEY_BOARDS_PLAYER, req.headers(Ports.BOARD_KEY)+ "/players/");

            Gson gson = new GsonBuilder()
             //       .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.components"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.uri"))
                    .create();
            for (Game game : games) {
                if (game.getGameid().equals(req.params(":gameid"))) {
                    res.status(HttpStatus.SC_OK);
                    return gson.toJson(game);
                }
            }
            return "";
        });

        get("/games/:gameid/players", (req, res) -> {
            res.status(HttpStatus.SC_OK);
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
            res.status(HttpStatus.SC_OK);
            res.header("Content-Type", "application/json");
            Game game = null;
            for (Game g : games) {
                if (g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.position"))
                    .create();
            return gson.toJson(game.getPlayer(req.params(":playerid")));
        });

        put("/games/:gameid/players/:playerid", (req, res) -> {
            res.status(HttpStatus.SC_OK);
            // Client from GUI
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));
            res.header(Ports.KEY_PLAYER_GAME_READY, req.headers(Ports.KEY_GAME_PLAYER)+"/ready");
            res.header(Ports.KEY_BOARDS_PLAYER, req.headers(Ports.KEY_BOARDS_PLAYER));
            res.header(Ports.KEY_GAME_PLAYER, req.headers(Ports.KEY_GAME_PLAYER));

            Player player = new Player(req.params(":playerid"));
            player.setName(req.queryParams("name"));
            player.setPosition(0);
//            Unirest.put(restopoly.util.Ports.BOARDSADDRESS + "/" + req.params(":gameid") + "/players/" + req.params(":playerid")).body(new Gson().toJson(player)).asString();
            Unirest.put(req.headers(Ports.KEY_BOARDS_PLAYER)).body(new Gson().toJson(player)).asString();
            Game game = null;
            for (Game g : games) {
                if (g.getGameid().equals(req.params(":gameid"))) game = g;
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
            for (Game g : games) {
                if (g.getGameid().equals(req.params(":gameid"))) game = g;
            }
            game.deletePlayer(req.params(":playerid"));
            mutex.removePlayer(req.params(":gameid"), req.params(":playerid"));
            return "";
        });

        put("/games/:gameid/players/:playerid/ready", (req, res) -> {
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));
            res.header(Ports.KEY_PLAYER_TURN, req.headers(Ports.KEY_GAME_PLAYER)+"/turn");



            Game game = null;
            for (Game g : games) {
                if (g.getGameid().equals(req.params(":gameid"))) {
                    game = g;
                }
            }

            Player player = game.getPlayer(req.params(":playerid"));

            String gameId = req.params(":gameid");

            for (Player p : game.getPlayers()) {
                if (!p.isReady()) p.setReady(true);
                player.setReady(true);
            }

            if (!mutex.isMutexFree(gameId)) {
                mutex.releaseMutex(gameId);
            }

            return "";

        });

        get("/games/:gameid/players/:playerid/ready", (req, res) -> {
            res.status(HttpStatus.SC_OK);
            res.header("Content-Type", "application/json");
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));
            res.header(Ports.KEY_GAME_PLAYER, req.headers(Ports.KEY_GAME_PLAYER));
            res.header(Ports.KEY_PLAYER_TURN, req.headers(Ports.KEY_GAME_PLAYER)+"/turn");
            res.header(KEY_BOARDS_PLAYER, req.headers(KEY_BOARDS_PLAYER));
            Game game = null;
            for(Game g : games){
                if(g.getGameid().equals(req.params(":gameid"))){
                    game = g;
                }
            }
            Player player = game.getPlayer(req.params(":playerid"));
            player.setReady(true);
            Gson gson = new GsonBuilder().create();
            return gson.toJson(player.isReady());
        });

        get("/games/:gameid/players/current", (req, res) -> {
            res.status(400);
            String gameid = req.params(":gameid");
            String playerid = mutex.getNextPlayer(gameid);
            if (!playerid.isEmpty()) {
                res.status(HttpStatus.SC_OK);
                Gson gson = new Gson();
                return gson.toJson(getGame(gameid).getPlayer(playerid));
            }
            return "";
        });

        get("/games/:gameid/players/turn", (req, res) -> {
            System.out.println("Player with Mutex: " + mutex.playerWithMutex(req.params(":gameid")));
            res.status(HttpStatus.SC_BAD_REQUEST);
            String gameid = req.params(":gameid");
            String playerid = mutex.playerWithMutex(gameid);

            if (!playerid.isEmpty()) {
                res.status(HttpStatus.SC_OK);
                Gson gson = new Gson();
                return gson.toJson(getGame(gameid).getPlayer(playerid));
            }
            return "";
        });

        put("/games/:gameid/players/:playerid/turn", (req, res) -> {
//          TODO - Player wird als RequestBody übergeben - weitere Verwendung?
//            Player player = new Gson().fromJson(req.body().toString(),Player.class);
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));
            res.header(Ports.KEY_ONBANK,req.headers(Ports.BANK_KEY)+"/players/"+req.params(":playerid"));
            res.header(Ports.KEY_ONBOARD,req.headers(Ports.BOARD_KEY)+"/players/"+req.params(":playerid"));
            res.header(KEY_BOARDS_PLAYER, req.headers(KEY_BOARDS_PLAYER));
            res.header(KEY_PLAYER_TURN, req.headers(KEY_PLAYER_TURN));
            res.status(HttpStatus.SC_CONFLICT);
            String gameid = req.params(":gameid");
            String playerid = req.params(":playerid");


            if (mutex.mutexBlockedByPlayer(gameid, playerid)) {
                System.out.println("mutexBlockedByPlayer");
                res.status(HttpStatus.SC_OK);
            }
            if (mutex.isMutexFree(gameid)) {
                res.status(HttpStatus.SC_CREATED);
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
