package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import restopoly.DTO.PlayerBoardDTO;
import restopoly.DTO.RollDTO;
import restopoly.resources.*;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Ports;
import restopoly.util.Service;

import java.util.ArrayList;

import static restopoly.util.Ports.BOARDSADDRESS;
import static restopoly.util.Ports.BOARD_KEY;
import static restopoly.util.Ports.GAMESADDRESS;
import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class BoardService {
    private static ArrayList<Board> boards = new ArrayList<>();

    public static void main(String[] args) {
//TODO - später auskommentieren
        port(4568);

        get("/boards", (req, res) -> {
            res.status(HttpStatus.SC_OK);
            res.header("Content-Type", "application/json");
            return new Gson().toJson(boards);
        });

        put("/boards/:gameid", (req, res) -> {
            res.status(HttpStatus.SC_OK);
            res.header("Content-Type", "application/json");
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));

            Board board = new Board(req.params(":gameid"));
            String uri_brooker = req.headers(Ports.BROOKER_KEY);
            ArrayList<Field> fields = new ArrayList<>();
            fields.add(new Field(new Place(Ports.BOARDSADDRESS+req.params(":gameid")+"/places/0"), new ArrayList<>()));
            fields.add(new Field(new Place(Ports.BOARDSADDRESS+req.params(":gameid")+"/places/1"), new ArrayList<>()));
            fields.add(new Field(new Place(Ports.BOARDSADDRESS+req.params(":gameid")+"/places/2"), new ArrayList<>()));
            fields.add(new Field(new Place(Ports.BOARDSADDRESS+req.params(":gameid")+"/places/3"), new ArrayList<>()));

            board.setFields(fields);
            for(Board b: boards){
                if(b.getGameid()==req.params(":gameid")){
                    return "";
                }
            }
            boards.add(board);
            Unirest.put(req.headers(Ports.BROOKER_KEY) + "/" + req.params(":gameid"));

//            Unirest.put(uri_brooker + "/" + req.params(":gameid"))
            Unirest.put(Ports.BROKERSADDRESS + "/" + req.params(":gameid"))
                    .header(Ports.GAME_KEY, Ports.GAMESADDRESS)
                    .header(Ports.DICE_KEY, Ports.DICEADDRESS)
                    .header(Ports.BANK_KEY, Ports.BANKSADDRESS)
                    .header(Ports.BOARD_KEY, Ports.BOARDSADDRESS)
                    .header(Ports.EVENT_KEY, Ports.EVENTSADDRESS)
                    .asString();

            for(Field field : fields){
                Estate tempEstate = new Estate(Ports.BOARDSADDRESS+"/fields/places/"+field.getPlace().getName(), "", "2000", "500", "1000", "0", "", "");
                Unirest.put(Ports.BROKERSADDRESS + "/" + req.params(":gameid") + "/places/" + field.getPlace().getName()).body(new Gson().toJson(tempEstate)).asString();
            }
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
            }
            return "";
        });

//        returns a list of all player positions
//        response: [{"id":"Mario","place":"/boards/42/places/4", "position":4}]
        get("/boards/:gameid/players", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));
            ArrayList<Player> result = new ArrayList<Player>();
            int i = 0;
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid")))
                    res.status(200);
                    for(Field f :b.getFields()){
                        for(Player p : f.getPlayers()) {
                            result.add(p);
                            res.header("player_on_board_"+i,req.headers(Ports.BOARD_KEY)+"/players/"+p.getId());
                            i++;
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
            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));
            res.header(Ports.KEY_PLAYER_ON_BOARD_ROLL,req.headers(Ports.KEY_BOARDS_PLAYER)+ req.params(":playerid") +"/roll");
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


// ------------------------------- Aufgabe 2.2 A ; 1 --------------------------------------------------------
        post("/boards/:gameid/players/:playerid/roll", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");

            Gson gsonMutex = new Gson();
//            HttpResponse playerResponse  = Unirest.get(GAMESADDRESS + "/" + req.params(":gameid") + "/players/turn").asJson();
            HttpResponse playerResponse  = Unirest.get(GAMESADDRESS + "/" + req.params(":gameid") + "/players/turn").asJson();

            Player mutexPlayer = gsonMutex.fromJson(playerResponse.getBody().toString(), Player.class);

            if (mutexPlayer!= null && mutexPlayer.getId().equals(req.params(":playerid"))) {

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

                    Event event = new Event("type", "Player throwed " + (roll1 + roll2),"resource", "reason", p);
                    Unirest.post(restopoly.util.Ports.EVENTSADDRESS).queryString("gameid", req.params(":gameid")).body(new Gson().toJson(event)).asString();

                    HttpResponse tempEvents = Unirest.post(Ports.BROKERSADDRESS + "/" + g_ID + "/places/" + p.getPosition() + "/visit/" + p.getId()).asString();
                    Gson gsonEvents = new Gson();
                    Event[] resultEvents = gsonEvents.fromJson(tempEvents.getBody().toString(), Event[].class);

                    playerBoardDTO = new PlayerBoardDTO(p, b, resultEvents);

                    return gson.toJson(playerBoardDTO);
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
