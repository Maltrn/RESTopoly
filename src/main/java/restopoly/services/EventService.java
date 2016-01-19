package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Event;
import restopoly.resources.Subscription;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Service;

import java.util.ArrayList;

import static restopoly.util.Ports.EVENTSADDRESS;
import static restopoly.util.Ports.PLAYERSADDRESS;
import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class EventService {

    static int eventCounter = 0;

    public static void main(String[] args) {
//TODO - später auskommentieren
        port(4570);

        ArrayList<Event> events = new ArrayList<Event>();
        ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();


        get("/events", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(events);
        });

        get("/event/:gameid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            for (Event event : events) {
                if(event.getGameid().equals(req.params(":gameid"))){
                    return gson.toJson(event);
                }
            }
            return "";
        });

        post("/events", (req, res) -> {
//            System.out.println("Query: " +req.queryParams("gameid").toString());
            res.status(201);
            res.header("Content-Type", "application/json");

            Gson gson = new GsonBuilder().create();
            Event event = gson.fromJson(req.body(), Event.class);
            event.setGameid(req.queryParams("gameid"));
            event.setUri("events/" + String.valueOf(eventCounter));
            eventCounter+=1;
            System.out.println("uri: " + event.getUri());
            events.add(event);
            for(Subscription subscription:subscriptions){
                if(subscription.getEvent().getName().equals(event.getName()) &&
                        subscription.getEvent().getGameid().equals(event.getGameid())){
                    Unirest.post(PLAYERSADDRESS+"/event").queryString("playerUri",subscription.getUri()).body(gson.toJson(event)).asString();
                }
            }
            gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Event.name"))
//                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Event.reason"))
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
                    .queryString("uri", EVENTSADDRESS)
                    .body(new Gson().toJson(new Service("EVENT", "Event Service", "events", EVENTSADDRESS)))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
