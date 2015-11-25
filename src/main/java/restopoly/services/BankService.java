package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.CustomExclusionStrategy;
import restopoly.Service;
import restopoly.resources.*;

import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class BankService {

    public static void main(String[] args) {

        Banks banks = new Banks();;
        get("/banks/:gameid", (req, res) -> {
            res.header("Content-Type", "application/json");
            res.status(200);
            Bank bank = banks.getBank(req.params(":gameid"));
            Gson gson = new GsonBuilder()
                    .create();
            return gson.toJson(bank);
        });

        put("/banks/:gameid", (req, res) -> {
            res.header("Content-Type", "application/json");
            Bank bank = new Bank(req.params(":gameid"));
            banks.addBank(bank);
            Gson gson = new GsonBuilder()
                    .create();
            return gson.toJson(bank);
        });

        post("/banks/:gameid/players", (req, res) -> {
            res.status(201);
            res.header("Content-Type", "application/json");
            if(banks.getBank(req.params(":gameid")).getAccount(req.body())!=null){
                res.status(409);
                return banks.getBank(req.params(":gameid")).getAccount(req.body());
            }
            //Später Service über den Verzeichnisdienst holen sobald er funktioniert?
            HttpResponse playerResponse = Unirest.get("http://vs-docker.informatik.haw-hamburg.de:18191/games/" + req.params(":gameid")+"/players/"+req.body()).asJson();
            Gson gson = new Gson();
            Player player = gson.fromJson(playerResponse.getBody().toString(),Player.class);

            Account account = new Account(req.body(),player,0);
            Bank bank =banks.getBank(req.params(":gameid"));
            bank.addAccount(account);
            Gson gsonb = new GsonBuilder().create();
            return gsonb.toJson(account);
        });

        get("/banks/:gameid/players", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Gson gson = new GsonBuilder().create();
            return gson.toJson(banks.getBank(req.params(":gameid")).getAccounts());
        });

        get("/banks/:gameid/players/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            int saldo = banks.getBank(req.params(":gameid")).getAccount(req.params(":playerid")).getSaldo();
            Gson gson = new GsonBuilder().create();
            return gson.toJson(saldo);
        });


        post("/banks/:gameid/transfer/to/:to/:amount", (req, res) -> {
            Unirest.get("http://vs-docker.informatik.haw-hamburg.de:18193/mutex/bankMutex").asString();
            res.status(201);
            res.header("Content-Type", "application/json");
            Account to = banks.getBank(req.params(":gameid")).getAccount(req.params(":to"));
            int saldo = to.getSaldo();
            to.setSaldo(saldo+Integer.parseInt(req.params(":amount")));
            Unirest.put("http://vs-docker.informatik.haw-hamburg.de:18193/mutex/bankMutex").asString();

            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Account.player"))
                    .create();
            return gson.toJson(to);
        });

        post("/banks/:gameid/transfer/from/:from/:amount", (req, res) -> {
            Unirest.get("http://vs-docker.informatik.haw-hamburg.de:18193/mutex/bankMutex").asString();
            res.status(201);
            res.header("Content-Type", "application/json");
            Account from = banks.getBank(req.params(":gameid")).getAccount(req.params(":from"));
            if(from.getSaldo()<Integer.parseInt(req.params(":amount"))){
                res.status(403);
                return "";
            }
            int saldo = from.getSaldo();
            from.setSaldo(saldo-Integer.parseInt(req.params(":amount")));
            Unirest.put("http://vs-docker.informatik.haw-hamburg.de:18193/mutex/bankMutex").asString();

            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Account.player"))
                    .create();
            return gson.toJson(from);
        });


        post("/banks/:gameid/transfer/from/:from/to/:to/:amount", (req, res) -> {
            Unirest.get("http://vs-docker.informatik.haw-hamburg.de:18193/mutex/bankMutex").asString();
            res.status(201);
            res.header("Content-Type", "application/json");

            Account from = banks.getBank(req.params(":gameid")).getAccount(req.params(":from"));
            int fromsaldo = from.getSaldo();

            Account to = banks.getBank(req.params(":gameid")).getAccount(req.params(":to"));
            int tosaldo = to.getSaldo();

            if(fromsaldo<Integer.parseInt(req.params(":amount"))){
                res.status(403);
                return "";
            }
            from.setSaldo(fromsaldo-Integer.parseInt(req.params(":amount")));
            to.setSaldo(tosaldo+Integer.parseInt(req.params(":amount")));
            Unirest.put("http://vs-docker.informatik.haw-hamburg.de:18193/mutex/bankMutex").asString();

            return "";
        });


        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "BANKS")
                    .queryString("description", "Banks Service")
                    .queryString("service", "banks")
                    .queryString("uri", "https://vs-docker.informatik/haw-hamburg.de/ports/18192/banks")
                    .body(new Gson().toJson(new Service("BANKS", "Banks Service", "banks", "https://vs-docker.informatik.haw-hamburg.de/ports/18192/banks")))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();

        }

    }
}

