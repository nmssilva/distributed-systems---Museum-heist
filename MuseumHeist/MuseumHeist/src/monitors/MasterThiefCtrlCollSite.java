/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import static GenRepOfInfo.Heist.*;
import entities.MasterThief;
import entities.Thief;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva
 */
public class MasterThiefCtrlCollSite implements IMasterThiefCtrlCollSite {

    private int totalPaintings;
    private MasterThief masterthief;

    public MasterThief getMasterthief() {
        return masterthief;
    }

    public MasterThiefCtrlCollSite() {
        this.totalPaintings = 0;
    }

    @Override
    public int getTotalPaintings() {
        return totalPaintings;
    }

    
    
    public void setMasterThief(MasterThief masterThief) {
        this.masterthief = masterThief;
    }

    @Override
    public synchronized void startOperations() {
        this.masterthief.setState(DECIDING_WHAT_TO_DO);
        notifyAll();
    }

    @Override
    public synchronized void sumUpResults() {
        this.masterthief.setState(PRESENTING_THE_REPORT);
        notifyAll();
    }

    @Override
    public synchronized void prepareAssaultParty() {
        this.masterthief.setState(ASSEMBLING_A_GROUP);
        notifyAll();
    }

    @Override
    public boolean getPartiesFull() {

        MasterThief mt = (MasterThief) Thread.currentThread();

        for (int i = 0; i < (THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES); i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (mt.getAp()[i].getThieves()[j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public synchronized void takeARest() {
        this.masterthief.setState(WAITING_FOR_ARRIVAL);
        notifyAll();
    }

    @Override
    public synchronized void appraiseSit() {
        this.masterthief.setState(DECIDING_WHAT_TO_DO);
        notifyAll();
    }

    @Override
    public synchronized void sendAssaultParty(IAssaultParty apToSend, IRoom room) {
        notifyAll();
        apToSend.setFree(false);
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            apToSend.getThieves()[i].setFree(false);
        }
    }

    @Override
    public synchronized void collectCanvas() {
        this.masterthief.setState(DECIDING_WHAT_TO_DO);
        notifyAll();
    }

    @Override
    public synchronized void waitForMaster() {

        notifyAll();

        while (masterthief.getMasterState() != WAITING_FOR_ARRIVAL) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MasterThiefCtrlCollSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void handACanvas() {

        Thief thief = (Thief) Thread.currentThread();

        if (thief.isHasCanvas()) {
            this.totalPaintings++;
            System.out.println("TOTAL COLLECTED CANVAS: " + this.totalPaintings);
            thief.getAP().getRoom().setFree(true);
        } else {
            System.out.println("Canvas not recovered");
            thief.getAP().getRoom().setFree(false);
        }

        thief.getAP().setFree(true);

        thief.getAP().getThieves()[thief.getPosInParty()] = null;
        thief.setFree(true);

        notifyAll();
    }

}
