package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import restopoly.DTO.PlayerBoardDTO;
import restopoly.DTO.RollDTO;
import restopoly.resources.Board;
import restopoly.resources.Field;
import restopoly.resources.Place;
import restopoly.resources.Player;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Ports;
import restopoly.util.Service;

import java.util.ArrayList;

import static restopoly.util.Ports.*;
import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class BoardService {
    private static ArrayList<Board> boards = new ArrayList<>();

    public static void main(String[] args) {

//        port(8090);

        get("/boards", (req, res) -> {
            res.status(HttpStatus.SC_OK);
            res.header("Content-Type", "application/json");
            return new Gson().toJson(boards);
        });

        put("/boards/:gameid", (req, res) -> {
            res.status(HttpStatus.SC_OK);
            Board board = new Board(req.params(":gameid"));
            String uri_brooker = req.headers(Ports.BROOKER_KEY);
            ArrayList<Field> fields = new ArrayList<>();
            fields.add(new Field(new Place("0"), new ArrayList<>()));
            fields.add(new Field(new Place("1"), new ArrayList<>()));
            fields.add(new Field(new Place("2"), new ArrayList<>()));
            fields.add(new Field(new Place("3"), new ArrayList<>()));

            board.setFields(fields);
            boards.add(board);
            System.out.println("BROKER_URI: " + uri_brooker);
//          TODO - Broker anlegen - später wieder rein
//            Unirest.put(uri_key + "/" + req.params(":gameid"))
//                    .header(Ports.GAME_KEY, Ports.GAMESADDRESS)
//                    .header(Ports.DICE_KEY, Ports.DICEADDRESS)
//                    .header(Ports.BANK_KEY, Ports.BANKSADDRESS)
//                    .header(Ports.BOARD_KEY, Ports.BOARDSADDRESS)
//                    .header(Ports.EVENT_KEY, Ports.EVENTSADDRESS)
//                    .asString();
            return "";
        });

        put("/boards/:gameid/players/:playerid", (req, res) -> {
//            TODO - Rückgabe????
            Player player = new Gson().fromJson(req.body().toString(),Player.class);

            Board board = null;
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid"))) board=b;
            }
            ArrayList<Field> fields = board.getFields();
            fields.get(0).addPlayer(player);
            return "";
        });

        get("/boards/:gameid", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid"))) {
                    res.status(200);
                    return new Gson().toJson(b);
                }
            }
            return "";
        });

// ------------------------------- Aufgabe 2.2 A ; 3 --------------------------------------------------------
        delete("/boards/:gameid", (req, res) -> {
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                   boards.remove(b);
//                TODO - Spiel muss unmittelbar beendet werden
            }
            return "";
        });

//        returns a list of all player positions
//        response: [{"id":"Mario","place":"/boards/42/places/4", "position":4}]
        get("/boards/:gameid/players", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");
            ArrayList<Player> result = new ArrayList<Player>();
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    res.status(200);
                    for(Field f :b.getFields()){
                        for(Player p : f.getPlayers()) {
                            result.add(p);
                        }
                    }
            }
            return new Gson().toJson(result);
        });

//        Gets a player
//        response: {"id":"Mario","place":"/boards/42/places/4", "position":4}
        get("/boards/:gameid/players/:playerid", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(Field f :b.getFields()){
                        for(Player p : f.getPlayers()) {
                            if (p.getId().equals(req.params(":playerid"))) {
                                res.status(200);
                                return new Gson().toJson(p);
                            }
                        }
                    }
            }
            return "";
        });


//        removes a player from the board
//        response: -
        delete("/boards/:gameid/players/:playerid", (req, res) -> {
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(Field f :b.getFields()){
                        for(Player p : f.getPlayers()) {
                            if (p.getId().equals(req.params(":playerid"))) {
                                f.deletePlayer(p);
                            }
                        }
                    }
            }
            return "";
        });

//      moves a player relative to its current position
//      response: -
        post("/boards/:gameid/players/:playerid/move", (req, res) -> {
            res.header("Content-Type", "application/json");
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(int i = 0; i < b.getFields().size(); i++){
                        for(Player p : b.getField(i).getPlayers()) {
                            if (p.getId().equals(req.params(":playerid"))) {

//                              kommt der Wurf aus dem Request??
                                p.setPosition(p.getPosition() + Integer.valueOf(req.params(":number"))); // TODO - ggf. muss hier noch was angepasst werden
                                Place newPlace = new Place("boards/" + req.params(":gameid") +"/places/" + i);
                                p.setPlace(newPlace);
//                              Position des Spielers wird im Gameservice ebenfalls aktualisiert
                                Gson gson = new Gson();
                                HttpResponse playerResponse  = Unirest.get(GAMESADDRESS + "/" + req.params(":gameid") + "/players/" + req.params(":playerid")).asJson();
                                Player newPlayer = gson.fromJson(playerResponse.getBody().toString(), Player.class);
                                newPlayer.setPlace(newPlace);
                                Unirest.delete(GAMESADDRESS + "/" + req.params(":gameid") + "/players/" + req.params(":playerid"));
                                Unirest.put(GAMESADDRESS + "/" + req.params(":gameid") + "/players/" + req.params(":playerid")).body(new Gson().toJson(newPlayer)).asString();
                            }
                        }
                    }
            }
            return "";
        });


//      gives a throw of dice from the player to the board - Returns the new board state and possible options to take
//      response: { player: "/boards/42/players/mario",
//            board: { "fields":[{"place": "/boards/42/places/0" ,"players":[]},
//            {"place": "/boards/42/places/1" ,"players":["/boards/42/players/mario"]},
//            {"place": "/boards/42/places/2" ,"players":[]},
//            {"place": "/boards/42/places/3" ,"players":[]},
//            {"place": "/boards/42/places/4","players":[]} ] },
//            events: [ { action: "transfer", uri: "/banks/42/transfer/12345",
//            name:"Bank Transfer", reason:"Rent for Badstrasse" } ] }

// ------------------------------- Aufgabe 2.2 A ; 1 --------------------------------------------------------
        post("/boards/:gameid/players/:playerid/roll", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");

            Gson gsonMutex = new Gson();
//            HttpResponse playerResponse  = Unirest.get(GAMESADDRESS + "/" + req.params(":gameid") + "/players/turn").asJson();
            HttpResponse playerResponse  = Unirest.get(GAMESADDRESS + "/games/" + req.params(":gameid") + "/players/turn").asJson();

            Player mutexPlayer = gsonMutex.fromJson(playerResponse.getBody().toString(), Player.class);

            if (mutexPlayer!= null && mutexPlayer.getId().equals(req.params(":playerid"))) {
                System.out.println("If- Player " +mutexPlayer.getId());

                RollDTO rollDto = new Gson().fromJson(req.body().toString(),RollDTO.class);

                int roll1 = rollDto.getRoll1().getNumber();
                int roll2 = rollDto.getRoll2().getNumber();

                String p_ID = req.params(":playerid");
                String g_ID = req.params(":gameid");

                Gson gson = new GsonBuilder()
                        .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.uri"))
                        .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Player.name"))
                        .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Board.gameid"))
                        .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Board.positions"))
                        .create();

                Player p= getPlayer(g_ID, p_ID);
                Board b = getGameB(g_ID);

                PlayerBoardDTO playerBoardDTO = null;
                if (b!=null && p != null){
                    res.status(200);

                    p.setPosition(p.getPosition()+(roll1+roll2));
                    playerBoardDTO = new PlayerBoardDTO(p, b);

                    return gson.toJson(playerBoardDTO);

//                  TODO - SPÄTER! -Nur ausgelöste Events sollen zurückgegeben werden
//                  TODO - possible Options to take???
//                HttpResponse eventRes  = Unirest.get(EVENTSADDRESS + "/event/"+g_ID).asJson();
//                Event event = gson.fromJson(eventRes.getBody().toString(), Event.class);
//
//                jsonObject.put("events", event);
                }
            }
            return "";
        });



//      TODO - ggf. eine Variable für die Referenz in Place anlegen
//      List of available place
//      response: ["/boards/42/places/0", "/boards/42/places/1"]
        get("/boards/:gameid/places", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            ArrayList<String> result = new ArrayList<>();
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(int i = 0; i < b.getFields().size(); i++){
                        // TODO - gibt aktuell die Places des Fields ohne Player zurück
                        if(b.getField(i).getPlayers().size() == 0) {
//                          TODO - welche Places sind available?
                            result.add("boards/" + req.params(":gameid") + "/places/" + i);
                        }
                    }
            }
            return new Gson().toJson(result);
        });

//      Gets a place
//      response: {"name":"Los"}
        get("/boards/:gameid/places/:place", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(Field f :b.getFields()){
                        if(f.getPlace().equals(req.params(":place"))){
                            return new Gson().toJson(f.getPlace());
                        }
                    }
            }
            return "";
        });

//      places a places
//      response: -
        put("/boards/:gameid/places/:place", (req, res) -> {
            res.header("Content-Type", "application/json");
            Place newPlace = new Place(req.body());
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(Field f :b.getFields()){
                        if(f.getPlace().equals(req.params(":place"))){
                            f.setPlace(newPlace);
                        }
                    }
            }
            return "";
        });


        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "BOARD")
                    .queryString("description", "Board Service")
                    .queryString("service", "boards")
                    .queryString("uri", BOARDSADDRESS)
                    .body(new Gson().toJson(new Service("BOARD", "Board Service", "board", BOARDSADDRESS)))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


    private static Player getPlayer(String gameID, String playerId) {
        for (Board b : boards) {
            if (b.getGameid().equals(gameID))
                for (int i = 0; i < b.getFields().size(); i++) {
                    for (Player p : b.getField(i).getPlayers()) {
                        if (p.getId().equals(playerId)) {
                            return p;
                        }
                    }
                }
        }
        return null;
    }

    private static Board getGameB(String game_id) {
        for (Board b : boards) {
            if (b.getGameid().equals(game_id))
                return b;
        }
        return null;
    }
}
