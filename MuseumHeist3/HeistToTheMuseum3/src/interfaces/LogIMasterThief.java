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
public interface LogIMasterThief  extends Remote{
    
    public void reportInitialStatus() throws RemoteException;

    public VectorTimestamp setMasterThief(int status, VectorTimestamp vt) throws RemoteException;

    public void reportFinalStatus() throws RemoteException;
    
}
