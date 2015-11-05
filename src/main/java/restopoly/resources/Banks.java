package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class Banks {
    private ArrayList<Bank> banks = new ArrayList<Bank>();

    public Banks(){}

    public ArrayList<Bank> getBanks() {
        return banks;
    }

    public Bank getBank(String gameid) {
        for(Bank bank : banks){
            if(bank.getGameid().equals(gameid)) return bank;
        }
        return null;
    }
    public void addBank(Bank bank){
        banks.add(bank);
    }
}
