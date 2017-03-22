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
public interface IMasterThiefCtrlCollSite {

    public void startOperations();

    public boolean[] getEmptyRooms();

    public void prepareAssaultParty();

    public void sendAssaultParty();

    public void takeARest();

    public void sumUpResults();
    
    public void appraiseSit();

    public int[] getPartyNotFull();

    public void collectCanvas();
    
    public void handACanvas();

}
