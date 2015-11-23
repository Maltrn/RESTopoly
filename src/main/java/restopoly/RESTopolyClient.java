package restopoly;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Game;
import restopoly.resources.Roll;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Krystian.Graczyk on 28.10.15.
 */
public class RESTopolyClient {

    private static String GAMESADRESS;
    private static String BANKSADRESS;
    private static String DICESADRESS;
    private static String GAMEID;

    public RESTopolyClient(){}

    public String createGame() throws UnirestException {
        HttpResponse response = Unirest.post(GAMESADRESS+"/games").queryString("gameUri",GAMESADRESS+"/games").queryString("diceUri",DICESADRESS+"/dice").queryString("bankUri",BANKSADRESS+"/banks").asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(),Game.class);
        Unirest.put(BANKSADRESS+"/banks/"+game.getGameid()).asString();
        return game.getGameid();

    }

    public void joinGame(String gameid, String playerid, String name) throws UnirestException {
        Unirest.put(GAMESADRESS+"/games/"+gameid+"/players/"+playerid).queryString("name", name).queryString("uri", GAMESADRESS+"/games/"+gameid+"/players/"+name).asString();
        Unirest.post(BANKSADRESS + "/banks/" + gameid + "/players").body(playerid).asString();
    }

    public void ready(String gameid, String playerid) throws UnirestException {
        Unirest.put(GAMESADRESS+"/games/"+gameid+"/players/"+playerid+"/ready").asString();
    }

    public int roll() throws UnirestException {
        HttpResponse response = Unirest.get(DICESADRESS + "/dice").asJson();
        Gson gson = new Gson();
        Roll roll = gson.fromJson(response.getBody().toString(),Roll.class);
        return roll.getNumber();
    }

    public String accountBalance(String account) throws UnirestException {
        HttpResponse response = Unirest.get(BANKSADRESS + "/banks/" + GAMEID + "/players/" + account).asString();
        return response.getBody().toString();
    }

    public void transferFrom(String from, int amount) throws UnirestException {
        Unirest.post(BANKSADRESS + "/banks/" + GAMEID + "/transfer/from/"+from+"/"+amount).asString();
    }

    public void transferTo(String to, int amount) throws UnirestException {
        Unirest.post(BANKSADRESS + "/banks/" + GAMEID + "/transfer/to/"+to+"/"+amount).asString();
    }

    public void transferFromTo(String from, String to, int amount) throws UnirestException {
        Unirest.post(BANKSADRESS + "/banks/" + GAMEID + "/transfer/from/"+from+"/to/"+to+"/"+amount).asString();
    }

    public static void main(String args[]) throws UnirestException {
        GAMESADRESS = "http://0.0.0.0:4567";
        DICESADRESS = "http://0.0.0.0:4568";
        BANKSADRESS = "http://0.0.0.0:4569";
        RESTopolyClient client = new RESTopolyClient();
      //  String gameid = client.createGame();
      //  client.joinGame(gameid,"1","Krystian","test");
      //  client.ready(gameid,"1");

        JFrame frame = new JFrame("RESTopoly");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 150);
        frame.setLocation(430, 100);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        JLabel lbl = new JLabel("Menu");
        lbl.setVisible(true);

        panel.add(lbl);

        String[] choices = { "Create Game","Join Game", "Ready", "Roll", "Account Balance", "Transfer from","Transfer to", "Transfer from - to"};

        final JComboBox<String> cb = new JComboBox<String>(choices);

        panel.add(cb);
        cb.setVisible(true);

        JButton btn = new JButton("OK");

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == btn){
                    if(cb.getSelectedItem().toString().equals("Create Game")){
                        try {
                            GAMEID = client.createGame();
                            JTextArea textarea= new JTextArea(GAMEID);
                            textarea.setEditable(false);
                            JOptionPane.showMessageDialog(frame,
                                    textarea);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(cb.getSelectedItem().toString().equals("Join Game")){
                        String gameid = JOptionPane.showInputDialog(frame,
                                "What is the Game ID?", null);
                        String playerid = JOptionPane.showInputDialog(frame,
                                "What is your Player ID?", null);
                        String name = JOptionPane.showInputDialog(frame,
                                "What is your Name?", null);
                        try {
                            client.joinGame(gameid,playerid,name);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(cb.getSelectedItem().toString().equals("Ready")){
                        String gameid = JOptionPane.showInputDialog(frame,
                                "What is your Game ID?", null);
                        String playerid = JOptionPane.showInputDialog(frame,
                                "What is the Player ID?", null);
                        try {
                            client.ready(gameid,playerid);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(cb.getSelectedItem().toString().equals("Roll")){
                        try {
                            JOptionPane.showMessageDialog(frame,
                                    client.roll());
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(cb.getSelectedItem().toString().equals("Account Balance")){
                        try {
                            String account = JOptionPane.showInputDialog(frame,
                                    "Which account balance to you want to see?", null);
                            JOptionPane.showMessageDialog(frame, client.accountBalance(account));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(cb.getSelectedItem().toString().equals("Transfer from")){
                        try {
                            String from = JOptionPane.showInputDialog(frame,
                                    "Which account do you want to take money from?", null);
                            String amount = JOptionPane.showInputDialog(frame,
                                    "How much money should be taken?", null);
                            client.transferFrom(from,Integer.parseInt(amount));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(cb.getSelectedItem().toString().equals("Transfer to")){
                        try {
                            String to = JOptionPane.showInputDialog(frame,
                                    "Which account do you want to give money to?", null);
                            String amount = JOptionPane.showInputDialog(frame,
                                    "How much money should be given?", null);
                            client.transferTo(to,Integer.parseInt(amount));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(cb.getSelectedItem().toString().equals("Transfer from - to")){
                        try {
                            String from = JOptionPane.showInputDialog(frame,
                                    "Which account do you want to take money from?", null);
                            String to = JOptionPane.showInputDialog(frame,
                                    "Which account do you want to give money to?", null);
                            String amount = JOptionPane.showInputDialog(frame,
                                    "How much money should be taken?", null);
                            client.transferFromTo(from, to, Integer.parseInt(amount));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        panel.add(btn);
        btn.setVisible(true);
        frame.setVisible(true);

    }
}
