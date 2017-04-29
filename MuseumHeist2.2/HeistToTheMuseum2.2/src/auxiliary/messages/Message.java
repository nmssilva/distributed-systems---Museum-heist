package auxiliary.messages;

import java.io.*;

/**
 *
 * This data type defines the exchanged messages between server and clients in a
 * solution of the Heist to the Museum that implements a type 2 client-server
 * model (with server replication) with static initialisation of thieves threads
 * The communication is based on the exchange of type Message objects in a TCP
 * channel.
 */

public class Message implements Serializable {

    private static final long serialVersionUID = 1001L;

    public static final int SETNFIC = 1;
    public static final int NFICDONE = 2;
    public static final int SETREP = 3;
    public static final int REPDONE = 4;
    public static final int STARTOP = 5;
    public static final int AMINEEDED = 6;
    public static final int GET_PTHIEVES = 7;
    public static final int ENDOP = 8;
    public static final int ACK = 9;
    public static final int GET_NEXT_ROOM = 10;
    public static final int PREPARE_AP = 11;
    public static final int SET_AP_ROOM = 12;
    public static final int ISREADY = 13;
    public static final int GET_ASSAULT_THIEVES_CS = 14;
    public static final int APPRAISE_SIT = 15;
    public static final int ADDTHIEF = 16;
    public static final int GET_NEXT_AP = 17;
    public static final int IS_EMPTY_AP = 18;
    public static final int SENDAP = 19;
    public static final int PREPAREEXCURSION = 20;
    public static final int GET_DIST_OUTSIDE = 21;
    public static final int CRAWL_IN = 22;
    public static final int SETFIRST = 23;
    public static final int GET_ROOM_ID = 24;
    public static final int REVERSE_DIRECTION = 25;
    public static final int CRAWL_OUT = 26;
    public static final int ROLL_A_CANVAS = 27;
    public static final int HAND_CANVAS = 28;
    public static final int SET_PTHIEVES = 29;
    public static final int TAKE_A_REST = 30;
    public static final int COLLECT_CANVAS = 31;
    public static final int SUM_UP_RESULTS = 32;
    public static final int NEXT_EMPTY_ROOM = 33;
    public static final int SETMUSEUM = 34;
    public static final int SETAP = 35;
    public static final int SETMTSTATUS = 36;
    public static final int SET_ASSAULT_THIEF = 37;

    private int msgType = -1;

    private int thiefID;

    private int status;

    private int maxDisp;

    private int hasCanvas;

    private boolean b;

    private int partyThieves[];

    private String fName = null;

    private int nIter = -1;

    private int value;
    private int value2;
    private int value3;

    private int[] intarray;
    private int[] intarray2;

    private int partyID;

    public Message(int type) {
        msgType = type;
    }

    public Message(int type, int integer) {
        msgType = type;
        this.value = integer;
    }

    public Message(int type, int integer, int integer2) {
        msgType = type;
        this.value = integer;
        this.value2 = integer2;
    }

    public Message(int type, int integer, int integer2, int integer3) {
        msgType = type;
        this.value = integer;
        this.value2 = integer2;
        this.value3 = integer3;
    }

    public Message(int type, int[] partyThieves) {
        msgType = type;
        this.partyThieves = partyThieves;
    }

    public Message(int type, String name, int nIter) {
        msgType = type;
        fName = name;
        this.nIter = nIter;
    }

    public Message(int type, boolean b) {
        msgType = type;
        this.b = b;
    }

    public Message(int type, int[] intarray, int[] intarray2) {
        this.msgType = type;
        this.intarray = intarray;
        this.intarray2 = intarray2;
    }

    public Message(int SETAP, int id, int[] partyThieves, int[] partyThievesPos, int roomID) {
        this.msgType = SETAP;
        this.partyID = id;
        this.partyThieves = partyThieves;
        this.intarray = partyThievesPos;
        this.value = roomID;
    }

    public Message(int type, int thiefID, int status, int maxDisp, int partyID, int hasCanvas) {
        msgType = type;
        this.thiefID = thiefID;
        this.status = status;
        this.maxDisp = maxDisp;
        this.partyID = partyID;
        this.hasCanvas = hasCanvas;
        //this.thief = (AssaultThief) Thread.currentThread();
    }

    public int getValue() {
        return value;
    }

    public int getValue2() {
        return value2;
    }

    public int getValue3() {
        return value3;
    }

    public int[] getPartyThieves() {
        return partyThieves;
    }

    public int getStatus() {
        return status;
    }

    public int getMaxDisp() {
        return maxDisp;
    }

    public int getHasCanvas() {
        return hasCanvas;
    }

    public int getPartyID() {
        return partyID;
    }

    public void setPartyID(int partyID) {
        this.partyID = partyID;
    }

    /**
     * Get value of message type field
     *
     * @return message type
     */
    public int getType() {
        return (msgType);
    }

    /**
     * Get value of thief identification field
     *
     * @return thief identification
     */
    public int getThiefID() {
        return thiefID;
    }

    /**
     * Get value of logging file name field
     *
     * @return file name
     */
    public String getFName() {
        return (fName);
    }

    public boolean isB() {
        return b;
    }

    public int[] getIntarray() {
        return intarray;
    }

    public int[] getIntarray2() {
        return intarray2;
    }

    /**
     * Get value of thieves lifecycle iterations number field
     *
     * @return thieves lifecycle iterations number
     */
    public int getNIter() {
        return (nIter);
    }

    /**
     * Internal fields print. Used for debugging.
     *
     * @return string containing, in separate lines, the concatenatino of the
     * field identification and its value
     */
    @Override
    public String toString() {
        return ("Type = " + msgType
                + "\nThief Id = " + thiefID);
    }

}
