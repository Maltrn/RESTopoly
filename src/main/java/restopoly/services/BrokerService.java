package restopoly.services;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpStatus;
import restopoly.resources.Broker;
import restopoly.resources.Estate;
import restopoly.resources.Event;
import restopoly.resources.Player;
import restopoly.util.Ports;

import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by final-work on 09.12.15.
 */
public class BrokerService {

    private static ArrayList<Broker> brokers = new ArrayList();

    public static void main(String[] args) {

        port(4571);

//#############################    Aufgabenstellung A3    #####################################

//      places a broker
        put("/brokers/:gameid", (req, res) -> {
//            res.status(HttpStatus.SC_OK);
            res.status(201);
            res.header("Content-Type", "application/json");

            Estate reqEstate = new Gson().fromJson(req.body().toString(), Estate.class);

            if (!alreadyExist(req.params(":gameid"))){
                if(reqEstate != null) {
                    Broker reqBroker = new Broker(req.params(":gameid"), reqEstate);
                    brokers.add(reqBroker);
                }
                Broker newBroker = new Broker(req.params(":gameid"));
                brokers.add(newBroker);
                return "";
            }
            if (alreadyExist(req.params(":gameid"))) {
                res.status(200);
            }
            return "";
        });

//      Registers the place with the broker, won't change anything if already registered
        put("/brokers/:gameid/places/:placeid", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");

            Estate reqEstate = new Gson().fromJson(req.body().toString(), Estate.class);

            Broker broker = getBroker(req.params(":gameid"));
            Gson gson = new Gson();
            if (broker != null) {
                for (Estate estate : broker.getEstates()) {
//                    Todo - Stringvergleich hier hardgecodet
                    if (estate.getPlace().equals(Ports.BOARDSADDRESS+"/fields/places/" + req.params(":placeid"))){
                        res.status(200);
                        return gson.toJson(estate.getPlace());
                    }
                }
                res.status(201);
                broker.getEstates().add(reqEstate);
                return gson.toJson(reqEstate.getPlace());
            }
            return "";
        });

//      indicates, that the player has visited this place, may be resulting in money transfer
        post("/brokers/:gameid/places/:placeid/visit/:playerid", (req, res) -> {
            res.status(HttpStatus.SC_NOT_FOUND);
            res.header("Content-Type", "application/json");
            String g_id = req.params(":gameid");
            String p_id = req.params(":placeid");
            String pl_id = req.params(":playerid");

            Broker broker = getBroker(g_id);
            Gson gson = new Gson();
            if (broker != null) {
                for (Estate estate : broker.getEstates()) {
                    if (estate.getPlace().equals(Ports.BOARDSADDRESS+"/fields/places/" + p_id)){
                        estate.setVisit(Ports.BOARDSADDRESS + "/" +g_id + "/players/" + pl_id);
                        if (!estate.getOwner().isEmpty()) {
                            HttpResponse playerRes  = Unirest.get(estate.getOwner()).asJson();
                            Player tempPlayer = gson.fromJson(playerRes.getBody().toString(), Player.class);

                            HttpResponse bankRes  = Unirest.post(Ports.BANKSADDRESS + "/" + g_id + "/transfer/from/" + pl_id + "/to/" + tempPlayer.getId() + "/" + estate.getRent()).asJson();
                            Event[] resultarray = gson.fromJson(bankRes.getBody().toString(), Event[].class);
                            return gson.toJson(resultarray);
                        }
                    }
                }
            }
//            Todo - Rückgabe muss angepasst werden
            Event[] resArray = new Event[0];
            return gson.toJson(resArray);
        });

//      Todo - Buy the place in question. It will fail if it is not for sale
        post("/brokers/:gameid/places/:placeid/owner", (req, res) -> {
            res.status(HttpStatus.SC_CONFLICT);
            res.header("Content-Type", "application/json");
            String g_id = req.params(":gameid");
            String p_id = req.params(":placeid");

            Broker broker = getBroker(g_id);
            Gson gson = new Gson();

            Player reqPlayer = gson.fromJson(req.body().toString(), Player.class);

            if (broker != null && reqPlayer!=null) {
                for (Estate estate : broker.getEstates()) {
                    if (estate.getPlace().equals(Ports.BOARDSADDRESS+"/fields/places/" + p_id)){
                        if (estate.getOwner().isEmpty()){
                            res.status(HttpStatus.SC_OK);
//                            Todo - Transaktion einleiten
                            HttpResponse bankRes  = Unirest.post(Ports.BANKSADDRESS + "/" + g_id + "/transfer/from/" + reqPlayer.getId() + "/" + estate.getValue()).asJson();
                            estate.setOwner(Ports.BOARDSADDRESS + "/" +g_id + "/players/" + reqPlayer.getId());
                            Event[] resultarray = gson.fromJson(bankRes.getBody().toString(), Event[].class);
//                            Todo - Events zurückgeben
                            return gson.toJson(resultarray);
                        }
                        res.status(409);
                        return "";
                    }
                }
            }
            return "";
        });



//##################################  Weitere  #####################################
//      TODO - Gets a broker
        get("/brokers/:gameid", (req, res) -> {
            res.status(HttpStatus.SC_NOT_FOUND);
            res.header("Content-Type", "application/json");
            for (Broker broker : brokers) {
                if (broker.getGameid().equals(req.params(":gameid"))) {
                    res.status(HttpStatus.SC_OK);
                    Gson gson = new Gson();
                    return gson.toJson(broker.getEstates());
                }
            }
            return "";
        });

//      TODO - Gets a places
        get("/brokers/:gameid/places/:placeid", (req, res) -> {
            res.status(404);
            res.header("Content-Type", "application/json");

            res.header(Ports.GAME_KEY, req.headers(Ports.GAME_KEY));
            res.header(Ports.DICE_KEY, req.headers(Ports.DICE_KEY));
            res.header(Ports.BANK_KEY, req.headers(Ports.BANK_KEY));
            res.header(Ports.BOARD_KEY, req.headers(Ports.BOARD_KEY));
            res.header(Ports.EVENT_KEY, req.headers(Ports.EVENT_KEY));
            res.header(Ports.BROOKER_KEY, req.headers(Ports.BROOKER_KEY));

            Gson gson = new Gson();
            Broker broker = getBroker(req.params(":gameid"));
            if (broker != null) {
                for(Estate estate : broker.getEstates()){
                    if (estate.getPlace().equals(Ports.BOARDSADDRESS + "/fields/places/" + req.params(":placeid"))){
                        res.status(200);
                        res.header(Ports.KEY_BROKER_PLACE_OWNER, req.headers(Ports.KEY_BROKER_PLACE)+ "/owner");
                        return gson.toJson(estate);
                    }
                }
            }
            return "";
        });

//      TODO - List of available place
        get("/brokers/:gameid/places", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//     TODO - Returns the owner of the place
        get("/brokers/:gameid/places/:placeid/owner", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - Trade the place - changing the owner
        put("/brokers/:gameid/places/:placeid/owner", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      Todo - takes a hypothecary credit onto the place
        put("/brokers/:gameid/places/:placeid/hypothecarycredit", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - removes the hypothecary credit from the place
        delete("/brokers/:gameid/places/:placeid/hypothecarycredit", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

    }


    private static Broker getBroker(String gameID){
        for (Broker b: brokers){
            if (b.getGameid().equals(gameID)) return b;
        }
        return null;
    }

    private static int getBrokerPos(Broker broker){
        for (int i = 0; i < brokers.size(); i++){
            if (brokers.get(i).getGameid().equals(broker.getGameid()))
                return i;

        }
        return -1;
    }

    private static boolean alreadyExist(String gameId){
        for (Broker broker : brokers){
            if (broker.getGameid().equals(gameId)){
                return true;
            }
        }
        return false;
    }

}
