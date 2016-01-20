package restopoly.client;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Game;
import restopoly.resources.Throw;
import restopoly.util.Ports;

/**
 * Created by mizus on 19.01.16.
 */
public class Aufgabe4 implements Ports {



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
                .header(Ports.BROOKER_KEY, Ports.BROKERSADDRESS)
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
                    .header(KEY_BOARDS_PLAYER, req_nick.getHeaders().getFirst(KEY_BOARDS_PLAYER))
                    .asString();

        //
        System.out.println("=====> " + jsonNodeHttpResponse.getHeaders());
        return jsonNodeHttpResponse;
    }

    public String aufgabe_4_3() throws UnirestException {
        HttpResponse req_a4_2 = aufgabe_4_2();

        System.out.println("Nick: " + req_a4_2.getHeaders().getFirst(Ports.KEY_BOARDS_PLAYER));

        System.out.println(req_a4_2.getHeaders().getFirst(Ports.KEY_PLAYER_TURN));
        HttpResponse uResponse = Unirest.put(req_a4_2.getHeaders().getFirst(Ports.KEY_PLAYER_TURN))
                .header(Ports.GAME_KEY, req_a4_2.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, req_a4_2.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, req_a4_2.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, req_a4_2.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, req_a4_2.getHeaders().getFirst(Ports.EVENT_KEY))
                .header(Ports.KEY_BOARDS_PLAYER, req_a4_2.getHeaders().getFirst(Ports.KEY_BOARDS_PLAYER))
                .header(Ports.KEY_GAME_PLAYER, req_a4_2.getHeaders().getFirst(Ports.KEY_GAME_PLAYER))
                .header(KEY_PLAYER_TURN, req_a4_2.getHeaders().getFirst(KEY_PLAYER_TURN))
                .asString();


        System.out.println("PLAYER ON BOARD:" + uResponse.getHeaders().getFirst(Ports.KEY_BOARDS_PLAYER));

        HttpResponse rResponse = Unirest.get(uResponse.getHeaders().getFirst(Ports.KEY_ONBOARD))
                .header(Ports.GAME_KEY, uResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, uResponse.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, uResponse.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, uResponse.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, uResponse.getHeaders().getFirst(Ports.EVENT_KEY))
                .header(KEY_BOARDS_PLAYER, uResponse.getHeaders().getFirst(KEY_BOARDS_PLAYER))
                .header(KEY_PLAYER_TURN, uResponse.getHeaders().getFirst(KEY_PLAYER_TURN))
                .asJson();

        System.out.println("Roll: " + rResponse.getHeaders().getFirst(Ports.KEY_PLAYER_ON_BOARD_ROLL));
        System.out.println("Turn: " + rResponse.getHeaders().getFirst(KEY_PLAYER_TURN));
        System.out.println("Event: " + rResponse.getHeaders().getFirst(EVENT_KEY));

        Throw wurf = new Throw();

        HttpResponse postRespone = Unirest.post(rResponse.getHeaders().getFirst(Ports.KEY_PLAYER_ON_BOARD_ROLL))
                .header(Ports.GAME_KEY, rResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, rResponse.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, rResponse.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, rResponse.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, rResponse.getHeaders().getFirst(Ports.EVENT_KEY))
                .header(KEY_BOARDS_PLAYER, rResponse.getHeaders().getFirst(KEY_BOARDS_PLAYER))
                .header(KEY_PLAYER_TURN, rResponse.getHeaders().getFirst(KEY_PLAYER_TURN))
                .body(new Gson().toJson(wurf))
                .asString();


//        TODO - der AUfruf von roll fehlt!

//        System.out.println("1" + rResponse.getHeaders().getFirst(Ports.GAME_KEY) );
//        System.out.println("2" + rResponse.getHeaders().getFirst(Ports.DICE_KEY) );
//        System.out.println("3" +rResponse.getHeaders().getFirst(Ports.BANK_KEY) );
//        System.out.println("4" + rResponse.getHeaders().getFirst(Ports.BOARD_KEY) );
//        System.out.println("5" + rResponse.getHeaders().getFirst(Ports.EVENT_KEY) );
//        System.out.println("6" + rResponse.getHeaders().getFirst(Ports.KEY_BOARD_PLAYER_PLACE) );

        System.out.println("Player_Place " + postRespone.getHeaders().getFirst(Ports.KEY_BOARD_PLAYER_PLACE));

//  TODO - GET Boards Place
        HttpResponse pResponse = Unirest.get(postRespone.getHeaders().getFirst(Ports.KEY_BOARD_PLAYER_PLACE))
                .header(Ports.GAME_KEY, postRespone.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, postRespone.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, postRespone.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, postRespone.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, postRespone.getHeaders().getFirst(Ports.EVENT_KEY))
                .header(Ports.BROOKER_KEY, postRespone.getHeaders().getFirst(Ports.BROOKER_KEY))
                .asJson();

        System.out.println("pResponse: " + pResponse.getBody().toString());
        System.out.println("KeyBrokerPlace "+ pResponse.getHeaders().getFirst(Ports.KEY_BROKER_PLACE));
//  TODO - GET Brokers Place
        HttpResponse bResponse = Unirest.get(pResponse.getHeaders().getFirst(Ports.KEY_BROKER_PLACE))
                .header(Ports.GAME_KEY, pResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, pResponse.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, pResponse.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, pResponse.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, pResponse.getHeaders().getFirst(Ports.EVENT_KEY))
                .header(Ports.BROOKER_KEY, postRespone.getHeaders().getFirst(Ports.BROOKER_KEY))
                .header(Ports.KEY_BROKER_PLACE, pResponse.getHeaders().getFirst(Ports.KEY_BROKER_PLACE))
                .asJson();

        System.out.println("bResponse: " + bResponse.getBody().toString());


        System.out.println("KEY_BROKER_PLACE_OWNER " + bResponse.getHeaders().getFirst(Ports.KEY_BROKER_PLACE_OWNER));

//        TODO - Post Owner
        HttpResponse oResponse = Unirest.get(bResponse.getHeaders().getFirst(Ports.KEY_BROKER_PLACE_OWNER))
                .header(Ports.GAME_KEY, bResponse.getHeaders().getFirst(Ports.GAME_KEY))
                .header(Ports.DICE_KEY, bResponse.getHeaders().getFirst(Ports.DICE_KEY))
                .header(Ports.BANK_KEY, bResponse.getHeaders().getFirst(Ports.BANK_KEY))
                .header(Ports.BOARD_KEY, bResponse.getHeaders().getFirst(Ports.BOARD_KEY))
                .header(Ports.EVENT_KEY, bResponse.getHeaders().getFirst(Ports.EVENT_KEY))
                .asJson();

        System.out.println("oResponse: " + oResponse.getBody().toString());

    return "";
    }

//    TODO - PUT Ready von Response getBoardsPlayers!!!


    public static void main(String[] args) {
        Aufgabe4 a4 = new Aufgabe4();
        try {
            a4.aufgabe_4_3();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


}
