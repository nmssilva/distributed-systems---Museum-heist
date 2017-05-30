/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Nuno Silva
 */
public interface CCSIAssaultThief  extends Remote{

    public void prepareExcursion()  throws RemoteException;

    public boolean handCanvas(int thiefID, int partyID, int hasCanvas)  throws RemoteException;
    
}
