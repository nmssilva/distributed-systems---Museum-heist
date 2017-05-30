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
public interface APInterface extends APIAssaultThief, Remote{

    public int[] getPartyThieves() throws RemoteException;

    public boolean addThief(int id, int maxdisp) throws RemoteException;

    public void setFirst() throws RemoteException;

    public void setRoom(int roomID) throws RemoteException;

    public boolean isEmptyAP() throws RemoteException;

    public void setPartyThieves(int i, int i0) throws RemoteException;
    
}
