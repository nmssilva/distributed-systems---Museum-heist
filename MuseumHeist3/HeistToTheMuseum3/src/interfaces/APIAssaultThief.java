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
public interface APIAssaultThief  extends Remote{

    public VectorTimestamp crawlIn(int thiefID,VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp getRoomID(VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp reverseDirection(int thiefID,VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp crawlOut(int thiefID,VectorTimestamp vt) throws RemoteException;
    
}
