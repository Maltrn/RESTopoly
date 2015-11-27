package restopoly.services;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.Service;

import static spark.Spark.delete;
import static spark.Spark.put;


/**
 * Created by Krystian.Graczyk on 25.11.15.
 */
public class MutexService {
    private static Gson gson = new Gson();

    public static void main(String[] args) {

        put("/mutex/bankMutex", (req, res) -> {
            return "";
        });

        delete("/mutex/bankMutex", (req, res) -> {
            return "";
        });

        put("/mutex/rollMutex", (req, res) -> {
            return "";
        });

        delete("/mutex/rollMutex", (req, res) -> {
            return "";
        });


        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "MUTEX")
                    .queryString("description", "Mutex Service")
                    .queryString("service", "mutex")
                    .queryString("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/18194/mutex")
                    .body(new Gson().toJson(new Service("MUTEX", "Mutex Service", "mutex", "https://vs-docker.informatik.haw-hamburg.de/ports/18194/mutex")))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }
}
