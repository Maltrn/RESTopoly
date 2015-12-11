package restopoly.services;

import static spark.Spark.*;

/**
 * Created by final-work on 09.12.15.
 */
public class BrokerService {

    public static void main(String[] args) {

//      TODO - Gets a broker
        get("/broker/:gameid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - places a broker
        put("/broker/:gameid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - List of available place
        get("/broker/:gameid/places", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - Gets a places
        get("/broker/:gameid/places/:placeid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - Registers the place with the broker, won't change anything if already registered
        put("/broker/:gameid/places/:placeid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//     TODO - Returns the owner of the place
        get("/broker/:gameid/places/:placeid/owner", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - Trade the place - changing the owner
        put("/broker/:gameid/places/:placeid/owner", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      Todo - Buy the place in question. It will fail if it is not for sale
        post("/broker/:gameid/places/:placeid/owner", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      Todo - takes a hypothecary credit onto the place
        put("/broker/:gameid/places/:placeid/hypothecarycredit", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - removes the hypothecary credit from the place
        delete("/broker/:gameid/places/:placeid/hypothecarycredit", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - indicates, that the player has visited this place, may be resulting in money transfe
        post("/broker/:gameid/places/:placeid/visit/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

    }
}
