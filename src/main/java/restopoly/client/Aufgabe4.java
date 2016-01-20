package restopoly.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Game;
import restopoly.util.Ports;

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


    public String aufgabe_4_2() throws UnirestException {
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

        HttpResponse<JsonNode> req_nick =   Unirest.put(response.getHeaders().getFirst(Ports.KEY_GAME_PLAYER)+"Nick")
                    .header(Ports.GAME_KEY, response.getHeaders().getFirst(Ports.GAME_KEY) + "Nick")
                    .header(Ports.KEY_GAME_PLAYER, response.getHeaders().getFirst(Ports.KEY_GAME_PLAYER)+"Nick")
                    .header(Ports.DICE_KEY, response.getHeaders().getFirst(Ports.DICE_KEY))
                    .header(Ports.BANK_KEY, response.getHeaders().getFirst(Ports.BANK_KEY))
                    .header(Ports.BOARD_KEY, response.getHeaders().getFirst(Ports.KEY_BOARDS_PLAYER)+ "Nick")
                    .header(Ports.EVENT_KEY, response.getHeaders().getFirst(Ports.EVENT_KEY))
                    .asJson();

        System.out.println(" req_nick: " + req_nick.getHeaders().getFirst(Ports.GAME_KEY));

        System.out.printf("READY: " +  req_nick.getHeaders().getFirst(Ports.KEY_PLAYER_GAME_READY));

        Unirest.get(req_nick.getHeaders().getFirst(Ports.KEY_PLAYER_GAME_READY))
                    .header(Ports.GAME_KEY, req_nick.getHeaders().getFirst(Ports.GAME_KEY))
                    .header(Ports.DICE_KEY, req_nick.getHeaders().getFirst(Ports.DICE_KEY))
                    .header(Ports.BANK_KEY, req_nick.getHeaders().getFirst(Ports.BANK_KEY))
                    .header(Ports.BOARD_KEY, req_nick.getHeaders().getFirst(Ports.BOARD_KEY))
                    .header(Ports.EVENT_KEY, req_nick.getHeaders().getFirst(Ports.EVENT_KEY))
                    .asString();

        //

        response = Unirest.get(restopoly.util.Ports.GAMESADDRESS).asJson();
        Gson gson = new Gson();
        JsonArray gameList = gson.fromJson(response.getBody().toString(), JsonArray.class);

        System.out.println(" gameList: " + gameList);

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