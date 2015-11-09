package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import restopoly.resources.Games;
import restopoly.resources.Roll;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class DiceService {

    public static void main(String[] args) {
        port(4568);
        get("/dice", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(new Roll());
        });
    }
}
