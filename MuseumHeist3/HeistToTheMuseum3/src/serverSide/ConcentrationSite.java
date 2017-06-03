package serverSide;

import auxiliary.Heist;
import static auxiliary.Heist.*;
import auxiliary.MemFIFO;
import auxiliary.VectorTimestamp;
import interfaces.APInterface;
import interfaces.CCSInterface;
import interfaces.CSInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private VectorTimestamp clocks;

    public ConcentrationSite(CCSInterface ccs, APInterface ap[]) {

        this.ccs = ccs;
        this.ap = ap;

        waitQueue = new MemFIFO(THIEVES_NUMBER);
        waitQueueDisp = new MemFIFO(THIEVES_NUMBER);
        nAssaultThievesCS = 0;

        this.clocks = new VectorTimestamp(Heist.THIEVES_NUMBER+1, 0);

    }

    /**
     *
     * Master Thief blocks until the number of Assault Thieves in the
     * Concentration Site is equal to the number of total Assault Thieves of the
     * heist. The status of the Master Thief is changed to DECIDING_WHAT_TO_DO
     * in the end of the operation.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp startOfOperations(VectorTimestamp vt) {

        this.clocks.update(vt);
        while (nAssaultThievesCS != THIEVES_NUMBER) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        return this.clocks.clone();
    }

    /**
     * Resets the Assault Thief current thread, adds it's reference to the
     * waiting queue and blocks it until the Master Thief executes
     * prepareAssaultParty or the heist ends. The status of the Assault Thief
     * current thread is changed to OUTSIDE in the end of the operation.
     *
     * @param thiefID ID of Assault Thief
     * @param maxDisp Maximum Displacement of Assault Thief
     * @param vt VectorTimestamp
     * @return Party ID if Needed, -1 if Not Needed
     */
    @Override
    public synchronized VectorTimestamp amINeeded(int thiefID, int maxDisp, VectorTimestamp vt) {
        // Reset thief

        this.clocks.update(vt);

        nAssaultThievesCS++;
        waitQueue.write(thiefID);
        waitQueueDisp.write(maxDisp);

        try {
            this.ccs.isReady(this.clocks.clone());
        } catch (RemoteException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }

        notifyAll();

        try {
            while (!inParty(thiefID, this.clocks.clone()).getBool()) {
                try {

                    int value = this.ccs.nextEmptyRoom(this.clocks.clone()).getInteger();

                    if (value == -1 && nAssaultThievesCS == THIEVES_NUMBER) {
                        this.clocks.setInteger(-1);
                        return this.clocks.clone();
                    }

                    wait();
                } catch (InterruptedException e) {

                } catch (RemoteException ex) {
                    Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

        this.clocks.setInteger(partyID);
        return this.clocks.clone();
    }

    /**
     * Checks if the Assault Thief current thread is in the Assault Party.
     *
     * @param thiefID ID of Assault Thief
     * @param vt VectorTimestamp
     * @return True, if the Assault Thief current thread is in the Assault Party
     * or false if otherwise.
     */
    public synchronized VectorTimestamp inParty(int thiefID, VectorTimestamp vt) throws RemoteException {

        this.clocks.update(vt);

        for (int i = 0; i < MAX_ASSAULT_PARTIES; i++) {

            int[] pthieves = this.ap[i].getPartyThieves(this.clocks.clone()).getArray();

            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (pthieves[j] == thiefID) {
                    this.clocks.setBool(true);
                    return this.clocks.clone();
                }
            }
        }

        this.clocks.setBool(false);
        return this.clocks.clone();
    }

    /**
     * Master Thief prepares and Assault Party. It adds elements to the Assault
     * Party and then assigns it a room and the turn of the element to first
     * crawl in. The status of the Master Thief is changed to ASSEMBLING_A_GROUP
     * in the end of the operation.
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp prepareAssaultParty(VectorTimestamp vt) {

        this.clocks.update(vt);
        
        try {
            partyID = this.ccs.getNextParty(this.clocks.clone()).getInteger();
        } catch (RemoteException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        ////System.out.println("PARTYID: " + partyID);
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {

            try {
                this.ap[partyID].addThief((int) waitQueue.read(), (int) waitQueueDisp.read(), this.clocks.clone());
            } catch (RemoteException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }

            nAssaultThievesCS--;
        }

        try {
            this.ap[partyID].setFirst(this.clocks.clone());
        } catch (RemoteException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }

        notifyAll();

        int roomID = 0;
        try {
            roomID = this.ccs.getNextRoom(this.clocks.clone()).getInteger();
        } catch (RemoteException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.ap[partyID].setRoom(roomID,this.clocks.clone());
        } catch (RemoteException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.clocks.clone();
        
    }

    /**
     * Get the number of Assault Thieves in Concentration Site.
     *
     * @param vt VectorTimestamp
     * @return Number of Assault Thieves in Concentration Site
     */
    @Override
    public VectorTimestamp getnAssaultThievesCS(VectorTimestamp vt) {
        this.clocks.update(vt);
        try {
            Thread.sleep((long) (100));
        } catch (InterruptedException e) {
        }
        this.clocks.setInteger(nAssaultThievesCS);
        return this.clocks.clone();
    }

}
