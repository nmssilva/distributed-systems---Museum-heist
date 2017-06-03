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
public interface MuseumIAssaultThief extends Remote {

    public VectorTimestamp rollACanvas(int roomID,  VectorTimestamp vt) throws RemoteException;
    
}
