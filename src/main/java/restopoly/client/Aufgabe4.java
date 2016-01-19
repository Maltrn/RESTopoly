package restopoly.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Game;
import restopoly.resources.Player;
import restopoly.util.Ports;

/**
 * Created by mizus on 19.01.16.
 */
public class Aufgabe4 {



    public String createGame() throws UnirestException {
        HttpResponse response = Unirest.post(restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS)
                .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS)
                .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS)
                .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS)
                .asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(), Game.class);
        return game.getGameid();
    }

    //Aufgabe 4

    public String aufgabe_4_1() throws UnirestException {

        String id = createGame();

        HttpResponse response = Unirest.get(restopoly.util.Ports.GAMESADDRESS + "/" + id)
                .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS + "/" + id)
                .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS + "/" + id)
                .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS + "/" + id)
                .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS + "/" + id)
                .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS + "/" + id)
                .asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(), Game.class);


        return response.getBody().toString();
    }


    public String aufgabe_4_2() throws UnirestException {
        aufgabe_4_1();
        HttpResponse response = Unirest.get(restopoly.util.Ports.GAMESADDRESS).asJson();
        Gson gson = new Gson();
        JsonArray gameList = gson.fromJson(response.getBody().toString(), JsonArray.class);
        for (JsonElement object: gameList){
            Game tgame = gson.fromJson(object, Game.class);
            String id = tgame.getGameid();

            Unirest.put(Ports.GAMESADDRESS +"/"+id +"/players/Nick")
                    .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS + "/" + id)
                    .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS + "/" + id)
                    .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS + "/" + id)
                    .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS + "/" + id+ "/players/Nick")
                    .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS + "/" + id)
                    .asJson();

            Unirest.put(Ports.GAMESADDRESS +"/"+id +"/players/Seb")
                    .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS + "/" + id)
                    .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS + "/" + id)
                    .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS + "/" + id)
                    .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS + "/" + id +"/players/Seb")
                    .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS + "/" + id)
                    .asJson();

            Unirest.put(Ports.GAMESADDRESS +"/"+id +"/players/Joe")
                    .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS + "/" + id)
                    .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS + "/" + id )
                    .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS + "/" + id )
                    .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS + "/" + id +"/players/Joe")
                    .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS + "/" + id)
                    .asJson();

            System.out.println("Game: " + tgame.getGameid());

        }

        System.out.println("gameList: " + gameList);

//        Unirest.put(Ports.GAMESADDRESS+"/"+gameId +"/players/olaf");
//        Unirest.put(Ports.GAMESADDRESS+"/"+gameId +"/players/nick");
//        Unirest.put(Ports.GAMESADDRESS+"/"+gameId +"/players/seb");
//
//        HttpResponse response = Unirest.get(restopoly.util.Ports.GAMESADDRESS + "/" + gameId)
//                .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS + "/" + gameId)
//                .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS + "/" + gameId)
//                .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS + "/" + gameId)
//                .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS + "/" + gameId)
//                .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS + "/" + gameId)
//                .asJson();
//        Gson gson = new Gson();
//        Game game = gson.fromJson(response.getBody().toString(), Game.class);
//
//        for (Player tPlayer: game.getPlayers()){
//
//
//
//        }



        return "";
    }


    public static void main(String[] args) {
        Aufgabe4 a4 = new Aufgabe4();
        try {
            a4.aufgabe_4_2();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


}
