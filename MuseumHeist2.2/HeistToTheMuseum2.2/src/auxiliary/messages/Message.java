package auxiliary.messages;

import java.io.*;

/**
 *
 * This data type defines the exchanged messages between server and clients in a
 * solution of the Heist to the Museum that implements a type 2 client-server
 * model (with server replication) with static "lancamento" of thieves threads
 * The communication is based on the exchange of type Message objectes in a TCP
 * channel.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1001L;

    public static final int SETNFIC = 1;
    public static final int NFICDONE = 2;
    public static final int SETREP = 3;
    public static final int REPDONE = 4;
    public static final int STARTOP = 5;
    public static final int STARTOPDONE = 6;
    public static final int AMINEEDED = 7;
    public static final int NEEDED = 8;
    public static final int N_NEEDED = 9;
    public static final int IS_INPARTY = 10;
    public static final int INPARTY = 11;
    public static final int N_INPARTY = 12;
    public static final int GET_PTHIEVES = 13;
    public static final int GET_PTHIEVES_DONE = 14;
    public static final int ENDOP = 15;
    public static final int ACK = 16;
    public static final int GET_NEXT_ROOM = 17;
    public static final int PREPARE_AP = 18;
    public static final int SET_AP_ROOM = 19;
    public static final int ISREADY = 20;
    public static final int GET_ASSAULT_THIEVES_CS = 21;
    public static final int APPRAISE_SIT = 22;
    public static final int ADDTHIEF = 23;
    public static final int GET_NEXT_AP = 24;
    public static final int IS_EMPTY_AP = 25;
    public static final int SENDAP = 26;
    public static final int PREPAREEXCURSION = 27;
    public static final int GET_DIST_OUTSIDE = 28;
    public static final int CRAWL_IN = 29;
    public static final int SETFIRST = 30;
    public static final int GET_ROOM_ID = 31;
    public static final int REVERSE_DIRECTION = 32;
    public static final int CRAWL_OUT = 33;
    public static final int ROLL_A_CANVAS = 34;
    public static final int HAND_CANVAS = 35;
    public static final int SET_PTHIEVES = 36;
    public static final int TAKE_A_REST = 37;
    public static final int COLLECT_CANVAS = 38;
    public static final int SUM_UP_RESULTS = 39;
    public static final int NEXT_EMPTY_ROOM = 40;
    public static final int SETMUSEUM = 41;
    public static final int SETAP = 42;
    public static final int SETMTSTATUS = 43;
    public static final int SET_ASSAULT_THIEF = 44;

    private int msgType = -1;

    private int thiefID;

    private int status;

    private int maxDisp;

    private int hasCanvas;

    private boolean b;

    private int partyThieves[];

    private String fName = null;

    private int nIter = -1;

    private int integer;
    private int integer2;
    private int integer3;
    
    private int[] intarray;
    private int[] intarray2;

    private int partyID;

    public Message(int type) {
        msgType = type;
    }

    public Message(int type, int integer) {
        msgType = type;
        this.integer = integer;
    }

    public Message(int type, int integer, int integer2) {
        msgType = type;
        this.integer = integer;
        this.integer2 = integer2;
    }

    public Message(int type, int integer, int integer2, int integer3) {
        msgType = type;
        this.integer = integer;
        this.integer2 = integer2;
        this.integer3 = integer3;
    }

    public Message(int type, int[] partyThieves) {
        msgType = type;
        this.partyThieves = partyThieves;
    }

    public Message(int type, String name, int nIter) {
        msgType = type;
        fName = name;
        this.nIter = nIter;
        System.out.println("Thief " + thiefID);
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
        this.integer = roomID;
    }
    
    

    public int getInteger() {
        return integer;
    }

    public int getInteger2() {
        return integer2;
    }

    public int getInteger3() {
        return integer3;
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

    public Message(int type, int thiefID, int status, int maxDisp, int partyID, int hasCanvas) {
        msgType = type;
        this.thiefID = thiefID;
        this.status = status;
        this.maxDisp = maxDisp;
        this.partyID = partyID;
        this.hasCanvas = hasCanvas;
        //this.thief = (AssaultThief) Thread.currentThread();
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

    public boolean isB() {
        return b;
    }

    public int[] getIntarray() {
        return intarray;
    }

    public int[] getIntarray2() {
        return intarray2;
    }
    
    

}
