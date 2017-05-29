package serverSide;

import static auxiliary.Heist.*;
import auxiliary.MemFIFO;
import interfaces.APInterface;
import interfaces.CCSInterface;
import interfaces.CSInterface;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class ConcentrationSite implements CSInterface {

    private MemFIFO waitQueue;                     // Wainting queue for ready assault thieves 
    private MemFIFO waitQueueDisp;                     // Wainting queue for ready assault thieves      
    private int nAssaultThievesCS;                 // Number of assault thieves in the concentration site
    private int partyID;
    
    private CCSInterface ccs;
    private APInterface ap[];

    public ConcentrationSite(CCSInterface ccs, APInterface ap[]) {
        
        this.ccs = ccs;
        this.ap = ap;
        
        waitQueue = new MemFIFO(THIEVES_NUMBER);
        waitQueueDisp = new MemFIFO(THIEVES_NUMBER);
        nAssaultThievesCS = 0;

    }

    /**
     *
     * Master Thief blocks until the number of Assault Thieves in the
     * Concentration Site is equal to the number of total Assault Thieves of the
     * heist. The status of the Master Thief is changed to DECIDING_WHAT_TO_DO
     * in the end of the operation.
     *
     */
    @Override
    public synchronized void startOfOperations() {

        while (nAssaultThievesCS != THIEVES_NUMBER) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Resets the Assault Thief current thread, adds it's reference to the
     * waiting queue and blocks it until the Master Thief executes
     * prepareAssaultParty or the heist ends. The status of the Assault Thief
     * current thread is changed to OUTSIDE in the end of the operation.
     *
     * @param thiefID ID of Assault Thief
     * @param maxDisp Maximum Displacement of Assault Thief
     * @return Party ID if Needed, -1 if Not Needed
     */
    @Override
    public synchronized int amINeeded(int thiefID, int maxDisp) {
        // Reset thief

        nAssaultThievesCS++;
        waitQueue.write(thiefID);
        waitQueueDisp.write(maxDisp);
        
        this.ccs.isReady();

        notifyAll();

        while (!inParty(thiefID)) {
            try {
                
                int value = this.ccs.nextEmptyRoom();

                if (value == -1 && nAssaultThievesCS == THIEVES_NUMBER) {
                    return -1;
                }

                wait();
            } catch (InterruptedException e) {

            }
        }

        return partyID;
    }

    /**
     * Checks if the Assault Thief current thread is in the Assault Party.
     *
     * @param thiefID ID of Assault Thief
     * @return True, if the Assault Thief current thread is in the Assault Party
     * or false if otherwise.
     */
    public synchronized boolean inParty(int thiefID) {
        for (int i = 0; i < MAX_ASSAULT_PARTIES; i++) {
            
            int[] pthieves = this.ap[i].getPartyThieves();

            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (pthieves[j] == thiefID) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Master Thief prepares and Assault Party. It adds elements to the Assault
     * Party and then assigns it a room and the turn of the element to first
     * crawl in. The status of the Master Thief is changed to ASSEMBLING_A_GROUP
     * in the end of the operation.
     */
    @Override
    public synchronized void prepareAssaultParty() { 

        partyID = this.ccs.getNextParty();
        ////System.out.println("PARTYID: " + partyID);
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            
            this.ap[partyID].addThief((int) waitQueue.read(), (int) waitQueueDisp.read());

            nAssaultThievesCS--;
        }
        
        this.ap[partyID].setFirst();

        notifyAll();        

        int roomID = this.ccs.getNextRoom();
        
        this.ap[partyID].setRoom(roomID);

    }

    /**
     * Get the number of Assault Thieves in Concentration Site.
     *
     * @return Number of Assault Thieves in Concentration Site
     */
    @Override
    public int getnAssaultThievesCS() {
        try {
            Thread.sleep((long) (100));
        } catch (InterruptedException e) {
        }
        return nAssaultThievesCS;
    }

}
