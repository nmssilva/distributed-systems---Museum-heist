/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Nuno Silva
 */
public interface CCSIAssaultThief {

    public void prepareExcursion();

    public boolean handCanvas(int thiefID, int partyID, int hasCanvas);
    
}