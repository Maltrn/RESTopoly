package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import static restopoly.util.Ports.*;
import restopoly.util.Service;
import restopoly.resources.Roll;
import com.mashape.unirest.http.exceptions.UnirestException;
import static spark.Spark.get;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class DiceService{

    public static void main(String[] args) {

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
