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
public interface APInterface extends APIAssaultThief, Remote{

    public VectorTimestamp getPartyThieves(VectorTimestamp vt) throws RemoteException; // int[]

    public VectorTimestamp addThief(int id, int maxdisp,VectorTimestamp vt) throws RemoteException; //boolean

    public VectorTimestamp setFirst(VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp setRoom(int roomID,VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp isEmptyAP(VectorTimestamp vt) throws RemoteException; //boolean

    public VectorTimestamp setPartyThieves(int i, int i0,VectorTimestamp vt) throws RemoteException;
    
}
