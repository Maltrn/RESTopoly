package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Roll;
import restopoly.util.Service;

import static restopoly.util.Ports.DICEADDRESS;
import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class DiceService{

    public static void main(String[] args) {

        port(4572);

        get("/dice", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(new Roll());
        });

                try {
                   Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                           .header("accept", "application/json")
                           .header("Content-Type", "application/json")
                           .queryString("name", "DICE")
                           .queryString("description", "Gives you a dice roll")
                           .queryString("service", "dice")
                           .queryString("uri", DICEADDRESS)
                           .body(new Gson().toJson(new Service("DICE", "Gives you a dice roll", "dice", DICEADDRESS)))
                           .asJson();
                } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
