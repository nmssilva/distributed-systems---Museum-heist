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
public interface CCSIMasterThief  extends Remote{

    public VectorTimestamp appraiseSit(int nAssaultThievesCS, VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp sendAssaultParty( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp takeARest( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp collectCanvas( VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp sumUpResults( VectorTimestamp vt) throws RemoteException;
    
}
