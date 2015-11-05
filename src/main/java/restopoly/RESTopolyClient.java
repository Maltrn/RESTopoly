package restopoly;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.resources.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Krystian.Graczyk on 28.10.15.
 */
public class RESTopolyClient {

    private static String GAMESADRESS;
    private static String GAMEID;

    public RESTopolyClient(){}

    public String createGame() throws UnirestException {
        HttpResponse response = Unirest.post(GAMESADRESS+"/games").asJson();
        Gson gson = new Gson();
        Game game = gson.fromJson(response.getBody().toString(),Game.class);
        return game.getGameid();

    }

    public void joinGame(String gameid, String playerid, String name, String uri) throws UnirestException {
        Unirest.put(GAMESADRESS+"/games/"+gameid+"/players/"+playerid).queryString("name", name).queryString("uri", uri).asString();
    }

    public void ready(String gameid, String playerid) throws UnirestException {
        Unirest.put(GAMESADRESS+"/games/"+gameid+"/players/"+playerid+"/ready").asString();
    }

    public static void main(String args[]) throws UnirestException {
        GAMESADRESS = "http://0.0.0.0:4567";

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

        String[] choices = { "Create Game","Join Game", "Ready"};

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
                        String uri = JOptionPane.showInputDialog(frame,
                                "What is your URI?", null);
                        try {
                            client.joinGame(gameid,playerid,name,uri);
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
                }
            }
        });
        panel.add(btn);
        btn.setVisible(true);
        frame.setVisible(true);

    }
}
