package restopoly.services;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpStatus;
import restopoly.resources.Broker;
import restopoly.resources.Estate;
import restopoly.resources.Event;
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
//                        TODO - Banktransfer vom Vister zum Owner
//                        TODO - return a list of Events
                        HttpResponse bankRes  = Unirest.get(Ports.BANKSADDRESS + "/" + g_id + "/transfer/from/" + pl_id + "/to/" + estate.getOwner() + "/" + estate.getRent()).asJson();
                        Event[] resultarray = gson.fromJson(bankRes.getBody().toString(), Event[].class);
                        return gson.toJson(resultarray);
                    }
                }
            }
            return "";
        });

//      Todo - Buy the place in question. It will fail if it is not for sale
//        post("/brokers/:gameid/places/:placeid/owner", (req, res) -> {
//            res.status(HttpStatus.SC_CONFLICT);
//            res.header("Content-Type", "application/json");
//            String g_id = req.params(":gameid");
//            String p_id = req.params(":placeid");
//            Broker tBroker = getBroker(g_id);
//            Broker copyForm =  tBroker.createCopy();
//            try{
//                if (tBroker != null){
//                    boolean free = tBroker.isAvalible(p_id);
//                    if (free){
//                        //ToDO - wie kommt man an den PlayerId welcher das Feld Kaufen soll
//                        String url = "/banks/:gameid/transfer/from/:from/:amount";
//                        String tPrice = tBroker.getPrice(p_id);
//                        String playerID= "";
//                        HttpResponse response = Unirest.post(BANKSADDRESS + "/" + g_id + "/transfer/from/" + playerID + "/" + tPrice).asJson();
//                        if(response.getStatus() == HttpStatus.SC_CREATED){
//                            tBroker.buyField(p_id,playerID);
//
//                            Gson gson = new Gson();
//                            JSONObject jsonObject = null;
//                            jsonObject = new JSONObject();
//                            HttpResponse eventRes  = Unirest.get(EVENTSADDRESS + "/event/"+req.params(":gameid")).asJson();
//                            Event event = gson.fromJson(eventRes.getBody().toString(), Event.class);
//                            res.status(HttpStatus.SC_OK);
//                            jsonObject.put("events", event);
////                            TODO - Welcher Player??
//                            jsonObject.put("player", playerID);
//                            return gson.toJson(jsonObject);
//                        }
//                    }
//                }
//            }catch (Exception e){
//                brokers.remove(getBrokerPos(tBroker));
//                brokers.add(copyForm);
//            }
//            return "";
//        });



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

            Gson gson = new Gson();
            Broker broker = getBroker(req.params(":gameid"));
            if (broker != null) {
                for(Estate estate : broker.getEstates()){
                    if (estate.getPlace().equals(req.params(":placeid"))){
                        res.status(200);
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
