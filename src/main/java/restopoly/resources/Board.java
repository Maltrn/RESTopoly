package restopoly.resources;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

/**
 * Created by final-work on 21.11.15.
 */
public class Board {

    private String gameid;
    private ArrayList<Field> fields = new ArrayList<Field>();
    private Map<String, Integer> positions = new HashMap<String, Integer>();

    public Board(String gameid){
        this.gameid = gameid;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public Field getField(int i) {
        return fields.get(i);
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    public void calculatePositions(){
        for(Field field : fields){
            for (Player player : field.getPlayers()){
                if (player != null) {
                    positions.put(player.getId(), player.getPosition());
                }
            }
        }

    }

    public String getGameid() {
        return gameid;
    }

}