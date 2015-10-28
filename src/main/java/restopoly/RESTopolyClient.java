package restopoly;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by Krystian.Graczyk on 28.10.15.
 */
public class RESTopolyClient {

    private static String IP;
    private static String PORT;
    private static String ADRESS;

    public RESTopolyClient(){}

    public String createGame() throws UnirestException {
        HttpResponse response = Unirest.post(ADRESS+"/games").asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(),Game.class);
        return game.getGameid();

    }

    public void joinGame(String gameid, String playerid, String name, String uri) throws UnirestException {
        Unirest.put(ADRESS+"/games/"+gameid+"/players/"+playerid).queryString("name", name).queryString("uri", uri).asString();
    }

    public void ready(String gameid, String playerid) throws UnirestException {
        Unirest.put(ADRESS+"/games/"+gameid+"/players/"+playerid+"/ready").asString();
    }

    public static void main(String args[]) throws UnirestException {
        IP = "0.0.0.0";
        PORT = "4567";
        ADRESS = "http://"+IP+":"+PORT;

        RESTopolyClient client = new RESTopolyClient();
        String gameid = client.createGame();
        client.joinGame(gameid,"1","Krystian","test");
        client.ready(gameid,"1");

    }
}
