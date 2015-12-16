package restopoly.resources;

import java.util.*;

/**
 * Created by mizus on 11.12.15.
 */
public class Mutex {

    // GameID, PlayerId
    private Map<String, Map<String, InnerMutex>> mapMutex;

    public Mutex() {
        mapMutex = new HashMap<String, Map<String, InnerMutex>>();
    }

    private boolean containGameMutex(String gameId){
        return mapMutex.containsKey(gameId);
    }

    public String playerWithMutex(String gameId){
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

    public boolean playerHasMutex(String gameId, String playerId){
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

    public void changeMutexToNextPlayer(String gameId){
        if(containGameMutex(gameId)){
            String playerId = getNextPlayer(gameId);
            InnerMutex mutex = mapMutex.get(gameId).get(playerId);
            mutex.setMutexing(true);
        }
    }

    public void changeMutexToPlayer(String gameId, String playerId){
        if(containGameMutex(gameId)){
            Iterator<Map.Entry<String, InnerMutex>> iterator = mapMutex.get(gameId).entrySet().iterator();
            boolean mutex_is_free = true;
            while (iterator.hasNext()){
                Map.Entry<String, InnerMutex> entry = iterator.next();
                InnerMutex mutex = entry.getValue();
                if (mutex.isMutexing()) mutex_is_free = false;
            }
            if (mutex_is_free) mapMutex.get(gameId).get(playerId).setMutexing(mutex_is_free);
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
        if (!isMutexFree(gameId)){
            if (containGameMutex(gameId) && containPlayer(gameId,playerId)){
                Iterator<Map.Entry<String, InnerMutex>> iterator = mapMutex.get(gameId).entrySet().iterator();
                int waitPosition = 0;
                while (iterator.hasNext()){
                    InnerMutex mutex = iterator.next().getValue();
                    if (mutex.isWaitOfMutex()) waitPosition++;
                }
                InnerMutex mutex = mapMutex.get(gameId).remove(playerId);
                mutex.setWaitOfMutex(true);
                mutex.setWaitPosition(waitPosition);
                mapMutex.get(gameId).put(playerId,mutex);
            }
        }
    }

    private boolean containPlayer(String gameId, String player){
        return containGameMutex(gameId) && mapMutex.get(gameId).containsKey(player);
    }

    public void addPlayer(String gameId, String playerId){
        if (containGameMutex(gameId)){
            if(!containPlayer(gameId, playerId))
                mapMutex.get(gameId).put(playerId, new InnerMutex());
        }
    }

    public boolean isMutexFree(String gameid){
        boolean result = true;
        if (containGameMutex(gameid)){
            Iterator<Map.Entry<String, InnerMutex>> iterator = mapMutex.get(gameid).entrySet().iterator();
            while (iterator.hasNext()){
                if (iterator.next().getValue().isMutexing()) return false;
            }
        }
        return result;
    }



    public void removePlayer(String gameId, String playerId){
        if (containGameMutex(gameId)){
            if(containPlayer(gameId, playerId))
                mapMutex.get(gameId).remove(playerId);
        }
    }

    public boolean isPlayersWaiting(String game){
        boolean result = false;
        if (containGameMutex(game)){




        }

        return true;
    }

    private class InnerMutex{

        private boolean hasMutex;
        private int turns = 0;
        private boolean waitOfMutex;
        private int waitPosition;

        public boolean isMutexing() {
            return hasMutex;
        }

        public void setMutexing(boolean hasMutex) {
            this.hasMutex = hasMutex;
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

        public void setWaitPosition(int pos){
            waitPosition = pos;
        }

        public int getWaitPosition(){
            return waitPosition;
        }

    }

}
