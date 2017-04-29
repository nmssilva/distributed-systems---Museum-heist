package serverSide;

import static auxiliary.constants.Heist.*;
import auxiliary.messages.Message;
import clientSide.ClientCom;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class AssaultParty {

    private int id;                         // ID of the assault party
    private int roomID;                     // ID of room assigned
    private int[] partyThieves;             // Elements assigned
    private int[] partyThievesPos;          // Position of the elements
    private int[] partyThievesMaxDisp;      // Maximum displacement of the elements
    private boolean[] myTurn;               // Turns of the elements
    private boolean[] inRoom;               // Elements in room
    private int nThievesRoom;               // Number of assault thieves in room
    private int reverse;                    // Counter to inform all elements are Ready to crawlOut()

    private String hostname;

    public AssaultParty(int id) {
        this.id = id;
        roomID = -1;
        partyThieves = new int[MAX_ASSAULT_PARTY_THIEVES];
        partyThievesPos = new int[MAX_ASSAULT_PARTY_THIEVES];
        partyThievesMaxDisp = new int[MAX_ASSAULT_PARTY_THIEVES];
        myTurn = new boolean[MAX_ASSAULT_PARTY_THIEVES];
        inRoom = new boolean[MAX_ASSAULT_PARTY_THIEVES];
        nThievesRoom = 0;
        reverse = 0;

        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Empty assault party
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            partyThieves[i] = -1;
            partyThievesPos[i] = -1;
            partyThievesMaxDisp[i] = -1;
            myTurn[i] = false;
            inRoom[i] = false;
        }

        logSetAp();

    }

    /**
     *
     * Simulates the movement crawlIn of the Assault Thief current thread.
     *
     *
     */
    public synchronized boolean crawlIn(int thiefID) {
        while (partyThievesPos[getIndexParty(thiefID)] != getDistOutsideRoom()) {
            while (!myTurn[getIndexParty(thiefID)]) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            int myIndex = getIndexParty(thiefID);
            int myPos = partyThievesPos[myIndex];
            int myAgility = partyThievesMaxDisp[myIndex];
            int[] assaultThievesPos = new int[MAX_ASSAULT_PARTY_THIEVES - 1];

            System.out.println("Thief: " + thiefID + " | Position: " + myPos + " | Disp: " + myAgility + " Party positions: " + Arrays.toString(partyThievesPos));

            int count = 0;
            int i = 0;
            for (i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
                if (i != myIndex) {
                    assaultThievesPos[count] = partyThievesPos[i];
                    count++;
                }
            }

            Arrays.sort(assaultThievesPos);

            // Predict maximum displacement
            for (i = myAgility; i > 0; i--) {
                boolean tooFarOrOcupada = false;
                // Array que vai ter myPos no inicio e assaultThievesPos de seguida
                int[] posAfterMove = new int[assaultThievesPos.length + 1];
                posAfterMove[0] = myPos + i;
                System.arraycopy(assaultThievesPos, 0, posAfterMove, 1, assaultThievesPos.length);
                Arrays.sort(posAfterMove);

                for (int j = 0; j < posAfterMove.length - 1; j++) {
                    if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != 0 && posAfterMove[j + 1] != getDistOutsideRoom()))) { //ultima condicao deve ser alterada
                        tooFarOrOcupada = true;
                        break;
                    }
                }

                // Set new position
                if ((!tooFarOrOcupada)) {
                    if (myPos + i >= getDistOutsideRoom()) {
                        partyThievesPos[myIndex] = getDistOutsideRoom();
                        nThievesRoom++;
                        inRoom[myIndex] = true;
                    } else {
                        partyThievesPos[myIndex] = myPos + i;
                    }

                    break;
                }
            }

            // Check if it is possible to move even further
            boolean canMoveAgain = false;
            if (!(myPos == partyThievesPos[myIndex] || inRoom[myIndex])) {
                for (i = partyThievesMaxDisp[myIndex]; i > 0; i--) {
                    boolean tooFarOrOcupada = false;
                    int[] posAfterMove = new int[assaultThievesPos.length + 1];
                    posAfterMove[0] = myPos + i;
                    System.arraycopy(assaultThievesPos, 0, posAfterMove, 1, assaultThievesPos.length);
                    Arrays.sort(posAfterMove);

                    for (int j = 0; j < posAfterMove.length - 1 && posAfterMove[j] != 0; j++) {
                        if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != 0 && posAfterMove[j + 1] != getDistOutsideRoom()) && !(nThievesRoom == MAX_ASSAULT_PARTY_THIEVES - 1))) { //ultima condicao deve ser alterada
                            tooFarOrOcupada = true;
                            break;
                        }
                    }

                    if ((!tooFarOrOcupada)) {
                        canMoveAgain = true;
                        break;
                    }
                }
                // Didn't get to room or can't walk further
            } else if (!canMoveAgain) {
                myTurn[myIndex] = false;

                int min = ROOM_MAX_DISTANCE;
                int minIndex = -1;

                for (int x = 0; x < MAX_ASSAULT_PARTY_THIEVES; x++) {
                    if (min >= partyThievesPos[x]) {
                        min = partyThievesPos[x];
                        minIndex = x;
                    }
                }

                if (minIndex == myIndex) {
                    boolean changed = true;
                    while (changed) {
                        changed = false;
                        for (int x = 0; x < MAX_ASSAULT_PARTY_THIEVES; x++) {

                            if (partyThievesPos[minIndex] + 1 == partyThievesPos[x] && partyThievesMaxDisp[minIndex] == 2) {
                                if (partyThievesPos[minIndex] + 1 == getDistOutsideRoom()) {
                                    continue;
                                }
                                minIndex = x;
                                changed = true;
                            }
                        }
                    }

                }
                myTurn[minIndex] = true;

                notifyAll();
            }

            System.out.println("Turns: " + Arrays.toString(myTurn));
        }
        
        logSetAp();

        return true;
    }

    /**
     *
     * Simulates the operation reverseDirection of the Assault Party. The
     * Assault Thief current thread blocks until the last element of the Assault
     * Party executes this action.
     *
     */
    public synchronized void reverseDirection(int thiefID) {
        myTurn[getIndexParty(thiefID)] = false;
        inRoom[getIndexParty(thiefID)] = false;

        if (nThievesRoom == MAX_ASSAULT_PARTY_THIEVES) {
            nThievesRoom = 0;
        }

        myTurn[0] = true;

        reverse++;
        while (reverse % MAX_ASSAULT_PARTY_THIEVES != 0 && reverse != 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        notifyAll();
    }

    /**
     *
     * Simulates the movement crawlOut of the Assault Thief current thread.
     *
     */
    public synchronized boolean crawlOut(int thiefID) {
        while (partyThievesPos[getIndexParty(thiefID)] != 0) {
            while (!myTurn[getIndexParty(thiefID)]) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            int myIndex = getIndexParty(thiefID);
            int myPos = partyThievesPos[myIndex];
            int myAgility = partyThievesMaxDisp[myIndex];
            int[] assaultThievesPos = new int[MAX_ASSAULT_PARTY_THIEVES - 1];

            System.out.println("Thief: " + thiefID + " | Position: " + myPos + " | Disp: " + myAgility + " Party positions: " + Arrays.toString(partyThievesPos));

            int count = 0;
            int i = 0;
            for (i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
                if (i != myIndex) {
                    assaultThievesPos[count] = partyThievesPos[i];
                    count++;
                }
            }

            Arrays.sort(assaultThievesPos);

            /// Predict maximum displacement
            for (i = myAgility; i > 0; i--) {
                boolean tooFarOrOcupada = false;
                // Array que vai ter myPos no inicio e assaultThievesPos de seguida
                int[] posAfterMove = new int[assaultThievesPos.length + 1];
                posAfterMove[0] = myPos - i;
                System.arraycopy(assaultThievesPos, 0, posAfterMove, 1, assaultThievesPos.length);
                Arrays.sort(posAfterMove);

                for (int j = 0; j < posAfterMove.length - 1; j++) {
                    if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != getDistOutsideRoom() && posAfterMove[j + 1] != 0) && !(nThievesRoom == MAX_ASSAULT_PARTY_THIEVES - 1))) {
                        tooFarOrOcupada = true;
                        break;
                    }
                }

                // Set new position
                if ((!tooFarOrOcupada)) {
                    if (myPos - i <= 0) {
                        partyThievesPos[myIndex] = 0;
                        nThievesRoom++;
                        inRoom[myIndex] = true;

                    } else {
                        partyThievesPos[myIndex] = myPos - i;
                    }

                    break;
                }
            }

            // Check if it is possible to move even further
            boolean canMoveAgain = false;
            if (!(myPos == partyThievesPos[myIndex] || inRoom[myIndex])) {
                for (i = partyThievesMaxDisp[myIndex]; i > 0; i--) {
                    boolean tooFarOrOcupada = false;
                    int[] posAfterMove = new int[assaultThievesPos.length + 1];
                    posAfterMove[0] = myPos - i;
                    System.arraycopy(assaultThievesPos, 0, posAfterMove, 1, assaultThievesPos.length);
                    Arrays.sort(posAfterMove);

                    for (int j = 0; j < posAfterMove.length - 1 && posAfterMove[j] != 0; j++) {
                        if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != getDistOutsideRoom() && posAfterMove[j + 1] != 0) && !(nThievesRoom == MAX_ASSAULT_PARTY_THIEVES - 1))) {
                            tooFarOrOcupada = true;
                            break;
                        }
                    }

                    if ((!tooFarOrOcupada)) {
                        canMoveAgain = true;
                        break;
                    }
                }
                // Didn't get to room or can't walk further
            } else if (!canMoveAgain) {
                myTurn[myIndex] = false;

                int max = 0;
                int maxIndex = -1;

                for (int x = 0; x < MAX_ASSAULT_PARTY_THIEVES; x++) {
                    if (max <= partyThievesPos[x]) {
                        max = partyThievesPos[x];
                        maxIndex = x;
                    }
                }

                if (maxIndex == myIndex) {
                    boolean changed = true;
                    while (changed) {
                        changed = false;
                        for (int x = 0; x < MAX_ASSAULT_PARTY_THIEVES; x++) {
                            if (partyThievesPos[maxIndex] - 1 == partyThievesPos[x] && partyThievesMaxDisp[maxIndex] == 2) {
                                if (partyThievesPos[maxIndex] - 1 == 0) {
                                    continue;
                                }
                                maxIndex = x;
                                changed = true;
                            }
                        }
                    }
                }

                myTurn[maxIndex] = true;

                notifyAll();
            }

            System.out.println("Turns: " + Arrays.toString(myTurn));
        }

        logSetAp();
        
        return true;
    }

    /**
     * Add an Assault Thief to the Assault Party.
     *
     * @param thiefID
     * @param thief
     * @return True, if the operation was successful or false if otherwise
     */
    public synchronized boolean addThief(int thiefID, int maxDisp) {
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (partyThieves[i] == -1) {
                partyThieves[i] = thiefID;
                partyThievesPos[i] = 0;
                partyThievesMaxDisp[i] = maxDisp;
                System.out.println("Added " + thiefID + " with disp " + maxDisp);
                return true;
            }
        }
        
        logSetAp();

        return false;
    }

    /**
     * Get index of current Assault Thief thread in the partyThieves array.
     *
     * @return Returns the index of the current Assault Thief thread in the
     * partyThieves array
     */
    public synchronized int getIndexParty(int thiefID) {

        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (partyThieves[i] == thiefID) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Set the turn to crawl of the element of index 0 to true.
     *
     */
    public void setFirst() {
        myTurn[0] = true;
    }

    /**
     * Set the ID of an Assault Thief in the current Assault Party.
     *
     * @param i
     * @param value
     */
    public void setPartyThieves(int i, int value) {
        partyThieves[i] = value;
    }

    /**
     * Get Assault Thieves ID of the current Assault Party.
     *
     * @return Returns array partyThieves ID of the current Assault Party
     */
    public int[] getPartyThieves() {
        return partyThieves;
    }

    /**
     * Get Assault Thieves positions of the current Assault Party.
     *
     * @return Returns array partyThieves ID of the current Assault Party
     */
    public int[] getPartyThievesPos() {
        return partyThievesPos;
    }

    public boolean isEmptyAP() {

        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (partyThieves[i] != -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get Assault Thieves maximum displacement of the current Assault Party.
     *
     * @return Returns array partyThieves ID of the current Assault Party
     */
    public int[] getPartyThievesMaxDisp() {
        return partyThievesMaxDisp;
    }

    /**
     *
     * Set a roomID to this Assault Party.
     *
     * @param roomID
     */
    public void setRoom(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Get the roomID assigned to the current Assault Party.
     *
     * @return Returns roomIDy
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * +
     * Get distance from the Outside to the Room assigned to this Assault Party
     *
     * @return Returns distance from the Outside to the Room assigned to this
     * Assault Party
     */
    public int getDistOutsideRoom() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(hostname, PORT_MUSEUM);
        if (!con.open()) {
            return -1;
        }
        outMessage = new Message(Message.GET_DIST_OUTSIDE, roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();
        return inMessage.getInteger();
    }

    private void logSetAp() {
        /* Comunicação ao servidor dos parâmetros do problema */
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(HOST_LOG, PORT_LOG);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.SETAP, this.id, this.partyThieves, this.partyThievesPos, this.roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();
    }
}
