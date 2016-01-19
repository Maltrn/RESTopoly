package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by final-work on 11.12.15.
 */
public class Broker {

//    - broker: |
//    {
//        "type": "object",
//            "$schema": "http://json-schema.org/draft-03/schema",
//            "id": "broker",
//            "properties": {
//        "estates": { "type":"string", "required": true, "description":"uri to the estates managed by the broker" }
//    }
//    }

    String gameid;
    ArrayList<Estate> estates = new ArrayList<>();

    public Broker(String gameid){
        this.gameid = gameid;
    }

    public Broker(String gameid, Estate estate){
        this.gameid = gameid;
        this.estates.add(estate);
    }

    public ArrayList<Estate> getEstates() {
        return estates;
    }

    public void setEstates(ArrayList<Estate> estates) {
        this.estates = estates;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

}
