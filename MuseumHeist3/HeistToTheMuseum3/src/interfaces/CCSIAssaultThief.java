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
public interface CCSIAssaultThief  extends Remote{

    public VectorTimestamp prepareExcursion( VectorTimestamp vt)  throws RemoteException;

    public VectorTimestamp handCanvas(int thiefID, int partyID, int hasCanvas, VectorTimestamp vt)  throws RemoteException;
    
}
