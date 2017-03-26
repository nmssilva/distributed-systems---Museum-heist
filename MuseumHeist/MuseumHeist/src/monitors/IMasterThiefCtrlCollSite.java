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

    /**
     * transits Master Thief state to DECIDING_WHAT_TO_DO
     */
    public void startOperations();

    /**
     * transits Master Thief state to PRESENTING_THE_REPORT
     */
    public void sumUpResults();

    /**
     * transits Master Thief state to ASSEMBLING_A_GROUP
     */
    public void prepareAssaultParty();

    /**
     *
     * @return true if all parties are full, false if otherwise.
     */
    public boolean getPartiesFull();

    /**
     * transits Master Thief state to WAITING_FOR_ARRIVAL
     */
    public void takeARest();

    /**
     * transits Master Thief state to DECIDING_WHAT_TO_DO
     */
    public void appraiseSit();

    /**
     *
     * @param apToSend Assault Party to send
     * @param room Room where Assault Party goes
     */
    public void sendAssaultParty(IAssaultParty apToSend, IRoom room);

    /**
     * transits Master Thief state to DECIDING_WHAT_TO_DO
     */
    public void collectCanvas();
    
    /**
     * thiefs wait for master if she is not in WAITING FOR ARRIVAL state
     */
    public void waitForMaster();
    
    /**
     *
     * @return Total Number Of paintings collected
     */
    public int getTotalPaintings();
    
    /**
     * thiefs hand a canvas in collection site
     */
    public void handACanvas();
    
    /**
     *
     * @return MasterThief
     */
    public MasterThief getMasterthief();
    
}
