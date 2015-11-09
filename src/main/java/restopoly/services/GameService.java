package restopoly.services;
/**
 * Created by nickdiedrich on 19.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.CustomExclusionStrategy;
import restopoly.resources.Game;
import restopoly.resources.Games;
import restopoly.resources.Player;
import restopoly.resources.Roll;

import static spark.Spark.*;

public class GameService {
    public static void main(String[] args) {
        port(4567);
        String yellowPagesUrl = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

        Games games = new Games();


        get("/games", (req, res) -> {
            res.status(200);
            res.header("Content-Type","application/json");
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.ready"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.position"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.uri"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.started"))
                    .create();
            return gson.toJson(games.getGames());
        });

        post("/games", (req, res) -> {
            res.status(201);
            res.header("Content-Type","application/json");
            Game game = new Game();
            games.addGame(game);;
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.players"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Game.started"))
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
            Game game = games.getGame(req.params(":gameid"));;
            game.deletePlayer(req.params(":playerid"));
            return "";
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



        // REGISTRIERUNG DER SERVICES
        try {
            Unirest.post(yellowPagesUrl + "?name=GAMES_GET&description=Gives_you_a_list_of_all_games&service=games&uri=http://vs-docker.informatik.haw-hamburg.de/games").asJson();
            Unirest.post(yellowPagesUrl + "?name=GAMES_POST&description=Creates_a_new_game&service=games&uri=http://vs-docker.informatik.haw-hamburg.de/games").asJson();
            Unirest.post(yellowPagesUrl + "?name=GAMES_GAMEID_GET&description=Gives_you_information_about_the_specific_game&service=games&uri=http://vs-docker.informatik.haw-hamburg.de/games").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


    }
}
