package restopoly.resources;

import org.json.JSONObject;

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

    public String getRent(String placeID) {
        if (containField(placeID)){
            owner tOwner = fieldMap.get(placeID);
            return tOwner.getRent();

        }
        return  null;
    }

    public int[] getRentAry(String placeID) {
        if (containField(placeID)){
            owner tOwner = fieldMap.get(placeID);
            return tOwner.getRent_level_ary();
        }
        return  null;
    }

    public int[] getCostAry(String placeID) {
        if (containField(placeID)){
            owner tOwner = fieldMap.get(placeID);
            return tOwner.getCoast_level_ary();
        }
        return  null;
    }

    public JSONObject getReturnCode(String placeId){
        JSONObject result = null;
        if(containField(placeId)){
            result.put("place", placeId);
            result.put("owner", getOwnerID(placeId));
            result.put("value", getPrice(placeId));
            result.put("rent", getRentAry(placeId));
            result.put("cost", getCostAry(placeId));
            result.put("houses", getHouses(placeId));
            result.put("visit", getVisit(placeId));
            result.put("hypocredit", getHypoCredit(placeId));
        }
        return result;
    }

    public String getVisit(String placeId) {
        if (containField(placeId)){
            return fieldMap.get(placeId).getVisits();
        }
        return null;
    }

    public String getHypoCredit(String placeId){
        if (containField(placeId)){
            return fieldMap.get(placeId).getHypocredit();
        }
        return null;
    }

    public int getHouses(String placeId) {
        if (containField(placeId)){
            owner tOwner = fieldMap.get(placeId);
            return tOwner.getHouses();
        }
        return -1;
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


        public void setRent(String rent){
            this.rent = rent;
        }


        public String getRent(){
            return rent;
        }

        public int[] getRent_level_ary() {
            return rent_level_ary;
        }

        public void setRent_level_ary(int[] rent_level_ary) {
            this.rent_level_ary = rent_level_ary;
        }

        public int[] getCoast_level_ary() {
            return coast_level_ary;
        }

        public void setCoast_level_ary(int[] coast_level_ary) {
            this.coast_level_ary = coast_level_ary;
        }

        public int getHouses() {
            return houses;
        }

        public void setHouses(int houses) {
            this.houses = houses;
        }

        public String getVisits() {
            return visits;
        }

        public void setVisits(String visits) {
            this.visits = visits;
        }

        public String getHypocredit() {
            return hypocredit;
        }

        public void setHypocredit(String hypocredit) {
            this.hypocredit = hypocredit;
        }

        private boolean isfree=true;
        private String price;
        private String rent;
        private int[] rent_level_ary;
        private int[] coast_level_ary;
        private int houses;
        private String visits;
        private String hypocredit;
        private String playerName = null;


    }

}
