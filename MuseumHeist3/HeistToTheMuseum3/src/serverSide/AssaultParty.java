package serverSide;

import auxiliary.Heist;
import static auxiliary.Heist.*;
import auxiliary.VectorTimestamp;
import interfaces.APInterface;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class AssaultParty implements APInterface {

    private int id;                         // ID of the assault party
    private int roomID;                     // ID of room assigned
    private int[] partyThieves;             // Elements assigned
    private int[] partyThievesPos;          // Position of the elements
    private int[] partyThievesMaxDisp;      // Maximum displacement of the elements
    private boolean[] myTurn;               // Turns of the elements
    private boolean[] inRoom;               // Elements in room
    private int nThievesRoom;               // Number of assault thieves in room
    private int reverse;                    // Counter to inform all elements are Ready to crawlOut()

    private MuseumInterface museum;
    private LoggerInterface log;

    private VectorTimestamp clocks;

    public AssaultParty(int id, MuseumInterface museum, LoggerInterface log) throws RemoteException {

        this.museum = museum;
        this.log = log;

        this.id = id;
        roomID = -1;
        partyThieves = new int[MAX_ASSAULT_PARTY_THIEVES];
        partyThievesPos = new int[MAX_ASSAULT_PARTY_THIEVES];
        partyThievesMaxDisp = new int[MAX_ASSAULT_PARTY_THIEVES];
        myTurn = new boolean[MAX_ASSAULT_PARTY_THIEVES];
        inRoom = new boolean[MAX_ASSAULT_PARTY_THIEVES];
        nThievesRoom = 0;
        reverse = 0;

        this.clocks = new VectorTimestamp(Heist.THIEVES_NUMBER + 1, 0);

        // Empty assault party
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            partyThieves[i] = -1;
            partyThievesPos[i] = -1;
            partyThievesMaxDisp[i] = -1;
            myTurn[i] = false;
            inRoom[i] = false;
        }

        logSetAp(this.clocks.clone());

    }

    /**
     *
     * Simulates the movement crawlIn of the Assault Thief current thread.
     *
     * @param thiefID ID of Assault Thief
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp crawlIn(int thiefID, VectorTimestamp vt) {
        this.clocks.update(vt);
        try {
            while (partyThievesPos[getIndexParty(thiefID)] != getDistOutsideRoom(this.clocks.clone()).getInteger()) {
                while (!myTurn[getIndexParty(thiefID)]) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        //System.out.println(ex.getMessage());
                    }
                }

                int myIndex = getIndexParty(thiefID);
                int myPos = partyThievesPos[myIndex];
                int myAgility = partyThievesMaxDisp[myIndex];
                int[] assaultThievesPos = new int[MAX_ASSAULT_PARTY_THIEVES - 1];

                ////System.out.println("Thief: " + thiefID + " | Position: " + myPos + " | Disp: " + myAgility + " Party positions: " + Arrays.toString(partyThievesPos));
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
                    boolean BadToGo = false;
                    // Array que vai ter myPos no inicio e assaultThievesPos de seguida
                    int[] posAfterMove = new int[assaultThievesPos.length + 1];
                    posAfterMove[0] = myPos + i;
                    System.arraycopy(assaultThievesPos, 0, posAfterMove, 1, assaultThievesPos.length);
                    Arrays.sort(posAfterMove);

                    for (int j = 0; j < posAfterMove.length - 1; j++) {
                        if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != 0 && posAfterMove[j + 1] != getDistOutsideRoom(this.clocks.clone()).getInteger()))) { //ultima condicao deve ser alterada
                            BadToGo = true;
                            break;
                        }
                    }

                    // Set new position
                    if ((!BadToGo)) {
                        if (myPos + i >= getDistOutsideRoom(this.clocks.clone()).getInteger()) {
                            partyThievesPos[myIndex] = getDistOutsideRoom(this.clocks.clone()).getInteger();
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
                        boolean BadToGo = false;
                        int[] posAfterMove = new int[assaultThievesPos.length + 1];
                        posAfterMove[0] = myPos + i;
                        System.arraycopy(assaultThievesPos, 0, posAfterMove, 1, assaultThievesPos.length);
                        Arrays.sort(posAfterMove);

                        for (int j = 0; j < posAfterMove.length - 1 && posAfterMove[j] != 0; j++) {
                            if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != 0 && posAfterMove[j + 1] != getDistOutsideRoom(this.clocks.clone()).getInteger()) && !(nThievesRoom == MAX_ASSAULT_PARTY_THIEVES - 1))) { //ultima condicao deve ser alterada
                                BadToGo = true;
                                break;
                            }
                        }

                        if ((!BadToGo)) {
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
                                    if (partyThievesPos[minIndex] + 1 == getDistOutsideRoom(this.clocks.clone()).getInteger()) {
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

                ////System.out.println("Turns: " + Arrays.toString(myTurn));
            }
        } catch (RemoteException ex) {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            logSetAp(this.clocks.clone());
        } catch (RemoteException ex) {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.clocks.clone();

    }

    /**
     *
     * Simulates the operation reverseDirection of the Assault Party. The
     * Assault Thief current thread blocks until the last element of the Assault
     * Party executes this action.
     *
     * @param thiefID Id of Assault Thief
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp reverseDirection(int thiefID, VectorTimestamp vt) {

        this.clocks.update(vt);

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
                //System.out.println(ex.getMessage());
            }
        }

        notifyAll();

        return this.clocks.clone();
    }

    /**
     *
     * Simulates the movement crawlOut of the Assault Thief current thread.
     *
     * @param thiefID ID of Assault Thief
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public synchronized VectorTimestamp crawlOut(int thiefID, VectorTimestamp vt) {
        this.clocks.update(vt);
        while (partyThievesPos[getIndexParty(thiefID)] != 0) {
            while (!myTurn[getIndexParty(thiefID)]) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    //System.out.println(ex.getMessage());
                }
            }

            int myIndex = getIndexParty(thiefID);
            int myPos = partyThievesPos[myIndex];
            int myAgility = partyThievesMaxDisp[myIndex];
            int[] assaultThievesPos = new int[MAX_ASSAULT_PARTY_THIEVES - 1];

            //System.out.println("Thief: " + thiefID + " | Position: " + myPos + " | Disp: " + myAgility + " Party positions: " + Arrays.toString(partyThievesPos));
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
                    try {
                        if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != getDistOutsideRoom(this.clocks.clone()).getInteger() && posAfterMove[j + 1] != 0) && !(nThievesRoom == MAX_ASSAULT_PARTY_THIEVES - 1))) {
                            tooFarOrOcupada = true;
                            break;
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
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
                        try {
                            if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) || (posAfterMove[j + 1] - posAfterMove[j] == 0 && (posAfterMove[j + 1] != getDistOutsideRoom(this.clocks.clone()).getInteger() && posAfterMove[j + 1] != 0) && !(nThievesRoom == MAX_ASSAULT_PARTY_THIEVES - 1))) {
                                tooFarOrOcupada = true;
                                break;
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
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

            ////System.out.println("Turns: " + Arrays.toString(myTurn));
        }

        try {
            logSetAp(this.clocks.clone());
        } catch (RemoteException ex) {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.clocks.clone();
    }

    /**
     * Add an Assault Thief to the Assault Party.
     *
     * @param thiefID ID of Assault Thief
     * @param maxDisp Maximum Displacement of Assault Thief
     * @param vt VectorTimestamp
     * @return True, if the operation was successful or false if otherwise
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized VectorTimestamp addThief(int thiefID, int maxDisp, VectorTimestamp vt) throws RemoteException {
        this.clocks.update(vt);
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (partyThieves[i] == -1) {
                partyThieves[i] = thiefID;
                partyThievesPos[i] = 0;
                partyThievesMaxDisp[i] = maxDisp;
                ////System.out.println("Added " + thiefID + " with disp " + maxDisp);
                this.clocks.setBool(true);
                return this.clocks.clone();
            }
        }

        logSetAp(this.clocks.clone());

        this.clocks.setBool(false);
        return this.clocks.clone();
    }

    /**
     * Get index of current Assault Thief thread in the partyThieves array.
     *
     * @param thiefID ID of Assault Thief
     * @return Returns the index of the current Assault Thief thread in the
     * partyThieves array
     */
    public synchronized int getIndexParty(int thiefID) {

        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (partyThieves[i] == thiefID) {
                return i;
            }
        }

        return thiefID;
    }

    /**
     * Set the turn to crawl of the element of index 0 to true.
     *
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public VectorTimestamp setFirst(VectorTimestamp vt) {
        this.clocks.update(vt);
        myTurn[0] = true;
        return this.clocks.clone();
    }

    /**
     * Set the ID of an Assault Thief in the current Assault Party.
     *
     * @param i Index
     * @param value Value to set
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public VectorTimestamp setPartyThieves(int i, int value, VectorTimestamp vt) {
        this.clocks.update(vt);
        partyThieves[i] = value;
        return this.clocks.clone();
    }

    /**
     * Get Assault Thieves ID of the current Assault Party.
     *
     * @param vt VectorTimestamp
     * @return Returns array partyThieves ID of the current Assault Party
     */
    @Override
    public VectorTimestamp getPartyThieves(VectorTimestamp vt) {
        this.clocks.update(vt);
        this.clocks.setArray(partyThieves);
        return this.clocks.clone();
    }

    /**
     * Get Assault Thieves positions of the current Assault Party.
     *
     * @return Returns array partyThieves ID of the current Assault Party
     */
    public int[] getPartyThievesPos() {
        return partyThievesPos;
    }

    /**
     *
     * @param vt
     * @return
     */
    @Override
    public VectorTimestamp isEmptyAP(VectorTimestamp vt) {

        this.clocks.update(vt);
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (partyThieves[i] != -1) {
                this.clocks.setBool(false);
                return this.clocks.clone();
            }
        }

        this.clocks.setBool(true);
        return this.clocks.clone();
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
     * @param roomID ID of Room
     * @param vt VectorTimestamp
     * @return VectorTimestamp
     */
    @Override
    public VectorTimestamp setRoom(int roomID, VectorTimestamp vt) {
        this.clocks.update(vt);
        this.roomID = roomID;
        this.clocks.setInteger(roomID);
        return this.clocks.clone();
    }

    /**
     * Get the roomID assigned to the current Assault Party.
     *
     * @param vt VectorTimestamp
     * @return Returns roomIDy
     */
    @Override
    public VectorTimestamp getRoomID(VectorTimestamp vt) {
        this.clocks.update(vt);
        this.clocks.setInteger(this.roomID);
        return this.clocks.clone();
    }

    /**
     * +
     * Get distance from the Outside to the Room assigned to this Assault Party
     *
     * @return Returns distance from the Outside to the Room assigned to this
     * Assault Party
     */
    public VectorTimestamp getDistOutsideRoom(VectorTimestamp vt) throws RemoteException {
        this.clocks.update(vt);
        this.clocks.setInteger(this.museum.getDistRoom(this.roomID, this.clocks.clone()).getInteger());
        return this.clocks.clone();
    }

    private VectorTimestamp logSetAp(VectorTimestamp vt) throws RemoteException {
        this.clocks.update(vt);
        this.log.setAssaultParty(this.id, this.partyThieves, this.partyThievesPos, this.roomID, this.clocks.clone());
        return this.clocks.clone();
    }
}
