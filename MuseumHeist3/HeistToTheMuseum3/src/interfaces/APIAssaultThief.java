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
public interface APIAssaultThief  extends Remote{

    public void crawlIn(int thiefID) throws RemoteException;

    public int getRoomID() throws RemoteException;

    public void reverseDirection(int thiefID) throws RemoteException;

    public void crawlOut(int thiefID) throws RemoteException;
    
}
