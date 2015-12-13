package restopoly.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mizus on 11.12.15.
 */
public class Mutex {

    private Map<String, Map<String, InnerMutex>> mapMutex;

    public Mutex() {
        mapMutex = new HashMap<String,Map<String, InnerMutex>>();
    }



    public String hasMutex(String gameId){
        if (mapMutex.containsKey(gameId)){

        }
        return null;
    }

    public boolean hasMutex(String playerId, String gameId){
        boolean result = mapMutex.containsKey(gameId);
        if (result){
            result = mapMutex.get(gameId).containsKey(playerId);
            if (result){
                InnerMutex tInnerMutex = mapMutex.get(gameId).get(playerId);
                result = tInnerMutex.isMutexing();
            }
        }
        return result;
    }


    public void addGame(String gameId){
        mapMutex.put(gameId, new HashMap<String, InnerMutex>());
    }

    public void removeGame(String gameId){
        mapMutex.remove(gameId);
    }








    private class InnerMutex{

        public boolean isMutexing() {
            return mutexing;
        }

        public void setMutexing(boolean mutexing) {
            this.mutexing = mutexing;
        }

        public int getTurns() {
            return turns;
        }

        public void setTurns(int turns) {
            this.turns = turns;
        }

        private boolean mutexing;
        private int turns;

    }


}
