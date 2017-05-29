/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;

/**
 *
 * @author Nuno Silva
 */
public interface CCSInterface extends CCSIAssaultThief, CCSIMasterThief, Remote{
    
    public void isReady();

    public int nextEmptyRoom();

    public int getNextParty();

    public int getNextRoom();
    
}
