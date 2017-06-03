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
public interface CCSInterface extends CCSIAssaultThief, CCSIMasterThief, Remote{
    
    public VectorTimestamp isReady( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp nextEmptyRoom( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp getNextParty( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp getNextRoom( VectorTimestamp vt) throws RemoteException;
    
}
