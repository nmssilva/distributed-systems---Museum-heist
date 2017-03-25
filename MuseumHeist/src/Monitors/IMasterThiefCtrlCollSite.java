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
    
    public int getFreeAP();

    public boolean getPartiesFull();
    
    public int[] getEmptySlotParty();

    public void collectCanvas();
    
    public void handACanvas(boolean hasCanvas, int i);
    
    public boolean addThiefToParty(int thiefid);

    public void waitArrival();
    
    public void setEmptyRooms(int i, boolean f);

}
