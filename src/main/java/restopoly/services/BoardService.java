package restopoly.services;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Board;
import restopoly.resources.Field;
import restopoly.resources.Place;
import restopoly.resources.Player;
import restopoly.util.Service;

import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class BoardService {

    public static void main(String[] args) {

        ArrayList<Board> boards = new ArrayList<Board>();
        String gameaddress = "http://vs-docker.informatik.haw-hamburg.de:18191/games/";

        get("/boards", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return new Gson().toJson(boards);
        });

        put("/boards/:gameid", (req, res) -> {
            Board board = new Board(req.params(":gameid"));
            ArrayList<Field> fields = new ArrayList<Field>();
            fields.add(new Field(new Place("0"), new ArrayList<Player>()));
            fields.add(new Field(new Place("1"), new ArrayList<Player>()));
            fields.add(new Field(new Place("2"), new ArrayList<Player>()));
            fields.add(new Field(new Place("3"), new ArrayList<Player>()));

            board.setFields(fields);
            boards.add(board);
            return "";
        });

        put("/boards/:gameid/players/:playerid", (req, res) -> {
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
            res.status(200);
            res.header("Content-Type", "application/json");
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    return new Gson().toJson(b);
            }
            return "";
        });

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
            res.status(200);
            res.header("Content-Type", "application/json");
            ArrayList<Player> result = new ArrayList<Player>();
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(Field f :b.getFields()){
                        for(Player p : f.getPlayers()) {
                            result.add(p);
                        }
                    }
            }
            return new Gson().toJson(result);
        });

        //TODO - Konstruktion des Boards generell richtig?
//        Gets a player
//        response: {"id":"Mario","place":"/boards/42/places/4", "position":4}
        get("/boards/:gameid/players/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(Field f :b.getFields()){
                        for(Player p : f.getPlayers()) {
                            if (p.getId().equals(req.params(":playerid"))) {
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

        //TODO
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
                                p.setPosition(p.getPosition() + Integer.valueOf(req.params(":number"))); // ggf. muss hier noch was angepasst werden
                                Place newPlace = new Place("boards/" + req.params(":gameid") +"/places/" + i);
                                p.setPlace(newPlace);
//                              TODO die Playerposition im Gameservice muss ebenfalls aktualisiert werden oder?
                            }
                        }
                    }
            }
            return "";
        });

        //TODO - was soll damit gemeint sein
//      gives a throw of dice from the player to the board - Returns the new board state and possible options to take
//      response: { player: "/boards/42/players/mario", board: { "fields":[{"place": "/boards/42/places/0" ,"players":[]},
//            {"place": "/boards/42/places/1" ,"players":["/boards/42/players/mario"]},
//            {"place": "/boards/42/places/2" ,"players":[]},
//            {"place": "/boards/42/places/3" ,"players":[]},
//            {"place": "/boards/42/places/4","players":[]} ] }, events: [ { action: "transfer", uri: "/banks/42/transfer/12345",
//              name:"Bank Transfer", reason:"Rent for Badstrasse" } ] }
        post("/games/:gameid/players/:playerid/roll", (req, res) -> {

            return "";
        });

        //TODO - ggf. eine Variable für die Referenz in Place anlegen
//      List of available place
//      response: ["/boards/42/places/0", "/boards/42/places/1"]
        get("/boards/:gameid/places", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            ArrayList<String> result = new ArrayList<>();
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    for(int i = 0; i < b.getFields().size(); i++){
                        // gibt aktuell die Places des Fields ohne Player zurück
                        if(b.getField(i).getPlayers().size() == 0) {
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
                    .queryString("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/18193/boards")
                    .body(new Gson().toJson(new Service("BOARD", "Board Service", "board", "https://vs-docker.informatik.haw-hamburg.de/ports/18193/boards")))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
