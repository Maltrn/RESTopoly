package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.CustomExclusionStrategy;
import restopoly.Service;
import restopoly.resources.Event;
import restopoly.resources.Events;
import restopoly.resources.Subscription;
import restopoly.resources.Subscriptions;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class EventService {

    public static void main(String[] args) {

        Events events = new Events();
        Subscriptions subscriptions = new Subscriptions();

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
            events.addEvent(event);
            for(Subscription subscription:subscriptions.getSubscriptions()){
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
            for (Event event: events.getEvents()){
                if(event.getGameid().equals(req.queryParams("gameid"))){
                    events.deleteEvent(event);
                }
            }
            return "";
        });

        post("/events/subscriptions", (req, res) -> {
            res.status(201);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            Subscription subscription = gson.fromJson(req.body(), Subscription.class);
            subscriptions.addSubscription(subscription);
            return "";
        });

        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "EVENT")
                    .queryString("description", "Event Service")
                    .queryString("service", "events")
                    .queryString("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/18195/events")
                    .body(new Gson().toJson(new Service("EVENT", "Event Service", "events", "https://vs-docker.informatik.haw-hamburg.de/ports/18195/events")))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
