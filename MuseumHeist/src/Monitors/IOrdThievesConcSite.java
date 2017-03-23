/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

/**
 *
 * @author Nuno Silva
 */
public interface IOrdThievesConcSite {

    public void amReady(int thiefid);

    public boolean getFreeAssaultThief(int thiefid);

    public int getAssaultParty(int thiefid);

    public void amINeeded(int thiefid);
    
    public void callAssaultThief(int thiefid);

    public void setFreeAssaultThief(int thiefid);

    public int getNAssaultThievesCs();
    
}
