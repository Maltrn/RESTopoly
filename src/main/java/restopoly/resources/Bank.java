package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class Bank {

    private String gameid;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    public Bank(String gameid){

        this.gameid = gameid;
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public Account getAccount(String playerid){
        for(Account account:  accounts){
            if(account.getId().equals(playerid)) return account;
        }
        return null;
    }

    public boolean deleteAccount(String playerid){
        for(Account account : accounts){
            if(account.getId().equals(playerid)) {
                accounts.remove(account);
                return true;
            }
        }
        return false;
    }

    public String getGameid() {
        return gameid;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
