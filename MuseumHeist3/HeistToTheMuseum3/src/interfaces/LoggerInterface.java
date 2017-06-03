/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import auxiliary.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Nuno Silva
 */
public interface LoggerInterface extends LogIAssaultThief, LogIMasterThief, Remote{

    
    public void signalShutdown() throws RemoteException;

    public VectorTimestamp setAssaultParty(int id, int[] partyThieves, int[] partyThievesPos, int roomID,VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp setMuseum(int[] roomsdistance, int[] roomspaintings,VectorTimestamp vt) throws RemoteException;
    
    
}
