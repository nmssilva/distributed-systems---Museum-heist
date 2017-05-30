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
public interface LoggerInterface extends LogIAssaultThief, LogIMasterThief, Remote{

    
    public void signalShutdown() throws RemoteException;

    public void setAssaultParty(int id, int[] partyThieves, int[] partyThievesPos, int roomID) throws RemoteException;

    public void setMuseum(int[] roomsdistance, int[] roomspaintings) throws RemoteException;
    
    
}
