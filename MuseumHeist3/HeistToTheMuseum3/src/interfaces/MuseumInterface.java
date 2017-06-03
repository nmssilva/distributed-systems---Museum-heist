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
public interface MuseumInterface extends MuseumIAssaultThief, Remote{

    public VectorTimestamp getRoom(int roomID, VectorTimestamp vt) throws RemoteException;

    public VectorTimestamp getDistRoom(int roomID, VectorTimestamp vt) throws RemoteException;
    
}
