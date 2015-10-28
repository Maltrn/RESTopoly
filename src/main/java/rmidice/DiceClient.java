package rmidice;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Krystian.Graczyk on 26.10.15.
 */
public class DiceClient {
    public static void main(String args[]){
        try{
            DiceRMI dice = (DiceRMI)Naming.lookup("//127.0.0.1:1099/Roll");
            System.out.println(dice.roll().getNumber());
        }
        catch (RemoteException | NotBoundException | MalformedURLException e) {}
    }
}
