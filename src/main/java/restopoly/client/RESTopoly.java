package restopoly.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import restopoly.resources.*;
import restopoly.util.PlayerWebSocket;
import restopoly.util.Ports;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Krystian.Graczyk on 28.10.15.
 */
public class RESTopoly {

    private static String GAMEID;
    private static Player PLAYER;
    private static String PLAYERADDRESS;
    private boolean turn = true;
    private List<Event> eventList = new ArrayList<>();

    private static final RESTopoly INSTANCE = new RESTopoly();

    public static RESTopoly getInstance() {
        return INSTANCE;
    }


    // METHODS

    public String createGame() throws UnirestException {
        HttpResponse response = Unirest.post(restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.GAMESADDRESS, restopoly.util.Ports.GAMESADDRESS)
                .header(Ports.DICE_KEY, restopoly.util.Ports.DICEADDRESS)
                .header(Ports.BANK_KEY, restopoly.util.Ports.BANKSADDRESS)
                .header(Ports.BOARD_KEY, restopoly.util.Ports.BOARDSADDRESS)
                .header(Ports.EVENT_KEY, restopoly.util.Ports.EVENTSADDRESS)
                .asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(), Game.class);
        return game.getGameid();
    }

    public void joinGame(String gameid, String playerid, String name) throws UnirestException {
        GAMEID = gameid;
        Unirest.put(restopoly.util.Ports.GAMESADDRESS + "/" + GAMEID + "/players/" + playerid).queryString("name", name).queryString("uri", PLAYERADDRESS).asString();
        Unirest.post(restopoly.util.Ports.BANKSADDRESS + "/" + GAMEID + "/players").body(playerid).asString();
        HttpResponse playerResponse = Unirest.get(restopoly.util.Ports.GAMESADDRESS + "/" + GAMEID + "/players/" + playerid).asJson();
        PLAYER = new Gson().fromJson(playerResponse.getBody().toString(), Player.class);
    }

    public void ready() throws UnirestException {
        Unirest.put(restopoly.util.Ports.GAMESADDRESS + "/" + GAMEID + "/players/" + PLAYER.getId() + "/ready").asString();
    }

    public int diceRoll() throws UnirestException {

        if (turn) {
            turn = false;
            return roll() + roll();
        }
        return 0;
    }

    public int roll() throws UnirestException {
        HttpResponse response = Unirest.get(restopoly.util.Ports.DICEADDRESS).asString();
        Gson gson = new Gson();
        Roll roll = gson.fromJson(response.getBody().toString(), Roll.class);
        return roll.getNumber();
    }

    public String accountBalance(String account) throws UnirestException {
        HttpResponse response = Unirest.get(restopoly.util.Ports.BANKSADDRESS + "/" + GAMEID + "/players/" + account).asString();
        return response.getBody().toString();
    }

    public void transferFrom(String from, int amount) throws UnirestException {
        Unirest.post(restopoly.util.Ports.BANKSADDRESS + "/" + GAMEID + "/transfer/from/" + from + "/" + amount).asString();
    }

    public void transferTo(String to, int amount) throws UnirestException {
        Unirest.post(restopoly.util.Ports.BANKSADDRESS + "/" + GAMEID + "/transfer/to/" + to + "/" + amount).asString();
    }

    public void transferFromTo(String from, String to, int amount) throws UnirestException {
        Unirest.post(restopoly.util.Ports.BANKSADDRESS + "/" + GAMEID + "/transfer/from/" + from + "/to/" + to + "/" + amount).asString();
    }

    public void createEvent(String type, String name, String reason, String resource, Player player) throws UnirestException{
        Event event = new Event(type,name,resource,reason,player);
        Unirest.post(restopoly.util.Ports.EVENTSADDRESS).queryString("gameid", GAMEID).body(new Gson().toJson(event)).asString();
    }

    public void subscribe(String type, String name, String reason, String resource, Player player) throws UnirestException{
        Event event = new Event(type,name,resource,reason,player);
        event.setGameid(GAMEID);
        Subscription subscription = new Subscription(GAMEID, PLAYERADDRESS,event);
        Unirest.post(restopoly.util.Ports.EVENTSADDRESS+"/subscriptions").body(new Gson().toJson(subscription)).asString();
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public static String getPLAYERADDRESS() {
        return PLAYERADDRESS;
    }

    public static void setPLAYERADDRESS(String PLAYERADDRESS) {
        RESTopoly.PLAYERADDRESS = PLAYERADDRESS;
    }

    public static Player getPLAYER() {
        return PLAYER;
    }

    public void addEvent(Event event){
        eventList.add(event);
    }

    public static void main(String args[]) throws UnirestException, IOException {

        URI uri = URI.create(restopoly.util.Ports.PLAYERSWEBSOCKETADDRESS);

        try {
            WebSocketClient clientSocket = new WebSocketClient();
            clientSocket.start();

            Future<Session> session = clientSocket.connect(new PlayerWebSocket(), uri);

        } catch (Exception e) {
            e.printStackTrace();
        }


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
                            GAMEID = INSTANCE.createGame();
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
                            INSTANCE.joinGame(gameid, playerid, name);
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Ready")) {
                        try {
                            INSTANCE.ready();
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Roll")) {
                        try {
                            JOptionPane.showMessageDialog(frame,
                                    INSTANCE.diceRoll());
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Account Balance")) {
                        try {
                            String account = JOptionPane.showInputDialog(frame,
                                    "Which account balance do you want to see?", null);
                            JOptionPane.showMessageDialog(frame, INSTANCE.accountBalance(account));
                        } catch (UnirestException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (cbl.getSelectedItem().toString().equals("Subscribe")) {
                        try {
                            String name = JOptionPane.showInputDialog(frame,
                                    "Which name does the event have which you want to subscribe to?", null);
                            INSTANCE.subscribe("", name, "", "", PLAYER);
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
                            INSTANCE.transferFrom(from, Integer.parseInt(amount));
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
                            INSTANCE.transferTo(to, Integer.parseInt(amount));
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
                            INSTANCE.transferFromTo(from, to, Integer.parseInt(amount));
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
                            INSTANCE.createEvent(type, name, reason, resource, PLAYER);
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
