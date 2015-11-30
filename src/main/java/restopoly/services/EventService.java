package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Service;
import restopoly.resources.Event;
import restopoly.resources.Subscription;

import java.util.ArrayList;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class EventService {

    public static void main(String[] args) {

        ArrayList<Event> events = new ArrayList<Event>();
        ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();

        get("/events", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(events);
        });

        post("/events", (req, res) -> {
            res.status(201);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            Event event = gson.fromJson(req.body(), Event.class);
            event.setGameid(req.queryParams("gameid"));
            events.add(event);
            for(Subscription subscription:subscriptions){
                if(subscription.getEvent().getName().equals(event.getName()) &&
                        subscription.getEvent().getGameid().equals(event.getGameid())){
                Unirest.post(subscription.getUri()).body(gson.toJson(event)).asString();
                }
            }
            gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Event.name"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Event.reason"))
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Event.player"))
                    .create();
            return gson.toJson(event);
        });

        delete("/events", (req, res) -> {
            for (Event event: events){
                if(event.getGameid().equals(req.queryParams("gameid"))){
                    events.remove(event);
                }
            }
            return "";
        });

        get("/events/subscriptions", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(subscriptions);
        });

        post("/events/subscriptions", (req, res) -> {
            res.status(201);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
                    Subscription subscription = gson.fromJson(req.body(), Subscription.class);
                    subscriptions.add(subscription);
                    return "";
                });


        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "EVENT")
                    .queryString("description", "Event Service")
                    .queryString("service", "events")
                    .queryString("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/18194/events")
                    .body(new Gson().toJson(new Service("EVENT", "Event Service", "events", "https://vs-docker.informatik.haw-hamburg.de/ports/18194/events")))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
