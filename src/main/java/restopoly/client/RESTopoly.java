package restopoly.client;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.*;
import spark.Spark;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static spark.Spark.post;

/**
 * Created by Krystian.Graczyk on 28.10.15.
 */
public class RESTopoly {

    private static String PLAYERADDRESS;
    private static String GAMESADDRESS;
    private static String BANKSADDRESS;
    private static String DICEADDRESS;
    private static String BOARDSADDRESS;
    private static String EVENTSADDRESS;
    private static String GAMEID;
    private static Player PLAYER;
    private static boolean TURN = true;
    private static int PORT = 4567;

    public RESTopoly() {
    }

    public String createGame() throws UnirestException {
        HttpResponse response = Unirest.post(GAMESADDRESS)
                .queryString("gameUri", GAMESADDRESS)
                .queryString("diceUri", DICEADDRESS)
                .queryString("bankUri", BANKSADDRESS)
                .queryString("boardUri", BOARDSADDRESS)
                .queryString("eventUri", EVENTSADDRESS)
                .asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(), Game.class);
        return game.getGameid();

    }

    public void joinGame(String gameid, String playerid, String name) throws UnirestException {
        GAMEID = gameid;
        Unirest.put(GAMESADDRESS + "/" + GAMEID + "/players/" + playerid).queryString("name", name).queryString("uri", PLAYERADDRESS).asString();
        Unirest.post(BANKSADDRESS + "/" + GAMEID + "/players").body(playerid).asString();
        HttpResponse playerResponse = Unirest.get(GAMESADDRESS + "/" + GAMEID + "/players/" + playerid).asJson();
        PLAYER = new Gson().fromJson(playerResponse.getBody().toString(), Player.class);
    }

    public void ready() throws UnirestException {
        Unirest.put(GAMESADDRESS + "/" + GAMEID + "/players/" + PLAYER.getId() + "/ready").asString();
    }

    public int diceRoll() throws UnirestException {

        if (TURN == true) return roll() + roll();
        return 0;
    }

    public int roll() throws UnirestException {
        HttpResponse response = Unirest.get(DICEADDRESS).asJson();
        Gson gson = new Gson();
        Roll roll = gson.fromJson(response.getBody().toString(), Roll.class);
        return roll.getNumber();
    }

    public String accountBalance(String account) throws UnirestException {
        HttpResponse response = Unirest.get(BANKSADDRESS + "/" + GAMEID + "/players/" + account).asString();
        return response.getBody().toString();
    }

    public void transferFrom(String from, int amount) throws UnirestException {
        Unirest.post(BANKSADDRESS + "/" + GAMEID + "/transfer/from/" + from + "/" + amount).asString();
    }

    public void transferTo(String to, int amount) throws UnirestException {
        Unirest.post(BANKSADDRESS + "/" + GAMEID + "/transfer/to/" + to + "/" + amount).asString();
    }

    public void transferFromTo(String from, String to, int amount) throws UnirestException {
        Unirest.post(BANKSADDRESS + "/" + GAMEID + "/transfer/from/" + from + "/to/" + to + "/" + amount).asString();
    }

    public void createEvent(String type, String name, String reason, String resource, Player player) throws UnirestException{
        Event event = new Event(type,name,resource,reason,player);
        Unirest.post(EVENTSADDRESS).queryString("gameid",GAMEID).body(new Gson().toJson(event)).asJson();
    }

    public void subscribe(String type, String name, String reason, String resource, Player player) throws UnirestException{
        Event event = new Event(type,name,resource,reason,player);
        Subscription subscription = new Subscription(GAMEID,PLAYERADDRESS,event);
        Unirest.post(EVENTSADDRESS+"/subscriptions").body(new Gson().toJson(subscription)).asString();
    }

    public static void main(String args[]) throws UnirestException, IOException {

        Spark.port(PORT);

        post("/player/turn", (req, res) -> {
            TURN = true;
            System.out.println(res.body());
            return "";
        });

        post("/player/event", (req, res) -> {
            Event event = new Gson().fromJson(req.body().toString(), Event.class);
            System.out.println(event);
            return "";
        });

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine(); //you get the IP as a String
        PLAYERADDRESS ="http://"+ip+":"+PORT+"/player";
        GAMESADDRESS = "https://vs-docker.informatik.haw-hamburg.de/ports/18191/games";
        DICEADDRESS = "https://vs-docker.informatik.haw-hamburg.de/ports/18190/dice";
        BANKSADDRESS = "https://vs-docker.informatik.haw-hamburg.de/ports/18192/banks";
        BOARDSADDRESS = "https://vs-docker.informatik.haw-hamburg.de/ports/18193/boards";
        EVENTSADDRESS = "https://vs-docker.informatik.haw-hamburg.de/ports/18194/events";


        RESTopoly client = new RESTopoly();

        JFrame frame = new JFrame("RESTopoly");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 150);
        frame.setLocation(430, 100);

        JPanel panel = new JPanel();
        JPanel panelLeft = new JPanel();
        JPanel panelRight = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));

        frame.add(panel);
        panel.add(panelLeft);
        panel.add(panelRight);

        JLabel lblLeft = new JLabel("Menu");
        lblLeft.setVisible(true);
        JLabel lblRight = new JLabel("Admin Menu");
        lblRight.setVisible(true);
        panelLeft.add(lblLeft);
        panelRight.add(lblRight);


        String[] choicesLeft = {"Create Game", "Join Game", "Ready", "Roll", "Account Balance","Subscribe"};
        String[] choicesRight = {"Transfer from", "Transfer to", "Transfer from - to", "Create Event"};


        final JComboBox<String> cbl = new JComboBox<String>(choicesLeft);
        final JComboBox<String> cbr = new JComboBox<String>(choicesRight);


        panelLeft.add(cbl);
        cbl.setVisible(true);
        panelRight.add(cbr);
        cbr.setVisible(true);

        JButton btnl = new JButton("OK");
        JButton btnr = new JButton("OK");


        btnl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnl) {
                    if (cbl.getSelectedItem().toString().equals("Create Game")) {
                        try {
                            GAMEID = client.createGame();
                            JTextArea textarea = new JTextArea(GAMEID);
                            textarea.setEditable(false);
                            JOptionPane.showMessageDialog(frame,
                                    textarea);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Join Game")) {
                        String gameid = JOptionPane.showInputDialog(frame,
                                "What is the Game ID?", null);
                        String playerid = JOptionPane.showInputDialog(frame,
                                "What is your Player ID?", null);
                        String name = JOptionPane.showInputDialog(frame,
                                "What is your Name?", null);
                        try {
                            client.joinGame(gameid, playerid, name);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Ready")) {
                        try {
                            client.ready();
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Roll")) {
                        try {
                            JOptionPane.showMessageDialog(frame,
                                    client.diceRoll());
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Account Balance")) {
                        try {
                            String account = JOptionPane.showInputDialog(frame,
                                    "Which account balance do you want to see?", null);
                            JOptionPane.showMessageDialog(frame, client.accountBalance(account));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Subscribe")) {
                        try {
                            String name = JOptionPane.showInputDialog(frame,
                                    "Which name does the event have which you want to subscribe to?", null);
                            client.subscribe("", name, "", "", PLAYER);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        btnr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnr) {
                    if (cbr.getSelectedItem().toString().equals("Transfer from")) {
                        try {
                            String from = JOptionPane.showInputDialog(frame,
                                    "Which account do you want to take money from?", null);
                            String amount = JOptionPane.showInputDialog(frame,
                                    "How much money should be taken?", null);
                            client.transferFrom(from, Integer.parseInt(amount));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbr.getSelectedItem().toString().equals("Transfer to")) {
                        try {
                            String to = JOptionPane.showInputDialog(frame,
                                    "Which account do you want to give money to?", null);
                            String amount = JOptionPane.showInputDialog(frame,
                                    "How much money should be given?", null);
                            client.transferTo(to, Integer.parseInt(amount));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbr.getSelectedItem().toString().equals("Transfer from - to")) {
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
                    if (cbr.getSelectedItem().toString().equals("Create Event")) {
                        try {
                            String type = JOptionPane.showInputDialog(frame,
                                    "Event Type?", null);
                            String name = JOptionPane.showInputDialog(frame,
                                    "Event Name?", null);
                            String reason = JOptionPane.showInputDialog(frame,
                                    "Reason for Event?", null);
                            String resource = JOptionPane.showInputDialog(frame,
                                    "Related Resource Uri?", null);
                            client.createEvent(type, name, reason, resource, PLAYER);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }
        });
        panelLeft.add(btnl);
        panelRight.add(btnr);
        btnl.setVisible(true);
        btnr.setVisible(true);
        frame.setVisible(true);

    }
}
