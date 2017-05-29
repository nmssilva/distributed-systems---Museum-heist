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
public interface LogIAssaultThief {

    public void reportStatus();

    public void setAssaultThief(int thiefID, int status, int maxDisp, int partyID, int hasCanvas);
    
}
