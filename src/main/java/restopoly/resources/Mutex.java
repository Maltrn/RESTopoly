package restopoly.resources;

import java.util.*;

/**
 * Created by mizus on 11.12.15.
 */
public class Mutex {


    // GameID, PlayerId
    private Map<String, Map<String, InnerMutex>> mapMutex;

    public Mutex() {
        mapMutex = new HashMap<String,Map<String, InnerMutex>>();
    }


    private boolean containGameMutex(String gameId){
        return mapMutex.containsKey(gameId);
    }

    public String hasMutex(String gameId){
        if (mapMutex.containsKey(gameId)){
            Iterator<Map.Entry<String, InnerMutex>> iterator = mapMutex.get(gameId).entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, InnerMutex> entry = iterator.next();
                String playerKey = entry.getKey();
                InnerMutex mutex = entry.getValue();
                if (mutex.isMutexing())return playerKey;
            }
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

    public void chanceMutexToNextPlayer(String gameId){
        if(containGameMutex(gameId)){
            String playerId = getNextPlayer(gameId);
            InnerMutex mutex = mapMutex.get(gameId).get(playerId);
            mutex.setMutexing(true);
        }
    }

    public void chanceMutexToPlayer(String gameId, String playerId){
        if(containGameMutex(gameId)){
            Iterator<Map.Entry<String, InnerMutex>> iterator = mapMutex.get(gameId).entrySet().iterator();
            boolean mutex_is_free = true;
            while (iterator.hasNext()){
                Map.Entry<String, InnerMutex> entry = iterator.next();
                InnerMutex mutex = entry.getValue();
                if (mutex.isMutexing()) mutex_is_free = false;
            }
            if (mutex_is_free)
                mapMutex.get(gameId).get(playerId).setMutexing(mutex_is_free);
        }
    }

    public void addTurn(String playerId, String gameID){
        if (containGameMutex(gameID))
            if (mapMutex.get(gameID).containsKey(playerId) && mapMutex.get(gameID).get(playerId).isMutexing()){
                InnerMutex mutex = mapMutex.get(gameID).remove(playerId);
                mutex.setTurns(mutex.getTurns()+1);
                mutex.setMutexing(false);
                mapMutex.get(gameID).put(playerId,mutex);
            }
    }

    public void addGame(String gameId){
        if(!containGameMutex(gameId))
            mapMutex.put(gameId, new HashMap<String, InnerMutex>());
    }

    public void removeGame(String gameId){
        mapMutex.remove(gameId);
    }

    public String getNextPlayer(String gameID){
        String result = "";
        if (containGameMutex(gameID)){
            Map<String, InnerMutex> tGame = mapMutex.get(gameID);
            Iterator<Map.Entry<String, InnerMutex>> iterator = tGame.entrySet().iterator();
            int minturns = Integer.MAX_VALUE;
            while(iterator.hasNext()){
                Map.Entry<String, InnerMutex> tEntry = iterator.next();
                String game = tEntry.getKey();
                InnerMutex mutex = tEntry.getValue();
                if (mutex.getTurns() < minturns)
                     result = game;
            }
        }
        return result;
    }

    public void addNextTurnPlayer(String gameId, String playerId){


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

        public boolean isWaitOfMutex(){
            return waitOfMutex;
        }

        public void setWaitOfMutex(boolean waitOfMutex){
            this.waitOfMutex = waitOfMutex;
        }


        private boolean mutexing;
        private int turns;
        private boolean waitOfMutex;


    }



}
