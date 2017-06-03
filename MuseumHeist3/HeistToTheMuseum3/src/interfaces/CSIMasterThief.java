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
public interface CSIMasterThief  extends Remote{

    public VectorTimestamp getnAssaultThievesCS( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp startOfOperations( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp prepareAssaultParty( VectorTimestamp vt) throws RemoteException;
    
}
