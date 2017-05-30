package serverSide;

import static auxiliary.Heist.*;
import interfaces.APInterface;
import interfaces.CCSInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class ControlCollectionSite implements CCSInterface {

    private int[] partiesArrived;           // Needed to know which parties have arrived
    private boolean[] roomOcupied;          // Represents the status of each room (occupied or clear)
    private boolean[] emptyRooms;           // Represents the emptyness of each room (empty or still with paintings)
    private int nPaintings;                 // Total number of collected paintings
    private int nextRoom;                   // Next room to rob
    private int nextParty;                  // Next party to prepare

    // Flags for operations sendAssaultParty and prepareExcursion
    private int partyReady;
    private int good = 0;
    private boolean sentAssaultParty;

    // Flags for operations handACanvas, collectCanvas and takeARest
    private boolean rest;
    private boolean ready;
    private boolean collectCanvas;
    
    private APInterface ap[];

    public ControlCollectionSite(APInterface ap[]) {
        
        this.ap = ap;
        
        roomOcupied = new boolean[ROOMS_NUMBER];
        emptyRooms = new boolean[ROOMS_NUMBER];
        nPaintings = 0;
        sentAssaultParty = false;
        collectCanvas = false;
        partiesArrived = new int[MAX_ASSAULT_PARTY_THIEVES];
        rest = false;
        ready = false;
        nextRoom = -1;
        nextParty = -1;
        partyReady = 0;


        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            partiesArrived[i] = 0;
        }
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            emptyRooms[i] = false;
        }
    }

    /**
     *
     * Master Thief appraises the situation and returns the next operation to
     * execute. The Master Thief sets its status to DECIDING_WHAT_TO_DO before
     * returning the operation.
     *
     * @param nAssaultThievesCS Nuber of Assault Thieves in Concentration site
     * @return ID of the operation to execute.
     */
    @Override
    public synchronized int appraiseSit(int nAssaultThievesCS) {

        try {
            nextParty = nextEmptyParty();
        } catch (RemoteException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        nextRoom = nextEmptyRoom();

        ////System.out.println(nextParty + " - " + nextRoom + " - " + nAssaultThievesCS + " - " + nPaintings);
        if (nextParty == -1) {
            return 0;                                                // takeARest()
        }
        if (nAssaultThievesCS >= MAX_ASSAULT_PARTY_THIEVES) {
            if (nextRoom != -1) {
                ////System.out.println("prepareAssaultParty()");
                return 1;                                            // prepareAssaultParty()
            } else if (nAssaultThievesCS != THIEVES_NUMBER) {
                ////System.out.println("takeARest()");
                return 0;                                            // takeARest()
            } else {
                ////System.out.println("sumUpResults()");
                return 2;                                            // sumUpResults()
            }
        } else {
            return 0;                                                // takeARest()        
        }
    }

    /**
     *
     * Assault Thief current thread sets its partyID to the assigned Assault
     * Party. The thread wakes the Master Thief if it's the last element of the
     * Assault Party to execute this operation and blocks until the Master Thief
     * finalizes executing sendAssaultParty. The Assault Thief sets its status
     * to WAITING_FOR_SENT_ASSAULT_PARTY.
     */
    @Override
    public synchronized void prepareExcursion() {
        partyReady++;

        if (partyReady == MAX_ASSAULT_PARTY_THIEVES) {
            notifyAll();
        }

        while (!sentAssaultParty) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        good++;

        if (good == MAX_ASSAULT_PARTY_THIEVES) {
            sentAssaultParty = false;
            good = 0;
        }
    }

    /**
     * Master Thief sends a ready Assault Party. The Master Thief sets it's
     * status to DECIDING_WHAT_TO_DO.
     */
    @Override
    public synchronized void sendAssaultParty() {

        while (partyReady != MAX_ASSAULT_PARTY_THIEVES) {
            try {
                wait();

            } catch (InterruptedException e) {

            }
        }

        int roomToSend = nextEmptyRoom();
        roomOcupied[roomToSend] = true;
        sentAssaultParty = true;
        partyReady = 0;

        notifyAll();
    }

    /**
     * Master Thief blocks until an Assault Thief executes handACanvas and
     * AmINeeded. The Master Thief sets its status to WAITING_FOR_GROUP_ARRIVAL.
     */
    @Override
    public synchronized void takeARest() {
        rest = true;

        notifyAll();

        while (!collectCanvas) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        ready = false;
        collectCanvas = false;

        notifyAll();
    }

    /**
     * Get next Party available for assignment.
     *
     * @return nextParty
     */
    @Override
    public int getNextParty() {
        return nextParty;
    }

    /**
     * Discover next empty Assault Party.
     *
     * @return the ID of the Assault Party or -1 if there is no empty Assault.
     * Party
     */
    public synchronized int nextEmptyParty() throws RemoteException {
        
        if (this.ap[0].isEmptyAP() == true) {
            return 0;
        }
        
        if (this.ap[1].isEmptyAP() == true) {
            return 1;
        }

        return -1;
    }

    /**
     * Checks for an unoccupied and empty Room.
     *
     * @return Returns the first available Room ID to raid.
     */
    @Override
    public synchronized int nextEmptyRoom() {
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            if (!emptyRooms[i] && !roomOcupied[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Operation to wake Master Thief that the Assault Thief current Thread is
     * ready.
     *
     */
    @Override
    public synchronized void isReady() {
        ready = true;

        notifyAll();
    }

    /**
     * Assault thieves hands a canvas to the Master Thief or shows up empty
     * handed.
     *
     * 
     * @param thiefID ID of Assault Thief
     * @param partyID ID of Assault Thief Party
     * @param hasCanvas 1 if Assault Thief has canvas, 0 if otherwise
     * @return true if operation successful, false if otherwise
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized boolean handCanvas(int thiefID, int partyID, int hasCanvas) throws RemoteException {

        while (!rest) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        rest = false;

        int roomID = this.ap[partyID].getRoomID();

        if (hasCanvas == 0) {
            emptyRooms[roomID] = true;
        }
        if (hasCanvas == 1) {
            nPaintings++;
        }

        partiesArrived[partyID]++;
        if (partiesArrived[partyID] == MAX_ASSAULT_PARTY_THIEVES) {
            partiesArrived[partyID] = 0;
            roomOcupied[roomID] = false;
        }

        collectCanvas = true;

        // Reset Party
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {

            int[] pthieves = null;
            try {
                pthieves = this.ap[partyID].getPartyThieves();
            } catch (RemoteException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (pthieves[i] == thiefID) {
                
                try {
                    this.ap[partyID].setPartyThieves(i,-1);
                } catch (RemoteException ex) {
                    Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }

        notifyAll();

        return true;
    }

    /**
     * Master Thief collects a canvas. The Master Thief sets its status to
     * DECIDING_WHAT_TO_DO.
     *
     */
    @Override
    public synchronized void collectCanvas() {
        collectCanvas = false;

        notifyAll();

    }

    /**
     * Master Thief presents the final heist report. The Master Thief sets its
     * status to PRESENTING_THE_REPORT.
     */
    @Override
    public synchronized void sumUpResults() {
        ////System.out.println("Got " + nPaintings + " paintings!");
    }

    /**
     * Set the next Assault Party ID to be assigned.
     *
     * @param nextParty Value to be set
     */
    public void setNextParty(int nextParty) {
        this.nextParty = nextParty;
    }

    /**
     * Set the next Room ID to be assigned.
     *
     * @param nextRoom value to be set
     */
    public void setNextRoom(int nextRoom) {
        this.nextRoom = nextRoom;
    }

    /**
     * Get the ID of the next Room to be assigned.
     *
     * @return ID of the next Room to be assigned
     */
    @Override
    public int getNextRoom() {
        return nextRoom;
    }

    /**
     * Get the number of paitings collected to the moment.
     *
     * @return Returns the number of paitings collected to the moment.
     */
    public int getnPaintings() {
        return nPaintings;
    }
}
