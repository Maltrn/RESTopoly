package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import restopoly.resources.Games;
import restopoly.resources.Roll;

import static spark.Spark.get;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class DiceService {

    public static void main(String[] args) {

        get("/dice", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(new Roll());
        });
    }
}
