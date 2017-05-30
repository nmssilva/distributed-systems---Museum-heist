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
public interface LogIAssaultThief extends Remote{

    public void reportStatus() throws RemoteException;

    public void setAssaultThief(int thiefID, int status, int maxDisp, int partyID, int hasCanvas) throws RemoteException;
    
}
