package restopoly.util;

/**
 * Created by mizus on 16.12.15.
 */
public interface Ports {

    String GAMESADDRESS = "http://172.18.0.53:12010/games";
    String BOARDSADDRESS = "http://172.18.0.54:12011/boards";
    String BANKSADDRESS = "http://172.18.0.55:12012/banks";


//    String BANKSADDRESS = "http://172.18.0.54:12012/banks";
    String DICEADDRESS = "http://172.18.0.9:4567/dice";
//    String GAMESADDRESS = "http://172.18.0.10:4567/games";
//    String BOARDSADDRESS = "http://172.18.0.12:4567/boards";
    String EVENTSADDRESS = "http://172.18.0.13:4567/events";
    String PLAYERSADDRESS = "http://172.18.0.16:4567/player";
    String PLAYERSWEBSOCKETADDRESS = "ws://172.18.0.16:4567";
    String BROKERSADDRESS = "http://172.18.0.17:4567/brokers";

}
