package serverSide;

import auxiliary.Heist;
import static auxiliary.Heist.*;
import auxiliary.VectorTimestamp;
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

    private VectorTimestamp clocks;

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

        this.clocks = new VectorTimestamp(Heist.THIEVES_NUMBER+1, 0);

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
     * @param vt VectorTimestamp
     * @return ID of the operation to execute.
     */
    @Override
    public synchronized VectorTimestamp appraiseSit(int nAssaultThievesCS, VectorTimestamp vt) {
        this.clocks.update(vt);
        try {
            nextParty = nextEmptyParty(this.clocks.clone()).getInteger();
        } catch (RemoteException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        nextRoom = nextEmptyRoom(this.clocks.clone()).getInteger();

        ////System.out.println(nextParty + " - " + nextRoom + " - " + nAssaultThievesCS + " - " + nPaintings);
        if (nextParty == -1) {
            this.clocks.setInteger(0);
            return this.clocks.clone();                                                // takeARest()
        }
        if (nAssaultThievesCS >= MAX_ASSAULT_PARTY_THIEVES) {
            if (nextRoom != -1) {
                ////System.out.println("prepareAssaultParty()");
                this.clocks.setInteger(1);
                return this.clocks.clone();                                           // prepareAssaultParty()
            } else if (nAssaultThievesCS != THIEVES_NUMBER) {
                ////System.out.println("takeARest()");
                this.clocks.setInteger(0);
                return this.clocks.clone();                                            // takeARest()
            } else {
                ////System.out.println("sumUpResults()");
                this.clocks.setInteger(2);
                return this.clocks.clone();                                            // sumUpResults()
            }
        } else {
            this.clocks.setInteger(0);
            return this.clocks.clone();                                                // takeARest()        
        }
    }

    /**
     *
     * Assault Thief current thread sets its partyID to the assigned Assault
     * Party. The thread wakes the Master Thief if it's the last element of the
     * Assault Party to execute this operation and blocks until the Master Thief
     * finalizes executing sendAssaultParty. The Assault Thief sets its status
     * to WAITING_FOR_SENT_ASSAULT_PARTY.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp prepareExcursion(VectorTimestamp vt) {

        this.clocks.update(vt);

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

        return this.clocks.clone();

    }

    /**
     * Master Thief sends a ready Assault Party. The Master Thief sets it's
     * status to DECIDING_WHAT_TO_DO.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp sendAssaultParty(VectorTimestamp vt) {

        this.clocks.update(vt);

        while (partyReady != MAX_ASSAULT_PARTY_THIEVES) {
            try {
                wait();

            } catch (InterruptedException e) {

            }
        }

        int roomToSend = nextEmptyRoom(this.clocks.clone()).getInteger();
        roomOcupied[roomToSend] = true;
        sentAssaultParty = true;
        partyReady = 0;

        notifyAll();

        return this.clocks.clone();
    }

    /**
     * Master Thief blocks until an Assault Thief executes handACanvas and
     * AmINeeded. The Master Thief sets its status to WAITING_FOR_GROUP_ARRIVAL.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp takeARest(VectorTimestamp vt) {

        this.clocks.update(vt);

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

        return this.clocks.clone();
    }

    /**
     * Get next Party available for assignment.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp with nextParty
     */
    @Override
    public VectorTimestamp getNextParty(VectorTimestamp vt) {
        this.clocks.update(vt);
        this.clocks.setInteger(nextParty);
        return this.clocks.clone();
    }

    /**
     * Discover next empty Assault Party.
     *
     * @param vt VectorTimestamp
     * @return the ID of the Assault Party or -1 if there is no empty Assault.
     * Party
     * @throws java.rmi.RemoteException
     */
    public synchronized VectorTimestamp nextEmptyParty(VectorTimestamp vt) throws RemoteException {
        this.clocks.update(vt);
        if (this.ap[0].isEmptyAP(this.clocks.clone()).getBool() == true) {
            this.clocks.setInteger(0);
            return this.clocks.clone();
        }

        if (this.ap[1].isEmptyAP(this.clocks.clone()).getBool() == true) {
            this.clocks.setInteger(1);
            return this.clocks.clone();
        }

        this.clocks.setInteger(-1);
        return this.clocks.clone();
    }

    /**
     * Checks for an unoccupied and empty Room.
     *
     * @param vt VectorTimestamp
     * @return Returns the first available Room ID to raid.
     */
    @Override
    public synchronized VectorTimestamp nextEmptyRoom(VectorTimestamp vt) {
        this.clocks.update(vt);
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            if (!emptyRooms[i] && !roomOcupied[i]) {
                this.clocks.setInteger(i);
                return this.clocks.clone();
            }
        }

        this.clocks.setInteger(-1);
        return this.clocks.clone();
    }

    /**
     * Operation to wake Master Thief that the Assault Thief current Thread is
     * ready.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp isReady(VectorTimestamp vt) {
        this.clocks.update(vt);
        ready = true;

        notifyAll();
        return this.clocks.clone();
    }

    /**
     * Assault thieves hands a canvas to the Master Thief or shows up empty
     * handed.
     *
     *
     * @param thiefID ID of Assault Thief
     * @param partyID ID of Assault Thief Party
     * @param hasCanvas 1 if Assault Thief has canvas, 0 if otherwise
     * @param vt VectorTimestamp
     * @return true if operation successful, false if otherwise
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized VectorTimestamp handCanvas(int thiefID, int partyID, int hasCanvas, VectorTimestamp vt) throws RemoteException {

        this.clocks.update(vt);
        
        while (!rest) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        rest = false;

        int roomID = this.ap[partyID].getRoomID(this.clocks.clone()).getInteger();

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
                pthieves = this.ap[partyID].getPartyThieves(this.clocks.clone()).getArray();
            } catch (RemoteException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (pthieves[i] == thiefID) {

                try {
                    this.ap[partyID].setPartyThieves(i, -1, this.clocks.clone());
                } catch (RemoteException ex) {
                    Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        notifyAll();

        this.clocks.setBool(true);
        return this.clocks.clone();
    }

    /**
     * Master Thief collects a canvas. The Master Thief sets its status to
     * DECIDING_WHAT_TO_DO.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp collectCanvas(VectorTimestamp vt) {
        
        this.clocks.update(vt);
        collectCanvas = false;

        notifyAll();
        return this.clocks.clone();

    }

    /**
     * Master Thief presents the final heist report. The Master Thief sets its
     * status to PRESENTING_THE_REPORT.
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp sumUpResults(VectorTimestamp vt) {
        this.clocks.update(vt);
        return this.clocks.clone();
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
     * @param vt VectorTimestamp
     * @return ID of the next Room to be assigned
     */
    @Override
    public VectorTimestamp getNextRoom(VectorTimestamp vt) {
        this.clocks.update(vt);
        this.clocks.setInteger(nextRoom);
        return this.clocks.clone();
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
