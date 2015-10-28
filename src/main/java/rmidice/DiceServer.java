package rmidice;

import java.rmi.*;
import java.rmi.registry.*;
import java.net.MalformedURLException;

/**
 * Created by Krystian.Graczyk on 26.10.15.
 */
public class DiceServer {
    public static void main(String args[]){
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("Roll", new Dice());
        }
        catch (MalformedURLException ex) {}
        catch (RemoteException ex) {}
    }
}
