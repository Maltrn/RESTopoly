package restopoly.services;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import restopoly.resources.Broker;

import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by final-work on 09.12.15.
 */
public class BrokerService {

    private static ArrayList<Broker> brokers = new ArrayList();

    public static void main(String[] args) {
        String BANKSADDRESS = "https://vs-docker.informatik.haw-hamburg.de/ports/18192/banks";

//#############################    Aufgabenstellung A3    #####################################

//      TODO - places a broker
        put("/brokers/:gameid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Broker newBroker = new Broker(req.params(":gameid"));
            brokers.add(newBroker);
            return new Gson().toJson(newBroker);
        });

//      TODO - Registers the place with the broker, won't change anything if already registered
        put("/brokers/:gameid/places/:placeid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            for (Broker broker : brokers) {
                if(broker.getGameid().equals(req.params(":gameid"))){
                    if(broker.containField(req.params(":placeId")))
                        broker.putField(req.params(":placeId"));
                }
            }
            return "";
        });

//      TODO - indicates, that the player has visited this place, may be resulting in money transfer
        post("/brokers/:gameid/places/:placeid/visit/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            String g_id = req.params(":gameid");
            String p_id = req.params(":placeid");
            String pl_id = req.params(":playerid");

            Broker tBroker= getBroker(g_id);
            Broker copyBroker = tBroker.createCopy();
            try{
                if (tBroker != null){
                    String ownerID = tBroker.getOwnerID(p_id);
                    if (ownerID != null){
                        String tPrice = tBroker.getPrice(p_id);
                        Unirest.post(BANKSADDRESS + "/" + g_id + "/transfer/from/" + pl_id + "/to/" + ownerID + "/" + tPrice).asJson();
                    }
                }
            }catch (Exception e){
                brokers.remove(getBrokerPos(tBroker));
                brokers.add(copyBroker);
            }

            return "";
        });

//      Todo - Buy the place in question. It will fail if it is not for sale
        post("/brokers/:gameid/places/:placeid/owner", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            String g_id = req.params(":gameid");
            String p_id = req.params(":placeid");
            Broker tBroker = getBroker(g_id);
            Broker copyForm =  tBroker.createCopy();
            try{
                if (tBroker != null){
                    boolean free = tBroker.isAvalible(p_id);
                    if (free){
                        //ToDO wie wie kommt man an den PlayerId welcher das Feld Kaufen soll
                        String url = "/banks/:gameid/transfer/from/:from/:amount";
                        String tPrice = tBroker.getPrice(p_id);
                        String playerID= "";
                        HttpResponse response = Unirest.post(BANKSADDRESS + "/" + g_id + "/transfer/from/" + playerID + "/" + tPrice).asJson();
                        if(response.getStatus() == 201){
                            tBroker.buyField(p_id,playerID);
                        }
                    }
                }
            }catch (Exception e){
                brokers.remove(getBrokerPos(tBroker));
                brokers.add(copyForm);
            }
            return "";
        });



//##################################  Weitere  #####################################
//      TODO - Gets a broker
        get("/brokers/:gameid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - List of available place
        get("/brokers/:gameid/places", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return "";
        });

//      TODO - Gets a places
        get("/brokers/:gameid/places/:placeid", (req, res) -> {
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


}
