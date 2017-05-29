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
public interface APInterface extends APIAssaultThief, Remote{

    public int[] getPartyThieves();

    public boolean addThief(int id, int maxdisp);

    public void setFirst();

    public void setRoom(int roomID);

    public boolean isEmptyAP();

    public void setPartyThieves(int i, int i0);
    
}
