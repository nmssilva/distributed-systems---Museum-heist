package serverSide;

import clientSide.AssaultThief;
import clientSide.MasterThief;
import interfaces.IConcentrationSite;
import static auxiliary.Heist.*;
import static auxiliary.States.*;
import auxiliary.MemFIFO;
import auxiliary.Message;
import static auxiliary.Message.*;
import clientSide.com.ClientCom;
import genclass.GenericIO;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class ConcentrationSite implements IConcentrationSite {

    private MemFIFO waitQueue;                     // Wainting queue for ready assault thieves               
    private int nAssaultThievesCS;                 // Number of assault thieves in the concentration site
    private int[][] parties;

    /**
     *
     */
    public ConcentrationSite() {
        waitQueue = new MemFIFO(THIEVES_NUMBER);
        nAssaultThievesCS = 0;
        parties = new int[MAX_ASSAULT_PARTIES][MAX_ASSAULT_PARTY_THIEVES];

        for (int i = 0; i < MAX_ASSAULT_PARTIES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                parties[i][j] = -1;
            }
        }
    }

    /**
     *
     * Master Thief blocks until the number of Assault Thieves in the
     * Concentration Site is equal to the number of total Assault Thieves of the
     * heist. The status of the Master Thief is changed to DECIDING_WHAT_TO_DO
     * in the end of the operation.
     *
     * @param mthief
     */
    @Override
    public synchronized void startOfOperations(MasterThief mthief) {

        while (nAssaultThievesCS != THIEVES_NUMBER) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        mthief.setStatus(DECIDING_WHAT_TO_DO);

    }

    /**
     * Resets the Assault Thief current thread, adds it's reference to the
     * waiting queue and blocks it until the Master Thief executes
     * prepareAssaultParty or the heist ends. The status of the Assault Thief
     * current thread is changed to OUTSIDE in the end of the operation.
     *
     * @param thief
     * @return
     */
    @Override
    public synchronized boolean amINeeded(AssaultThief thief) {

        // Reset thief
        thief.setPartyID(-1);
        thief.setHasCanvas(0);
        thief.setStatus(OUTSIDE);

        nAssaultThievesCS++;
        waitQueue.write(thief);

        //ccs.setReady();
        Message inMessage, outMessage;
        ClientCom con = null;
        try {
            con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_CCS);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!con.open()) {
            System.out.println("Client Con Not Open");
            System.exit(1);
        }
        outMessage = new Message(SET_READY);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("ERROR " + inMessage.toString());
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }

        notifyAll();

        boolean inParty;
        try {
            con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_CCS);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!con.open()) {
            System.out.println("Client Con Not Open");
            System.exit(1);
        }
        outMessage = new Message(IN_PARTY, thief);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK_BOOL) {
            GenericIO.writelnString("ERROR " + inMessage.toString());
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
        inParty = inMessage.getAckbool();

        // Blocks if thief is assigned to any party
        //while (!ccs.inParty()) {
        while (!inParty) {
            try {
                int nextEmptyRoom;

                try {
                    con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_CCS);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!con.open()) {
                    System.out.println("Client Con Not Open");
                    System.exit(1);
                }
                outMessage = new Message(NEXT_EMPTY_ROOM, thief);
                con.writeObject(outMessage);
                inMessage = (Message) con.readObject();
                if (inMessage.getType() != ACK_INT) {
                    GenericIO.writelnString("ERROR " + inMessage.toString());
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
                    System.exit(1);
                }
                nextEmptyRoom = inMessage.getValue();

                //if (ccs.nextEmptyRoom() == -1 && nAssaultThievesCS == THIEVES_NUMBER) {
                if (nextEmptyRoom == -1 && nAssaultThievesCS == THIEVES_NUMBER) {
                    return false;
                }
                wait();
            } catch (InterruptedException e) {

            }

            try {
                con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_CCS);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ConcentrationSite.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!con.open()) {
                System.out.println("Client Con Not Open");
                System.exit(1);
            }
            outMessage = new Message(IN_PARTY, thief);
            con.writeObject(outMessage);
            inMessage = (Message) con.readObject();
            if (inMessage.getType() != ACK_BOOL) {
                GenericIO.writelnString("ERROR " + inMessage.toString());
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
                System.exit(1);
            }
            inParty = inMessage.getAckbool();

        }

        //con.close();
        return true;
    }

    /**
     * Master Thief prepares and Assault Party. It adds elements to the Assault
     * Party and then assigns it a room and the turn of the element to first
     * crawl in. The status of the Master Thief is changed to ASSEMBLING_A_GROUP
     * in the end of the operation.
     */
    @Override
    public synchronized void prepareAssaultParty() {
        MasterThief mthief = (MasterThief) Thread.currentThread();

        mthief.setStatus(ASSEMBLING_A_GROUP);
        /*
        int partyID = ccs.getNextParty();
        
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            parties[partyID].addThief((AssaultThief) waitQueue.read());
            nAssaultThievesCS--;
        }

        parties[partyID].setRoom(ccs.getNextRoom());
        parties[partyID].setFirstToCrawl();
         */
        notifyAll();
    }

    /**
     * Get the number of Assault Thieves in Concentration Site.
     *
     * @return Number of Assault Thieves in Concentration Site
     */
    @Override
    public int getnAssaultThievesCS() {
        return nAssaultThievesCS;
    }
}
