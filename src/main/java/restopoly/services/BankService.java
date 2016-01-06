package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.util.CustomExclusionStrategy;
import restopoly.util.Service;
import static restopoly.util.Ports.*;
import restopoly.resources.*;

import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class BankService {

    public static void main(String[] args) {

        BankService bankService = new BankService();

        ArrayList<Bank> banks = new ArrayList<>();

        get("/banks/:gameid", (req, res) -> {
            res.header("Content-Type", "application/json");
            res.status(200);
            Bank bank = null;
            for(Bank b : banks){
                if(b.getGameid().equals(req.params(":gameid"))) bank=b;
            }
            Gson gson = new GsonBuilder()
                    .create();
            return gson.toJson(bank);
        });

        put("/banks/:gameid", (req, res) -> {
            res.header("Content-Type", "application/json");
            Bank bank = new Bank(req.params(":gameid"));
            banks.add(bank);
            Gson gson = new GsonBuilder()
                    .create();
            return gson.toJson(bank);
        });

        post("/banks/:gameid/players", (req, res) -> {
            res.status(201);
            res.header("Content-Type", "application/json");
            Bank bank = null;
            for(Bank b : banks){
                if(b.getGameid().equals(req.params(":gameid"))) bank=b;
            }
            Gson gson = new Gson();
            if(bank.getAccount(req.body())!=null){
                res.status(409);
                return gson.toJson(bank.getAccount(req.body()));
            }

            HttpResponse playerResponse = Unirest.get(GAMESADDRESS+"/"+ req.params(":gameid")+"/players/"+req.body()).asJson();
            Player player = gson.fromJson(playerResponse.getBody().toString(),Player.class);

            Account account = new Account(req.body(),player,0);
            bank.addAccount(account);
            return gson.toJson(account);
        });

        get("/banks/:gameid/players", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Bank bank = null;
            for(Bank b : banks){
                if(b.getGameid().equals(req.params(":gameid"))) bank=b;
            }
            Gson gson = new GsonBuilder().create();
            return gson.toJson(bank.getAccounts());
        });

        get("/banks/:gameid/players/:playerid", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            Bank bank = null;
            for(Bank b : banks){
                if(b.getGameid().equals(req.params(":gameid"))) bank=b;
            }
            int saldo = bank.getAccount(req.params(":playerid")).getSaldo();
            Gson gson = new GsonBuilder().create();
            return gson.toJson(saldo);
        });


        post("/banks/:gameid/transfer/to/:to/:amount", (req, res) -> {
            synchronized(bankService) {
                res.status(201);
                res.header("Content-Type", "application/json");
                Bank bank = null;
                for (Bank b : banks) {
                    if (b.getGameid().equals(req.params(":gameid"))) bank = b;
                }
                Account to = bank.getAccount(req.params(":to"));
                int saldo = to.getSaldo();
                try {
                    to.setSaldo(saldo + Integer.parseInt(req.params(":amount")));

                    Gson gson = new GsonBuilder()
                            .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Account.player"))
                            .create();
                    return gson.toJson(to);
                } catch (Throwable e) {
                    to.setSaldo(saldo);
                    res.status(500);
                    return "";
                }
            }
        });

        post("/banks/:gameid/transfer/from/:from/:amount", (req, res) -> {
            synchronized(bankService) {
                res.status(201);
                res.header("Content-Type", "application/json");
                Bank bank = null;
                for (Bank b : banks) {
                    if (b.getGameid().equals(req.params(":gameid"))) bank = b;
                }
                Account from = bank.getAccount(req.params(":from"));
                if (from.getSaldo() < Integer.parseInt(req.params(":amount"))) {
                    res.status(403);
                    return "";
                }
                int saldo = from.getSaldo();
                try {
                    from.setSaldo(saldo - Integer.parseInt(req.params(":amount")));

                    Gson gson = new GsonBuilder()
                            .setExclusionStrategies(new CustomExclusionStrategy("restopoly.resources.Account.player"))
                            .create();
                    return gson.toJson(from);
                } catch (Throwable e) {
                    from.setSaldo(saldo);
                    res.status(500);
                    return "";
                }
            }
        });


        post("/banks/:gameid/transfer/from/:from/to/:to/:amount", (req, res) -> {
            synchronized(bankService) {
                res.status(201);
                res.header("Content-Type", "application/json");
                Bank bank = null;
                for (Bank b : banks) {
                    if (b.getGameid().equals(req.params(":gameid"))) bank = b;
                }
                Account from = bank.getAccount(req.params(":from"));
                int fromsaldo = from.getSaldo();

                Account to = bank.getAccount(req.params(":to"));
                int tosaldo = to.getSaldo();

                if (fromsaldo < Integer.parseInt(req.params(":amount"))) {
                    res.status(403);
                    return "";
                }
                try {
                    from.setSaldo(fromsaldo - Integer.parseInt(req.params(":amount")));
                    to.setSaldo(tosaldo + Integer.parseInt(req.params(":amount")));
                    return "";
                } catch (Throwable e) {
                    from.setSaldo(fromsaldo);
                    to.setSaldo(tosaldo);
                    res.status(500);
                    return "";
                }
            }
        });


        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "BANKS")
                    .queryString("description", "Banks Service")
                    .queryString("service", "banks")
                    .queryString("uri", BANKSADDRESS)
                    .body(new Gson().toJson(new Service("BANKS", "Banks Service", "banks", BANKSADDRESS)))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();

        }

    }
}

