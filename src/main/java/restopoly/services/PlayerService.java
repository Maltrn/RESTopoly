package restopoly.services;


import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.util.PlayerWebSocket;
import restopoly.util.Service;
import restopoly.util.SocketList;
import static restopoly.util.Ports.*;
import spark.Spark;

import static spark.Spark.*;

public class PlayerService
{
    public static void main(String[] args)
    {

        Spark.webSocket("", PlayerWebSocket.class);
        Spark.webSocket("/player/turn",PlayerWebSocket.class);
        Spark.webSocket("/player/event",PlayerWebSocket.class);


        get("/player", (req, res) -> {
            SocketList.getInstance().writeSpecificMember(req.body(),"/player");
            return "";
        });

        post("/player/turn", (req, res) -> {
            SocketList.getInstance().writeSpecificMember(req.body(), "/player/turn");
            return "";
        });

        post("/player/event", (req, res) -> {
            SocketList.getInstance().writeSpecificMember(req.queryParams("playerUri"),"/player/event/"+req.body());
            return "";
        });





        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "PLAYER")
                    .queryString("description", "Player Service")
                    .queryString("service", "player")
                    .queryString("uri", PLAYERSADDRESS)
                    .body(new Gson().toJson(new Service("PLAYER", "Player Service", "player", PLAYERSADDRESS)))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();

        }


    }
}
