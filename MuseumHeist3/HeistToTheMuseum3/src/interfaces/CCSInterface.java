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
public interface CCSInterface extends CCSIAssaultThief, CCSIMasterThief, Remote{
    
    public void isReady() throws RemoteException;

    public int nextEmptyRoom() throws RemoteException;

    public int getNextParty() throws RemoteException;

    public int getNextRoom() throws RemoteException;
    
}
