/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OrdThievesConcSite;

/**
 *
 * @author Nuno Silva
 */
public interface IOrdThievesConcSite {

    public void amReady(int thiefid);

    public boolean getBusyAssaultThief(int thiefid);

    public int getAssaultParty(int thiefid);

    public void amINeeded(int thiefid);
    
}
