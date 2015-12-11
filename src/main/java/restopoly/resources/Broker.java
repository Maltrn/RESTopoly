package restopoly.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by final-work on 11.12.15.
 */
public class Broker {

    public Broker(){

    }

    private Broker(String gameID, Map<String, owner> fieldMap){
        this.gameid = gameID;
        this.fieldMap = fieldMap;
    }


    String gameid;
    private Map<String, owner> fieldMap = new HashMap<>();

    public Broker(String gameid) {
        this.gameid = gameid;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }


    public boolean containField(String placeID){
        return fieldMap.containsKey(placeID);
    }

    public void putField(String placeId){
        if (!fieldMap.containsKey(placeId))fieldMap.put(placeId,   new owner());
    }

    public String getOwnerID(String p_id) {
        if (containField(p_id)){
            owner tOwner = fieldMap.get(p_id);
            return tOwner.getPlayerName();
        }
        return null;
    }

    public String getPrice(String p_id){
        if (containField(p_id)){
            owner tOwner = fieldMap.get(p_id);
            return tOwner.getPrice();
        }
        return null;
    }

    public boolean isAvalible(String p_id){
        if (containField(p_id)){
            owner tOwner = fieldMap.get(p_id);
            return tOwner.isfree();
        }
        return false;
    }


    public void buyField(String placeID, String playerID){
        if (containField(placeID)){
            owner tOwner = fieldMap.remove(placeID);
            tOwner.setPlayerName(playerID);
            tOwner.setIsfree(false);
            fieldMap.put(placeID, tOwner);
        }
    }

    public Broker createCopy() {
        return new Broker(gameid, fieldMap);
    }


    private class owner{

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public boolean isfree() {
            return isfree;
        }

        public void setIsfree(boolean isfree) {
            this.isfree = isfree;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        private String playerName = null;
        private boolean isfree=true;
        private String price;



    }

}
