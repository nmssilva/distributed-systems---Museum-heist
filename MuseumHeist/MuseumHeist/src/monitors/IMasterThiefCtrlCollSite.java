/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import entities.MasterThief;

/**
 *
 * @author Nuno Silva
 */
public interface IMasterThiefCtrlCollSite {

    public void startOperations();

    public void sumUpResults();

    public void prepareAssaultParty();

    public boolean getPartiesFull();

    public void takeARest();

    public void appraiseSit();

    public void sendAssaultParty(IAssaultParty apToSend, IRoom room);

    public void collectCanvas();
    
    public void waitForMaster();
    
    public int getTotalPaintings();
    
    public void handACanvas();
    
    public MasterThief getMasterthief();
    
}
