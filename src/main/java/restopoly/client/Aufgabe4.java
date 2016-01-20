package restopoly.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Game;
import restopoly.resources.Player;
import restopoly.util.Ports;

import javax.sound.midi.Soundbank;

/**
 * Created by mizus on 19.01.16.
 */
public class Aufgabe4 {



//    public String createGame() throws UnirestException {
//        HttpResponse response = Unirest.post(restopoly.util.Ports.GAMESADDRESS)
//                .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS)
//                .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS)
//                .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS)
//                .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS)
//                .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS)
//                .asJson();
//        Gson gson = new Gson();
//        Game game = gson.fromJson(response.getBody().toString(), Game.class);
//        return game.getGameid();
//    }

    //Aufgabe 4

    public String aufgabe_4_1() throws UnirestException {
        HttpResponse tResponse = Unirest.post(restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.GAME_KEY, restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS)
                .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS)
                .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS)
                .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS)
                .asJson();

//        System.out.println("==> " + tResponse.getHeaders().getFirst(Ports.GAME_KEY));

        HttpResponse response = Unirest.get(tResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.GAMESADDRESS,tResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, tResponse.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, tResponse.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, tResponse.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, tResponse.getHeaders().getFirst(Ports.EVENT_KEY))
                .asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(), Game.class);


        return response.getBody().toString();
    }


    public HttpResponse aufgabe_4_2() throws UnirestException {
        HttpResponse tResponse = Unirest.post(restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.GAME_KEY, restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS)
                .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS)
                .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS)
                .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS)
                .asJson();

        System.out.println("==> " + tResponse.getHeaders().getFirst(Ports.GAME_KEY));

        HttpResponse response = Unirest.get(tResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.GAME_KEY,tResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, tResponse.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, tResponse.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, tResponse.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, tResponse.getHeaders().getFirst(Ports.EVENT_KEY))
                .asJson();


        System.out.println("==> " + response.getHeaders().getFirst(Ports.KEY_GAME_PLAYER));

        HttpResponse req_nick =   Unirest.put(response.getHeaders().getFirst(Ports.KEY_GAME_PLAYER)+"Nick")
                    .header(Ports.GAME_KEY, response.getHeaders().getFirst(Ports.GAME_KEY))
                    .header(Ports.KEY_GAME_PLAYER, response.getHeaders().getFirst(Ports.KEY_GAME_PLAYER)+"Nick")
                    .header(Ports.KEY_BOARDS_PLAYER, response.getHeaders().getFirst(Ports.KEY_BOARDS_PLAYER)+"Nick")
                    .header(Ports.DICE_KEY, response.getHeaders().getFirst(Ports.DICE_KEY))
                    .header(Ports.BANK_KEY, response.getHeaders().getFirst(Ports.BANK_KEY))
                    .header(Ports.BOARD_KEY, response.getHeaders().getFirst(Ports.BOARD_KEY))
                    .header(Ports.EVENT_KEY, response.getHeaders().getFirst(Ports.EVENT_KEY))
                    .asJson();

        System.out.println(" req_nick: " + req_nick.getHeaders().getFirst(Ports.KEY_GAME_PLAYER));

        System.out.println("READY: " + req_nick.getHeaders().getFirst(Ports.KEY_PLAYER_GAME_READY));

        HttpResponse jsonNodeHttpResponse = Unirest.get(req_nick.getHeaders().getFirst(Ports.KEY_PLAYER_GAME_READY))
                    .header(Ports.GAME_KEY, req_nick.getHeaders().getFirst(Ports.GAME_KEY))
                    .header(Ports.DICE_KEY, req_nick.getHeaders().getFirst(Ports.DICE_KEY))
                    .header(Ports.BANK_KEY, req_nick.getHeaders().getFirst(Ports.BANK_KEY))
                    .header(Ports.BOARD_KEY, req_nick.getHeaders().getFirst(Ports.BOARD_KEY))
                    .header(Ports.EVENT_KEY, req_nick.getHeaders().getFirst(Ports.EVENT_KEY))
                    .header(Ports.KEY_PLAYER_GAME_READY, req_nick.getHeaders().getFirst(Ports.KEY_PLAYER_GAME_READY))
                    .header(Ports.KEY_GAME_PLAYER, req_nick.getHeaders().getFirst(Ports.KEY_GAME_PLAYER))
                    .asString();

        //
        System.out.println("=====> " + jsonNodeHttpResponse.getHeaders());
        return jsonNodeHttpResponse;
    }

    public String aufgabe_4_3() throws UnirestException {
        HttpResponse req_nick = aufgabe_4_2();

        System.out.println("Nick: " + req_nick.getHeaders());

        System.out.println(req_nick.getHeaders().getFirst(Ports.KEY_PLAYER_TURN));
        HttpResponse uResponse = Unirest.put(req_nick.getHeaders().getFirst(Ports.KEY_PLAYER_TURN))
                .header(Ports.GAME_KEY, req_nick.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, req_nick.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, req_nick.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, req_nick.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, req_nick.getHeaders().getFirst(Ports.EVENT_KEY))
                .asString();




        System.out.println("PLAYER ON BOARD:" + uResponse.getHeaders());

        HttpResponse rResponse = Unirest.get(uResponse.getHeaders().getFirst(Ports.KEY_ONBOARD))
                .header(Ports.GAME_KEY, uResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, uResponse.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, uResponse.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, uResponse.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, uResponse.getHeaders().getFirst(Ports.EVENT_KEY))
                .asJson();
    System.out.println(rResponse.getBody().toString());
    return "";
    }



    public static void main(String[] args) {
        Aufgabe4 a4 = new Aufgabe4();
        try {
            a4.aufgabe_4_3();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


}
