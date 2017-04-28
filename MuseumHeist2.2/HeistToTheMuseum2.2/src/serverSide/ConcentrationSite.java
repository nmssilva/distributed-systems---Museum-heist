package serverSide;

import static auxiliary.constants.Heist.*;
import auxiliary.memFIFO.MemFIFO;
import auxiliary.messages.Message;
import clientSide.ClientCom;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class ConcentrationSite {

    private MemFIFO waitQueue;                     // Wainting queue for ready assault thieves 
    private MemFIFO waitQueueDisp;                     // Wainting queue for ready assault thieves      
    private int nAssaultThievesCS;                 // Number of assault thieves in the concentration site
    private int partyID;
    private String hostname;

    public ConcentrationSite() {
        waitQueue = new MemFIFO(THIEVES_NUMBER);
        waitQueueDisp = new MemFIFO(THIEVES_NUMBER);
        nAssaultThievesCS = 0;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            java.util.logging.Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Master Thief blocks until the number of Assault Thieves in the
     * Concentration Site is equal to the number of total Assault Thieves of the
     * heist. The status of the Master Thief is changed to DECIDING_WHAT_TO_DO
     * in the end of the operation.
     *
     */
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
     * @return
     */
    public synchronized int amINeeded(int thiefID, int maxDisp) {
        // Reset thief

        nAssaultThievesCS++;
        waitQueue.write(thiefID);
        waitQueueDisp.write(maxDisp);

        Message inMessage, outMessage;
        ClientCom con = new ClientCom(hostname, PORT_CCS);
        if (!con.open()) {
            return -1;
        }
        outMessage = new Message(Message.ISREADY);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();

        notifyAll();

        while (!inParty(thiefID)) {
            try {
                con = new ClientCom(hostname, PORT_CCS);
                if (!con.open()) {
                    return -1;
                }
                outMessage = new Message(Message.NEXT_EMPTY_ROOM);
                con.writeObject(outMessage);
                inMessage = (Message) con.readObject();
                con.close();

                if (inMessage.getInteger() == -1 && nAssaultThievesCS == THIEVES_NUMBER) {
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
     * @return True, if the Assault Thief current thread is in the Assault Party
     * or false if otherwise.
     */
    public synchronized boolean inParty(int thiefID) {
        for (int i = 0; i < MAX_ASSAULT_PARTIES; i++) {
            Message inMessage, outMessage;
            ClientCom con = new ClientCom(hostname, PORT_AP + i);
            if (!con.open()) {
                return false;
            }
            outMessage = new Message(Message.GET_PTHIEVES);
            con.writeObject(outMessage);
            inMessage = (Message) con.readObject();
            con.close();

            int[] pthieves = inMessage.getPartyThieves();
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
    public synchronized boolean prepareAssaultParty() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(hostname, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(Message.GET_NEXT_AP);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();

        partyID = inMessage.getInteger();
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            con = new ClientCom(hostname, PORT_AP + partyID);
            if (!con.open()) {
                return false;
            }
            outMessage = new Message(Message.ADDTHIEF, (int) waitQueue.read(), (int) waitQueueDisp.read());
            con.writeObject(outMessage);
            inMessage = (Message) con.readObject();
            con.close();

            nAssaultThievesCS--;
        }

        con = new ClientCom(hostname, PORT_AP + partyID);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(Message.SETFIRST);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();

        notifyAll();
        con = new ClientCom(hostname, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(Message.GET_NEXT_ROOM);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();

        int roomID = inMessage.getInteger();

        con = new ClientCom(hostname, PORT_AP + partyID);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(Message.SET_AP_ROOM);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();

        return true;
    }

    /**
     * Get the number of Assault Thieves in Concentration Site.
     *
     * @return Number of Assault Thieves in Concentration Site
     */
    public int getnAssaultThievesCS() {
        return nAssaultThievesCS;
    }
}
