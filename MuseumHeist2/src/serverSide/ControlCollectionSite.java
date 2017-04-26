package serverSide;

import clientSide.AssaultThief;
import clientSide.MasterThief;
import interfaces.IControlCollectionSite;
import static auxiliary.Heist.*;
import auxiliary.Message;
import static auxiliary.Message.ACK;
import static auxiliary.Message.STARTOP;
import static auxiliary.States.*;
import clientSide.com.ClientCom;
import genclass.GenericIO;
import interfaces.IAssaultParty;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class ControlCollectionSite implements IControlCollectionSite {

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

    /**
     *
     */
    public ControlCollectionSite() {
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
     * @param nAssaultThievesCS numero de thieves na concentration site
     * @param mt MasterThief
     * @return ID of the operation to execute.
     */
    @Override
    public synchronized int appraiseSit(int nAssaultThievesCS, MasterThief mt) {

        nextParty = nextEmptyParty();
        nextRoom = nextEmptyRoom();

        if (nextParty == -1) {
            return 0; // takeARest()
        }

        mt.setStatus(DECIDING_WHAT_TO_DO);

        if (nAssaultThievesCS >= MAX_ASSAULT_PARTY_THIEVES) {
            if (nextRoom != -1) {
                return 1;   // prepareAssaultParty()
            } else if (nAssaultThievesCS != THIEVES_NUMBER) {
                return 0;   // takeARest()
            } else {
                return 2;   // sumUpResults()
            }
        } else {
            return 0;       // takeARest()        
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
    public synchronized void prepareExcursion(AssaultThief thief) {

        thief.setPartyID(nextParty);
        thief.setStatus(WAITING_SEND_ASSAULT_PARTY);

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
        MasterThief mthief = (MasterThief) Thread.currentThread();

        mthief.setStatus(DECIDING_WHAT_TO_DO);

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
        MasterThief mthief = (MasterThief) Thread.currentThread();

        mthief.setStatus(WAITING_FOR_GROUP_ARRIVAL);

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
    @Override
    public synchronized int nextEmptyParty() {
        
        int[] getpartythieves;
        
        for (int i = 0; i < MAX_ASSAULT_PARTIES; i++) {
            int count = 0;
            
            Message inMessage, outMessage;
            ClientCom con = null;
            try {
                con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_AP + i);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!con.open()) {
                return -1;
            }
            outMessage = new Message(Message.GET_PARTY_THIEVES);
            con.writeObject(outMessage);

            inMessage = (Message) con.readObject();
            if (inMessage.getType() != Message.ACK_INT_ARRAY) {
                GenericIO.writelnString(inMessage.toString());
                GenericIO.writelnString("Class: " + this.getClass().getName());
                GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
                System.exit(1);
            }
            getpartythieves = inMessage.getAckintarray();
            
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                
                if(getpartythieves[j] == -1){
                    count++;
                }
                
                /*if (parties[i].getPartyThieves()[j] == -1) {
                    count++;
                }*/
                if (count == MAX_ASSAULT_PARTY_THIEVES) {
                    return i;
                }
            }
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
    public synchronized void setReady() {
        ready = true;

        notifyAll();
    }

    /**
     * Thief hands a canvas if he has canvas.
     *
     */
    @Override
    public synchronized void handCanvas() {
        AssaultThief thief = ((AssaultThief) Thread.currentThread());

        thief.setStatus(OUTSIDE);

        while (!rest) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        rest = false;
        //int roomID = parties[thief.getPartyID()].getRoomID();

        if (thief.getHasCanvas() == 0) {
            //emptyRooms[roomID] = true;
        }
        if (thief.getHasCanvas() == 1) {
            nPaintings++;
        }

        partiesArrived[thief.getPartyID()]++;
        if (partiesArrived[thief.getPartyID()] == MAX_ASSAULT_PARTY_THIEVES) {
            partiesArrived[thief.getPartyID()] = 0;
            //roomOcupied[roomID] = false;
        }

        collectCanvas = true;

        // Reset Party
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {

            /*Message inMessage, outMessage;
            ClientCom con = null;
            try {
                con = new ClientCom(InetAddress.getLocalHost().getHostAddress(), PORT_CS);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!con.open()) {
                return;
            }
            outMessage = new Message(STARTOP);
            con.writeObject(outMessage);

            inMessage = (Message) con.readObject();
            if (inMessage.getType() != ACK) {
                GenericIO.writelnString(inMessage.toString());
                GenericIO.writelnString("Class: " + this.getClass().getName());
                GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
                System.exit(1);
            }*/

 /*if (parties[thief.getPartyID()].getPartyThieves()[i] == thief.getThiefID()) {
                parties[thief.getPartyID()].setPartyThieves(i, -1);
            }*/
        }

        notifyAll();
    }

    /**
     * Checks if the Assault Thief current thread is in the Assault Party.
     *
     * @return True, if the Assault Thief current thread is in the Assault Party
     * or false if otherwise.
     */
    @Override
    public synchronized boolean inParty(AssaultThief thief) {

        int[] getpartythieves;
        for (int i = 0; i < MAX_ASSAULT_PARTIES; i++) {

            Message inMessage, outMessage;
            ClientCom con = null;
            try {
                con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_AP + i);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!con.open()) {
                return false;
            }
            outMessage = new Message(Message.GET_PARTY_THIEVES);
            con.writeObject(outMessage);

            inMessage = (Message) con.readObject();
            if (inMessage.getType() != Message.ACK_INT_ARRAY) {
                GenericIO.writelnString(inMessage.toString());
                GenericIO.writelnString("Class: " + this.getClass().getName());
                GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
                System.exit(1);
            }
            getpartythieves = inMessage.getAckintarray();

            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                /*if (parties[i].getPartyThieves()[j] == thief.getThiefID()) {
                    return true;
                }*/

                if (getpartythieves[j] == thief.getThiefID()) {
                    return true;
                }
            }
        }

        return thief.getPartyID() != -1;
    }

    /**
     * Master Thief collects a canvas. The Master Thief sets its status to
     * DECIDING_WHAT_TO_DO.
     *
     */
    @Override
    public synchronized void collectCanvas() {
        MasterThief mthief = (MasterThief) Thread.currentThread();

        mthief.setStatus(DECIDING_WHAT_TO_DO);

        collectCanvas = false;

        notifyAll();

    }

    /**
     * Master Thief presents the final heist report. The Master Thief sets its
     * status to PRESENTING_THE_REPORT.
     */
    @Override
    public synchronized void sumUpResults() {
        MasterThief mthief = (MasterThief) Thread.currentThread();

        mthief.setStatus(PRESENTING_THE_REPORT);
    }

    /**
     * Set the next Assault Party ID to be assigned.
     *
     * @param nextParty
     */
    @Override
    public void setNextParty(int nextParty) {
        this.nextParty = nextParty;
    }

    /**
     * Set the next Room ID to be assigned.
     *
     * @param nextRoom
     */
    @Override
    public void setNextRoom(int nextRoom) {
        this.nextRoom = nextRoom;
    }

    /**
     * Get the ID of the next Room to be assigned.
     *
     * @return
     */
    @Override
    public int getNextRoom() {
        return nextRoom;
    }

    /**
     * Get the number of paintings collected to the moment.
     *
     * @return Returns the number of paintings collected to the moment.
     */
    @Override
    public int getnPaintings() {
        return nPaintings;
    }
}
