package serverSide;

import static auxiliary.Heist.*;
import genclass.*;
import interfaces.LoggerInterface;
import java.rmi.RemoteException;
import java.util.stream.IntStream;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class Logger implements LoggerInterface{

    private String fileName;
    private String masterThiefStatus;
    private String[] assaultThiefStatus;
    private int[] assaultThiefMaxDisp;
    private String[] assaultThiefSituation;
    private int[] hasCanvas;
    private int nPaintings;
    private int[] roomsdistance;
    private int[] roomspaintings;
    private TextFile log;
    private int[][] asspartiesPos;
    private int[][] assparties;
    private int[] asspartiesRoomID;
    private String lastLine, lastLine2;

    private int nIter; // ver o que podemos fazer

    /**
     *
     */
    public Logger(){
        fileName = "logger.log";
        log = new TextFile();
        assaultThiefStatus = new String[THIEVES_NUMBER];
        assaultThiefMaxDisp = new int[THIEVES_NUMBER];
        assaultThiefSituation = new String[THIEVES_NUMBER];
        hasCanvas = new int[THIEVES_NUMBER];
        nPaintings = 0;
        //rooms = new Room[ROOMS_NUMBER];
        lastLine = "";
        lastLine2 = "";

        assparties = new int[2][MAX_ASSAULT_PARTY_THIEVES];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                assparties[i][j] = -1;
            }
        }

        asspartiesPos = new int[2][MAX_ASSAULT_PARTY_THIEVES];

        asspartiesRoomID = new int[2];
        for (int i = 0; i < 2; i++) {
            asspartiesRoomID[i] = -1;
        }
    }

    /**
     * Set the number of Paintings in the General Repository of Information.
     *
     * @param nPaintings Number of Paintings in the General Repository of Information
     */
    public void setnPaintings(int nPaintings) {
        this.nPaintings = nPaintings;
    }

    /**
     * Set an Assault Party in the General Repository of Information.
     *
     * @param id Assault Party ID
     * @param elements Assault Party int array of thieves IDs
     * @param positions Assault Party int array of thieves positions
     * @param roomID Assault Party designated room ID
     */
    public void setAssaultParty(int id, int[] elements, int[] positions, int roomID) {
        assparties[id] = elements;
        asspartiesPos[id] = positions;
        asspartiesRoomID[id] = roomID;
    }

    /**
     * Set the Master Thief in the General Repository of Information.
     *
     * @param status Status of Master Thief to be set
     */
    public void setMasterThief(int status) {
        switch (status) {
            case 1000:
                masterThiefStatus = "PLAN";
                break;
            case 2000:
                masterThiefStatus = "DCID";
                break;
            case 3000:
                masterThiefStatus = "ASSG";
                break;
            case 4000:
                masterThiefStatus = "WAIT";
                break;
            case 5000:
                masterThiefStatus = "REPO";
                break;

        }
    }

    /**
     * Set an Assault Thief in the General Repository of Information.
     *
     * @param thiefID ID of Assault Thief
     * @param status Status of Assault Thief
     * @param maxDisp Maximum Displacement of Assault Thief
     * @param partyID Party ID of Assault Thief
     * @param hasCanvas 1 if Assault Thief has canvas, 0 if otherwise
     */
    public void setAssaultThief(int thiefID, int status, int maxDisp, int partyID, int hasCanvas) {
        switch (status) {
            case 1000:
                assaultThiefStatus[thiefID] = "OUTS";
                break;
            case 1001:
                assaultThiefStatus[thiefID] = "WAIT";
                break;
            case 2000:
                assaultThiefStatus[thiefID] = "CR_I";
                break;
            case 3000:
                assaultThiefStatus[thiefID] = "ROOM";
                break;
            case 4000:
                assaultThiefStatus[thiefID] = "CR_O";
                break;
            case 5000:
                assaultThiefStatus[thiefID] = "COLL";
                break;
            case 6000:
                assaultThiefStatus[thiefID] = "HEND";
                break;

        }
        assaultThiefMaxDisp[thiefID] = maxDisp;
        if (partyID == -1) {
            assaultThiefSituation[thiefID] = "W";
        } else {
            assaultThiefSituation[thiefID] = "" + partyID;
        }
        this.hasCanvas[thiefID] = hasCanvas;
    }

    /**
     * Set the Museum in the General Repository of Information
     *
     * @param roomsd Rooms Distances Array
     * @param roomsp Rooms Number of Paintings Array
     */
    public void setMuseum(int[] roomsd, int[] roomsp) {
        this.roomsdistance = roomsd;
        this.roomspaintings = roomsp;
        int p = IntStream.of(roomsp).sum();
        if (p > this.nPaintings) {
            this.nPaintings = p;
        }
    }

    /**
     * Start a file where all the operations will be written along with the
     * changes to the main attributes of the Heist.
     *
     * @param fileName Logger File Name
     * @param nIter Number of Iterations
     */
    public synchronized void setFileName(String fileName, int nIter) {
        if ((fileName != null) && !("".equals(fileName))) {
            this.fileName = fileName;
        }
        if (nIter > 0) {
            this.nIter = nIter;
        }
        reportInitialStatus();
        reportStatus();
    }

    /**
     * Log the initial status of the Heist.
     *
     */
    public void reportInitialStatus() {
        if (!log.openForWriting(".", fileName)) {
            GenericIO.writelnString("2 A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
        log.writelnString("                             Heist to the Museum - Description of the internal state\n\n");
        log.writelnString("MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6");
        log.writelnString("Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD");
        log.writelnString("                   Assault party 1                       Assault party 2                       Museum");
        log.writelnString("           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5");
        log.writelnString("    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT");
        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
    }

    /**
     * Log the status of everything in the General Repository of Information.
     *
     */
    public synchronized void reportStatus() {
        boolean dontPrint = false;
        if (!log.openForAppending(".", fileName)) {
            GenericIO.writelnString("3 A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }

        String line = "";
        String line2 = "";

        line += masterThiefStatus + "  ";
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            if (assaultThiefStatus[i] != null) {
                line += assaultThiefStatus[i] + " " + assaultThiefSituation[i] + "  " + assaultThiefMaxDisp[i] + "    ";
            } else {
                //line += "----" + " " + "-" + "  " + "-" + "    ";
                dontPrint = true;
            }
        }

        line2 += "     ";

        for (int i = 0; i < 2; i++) {
            if (asspartiesRoomID[i] == -1) {
                line2 += "-" + "    ";
            } else {
                line2 += asspartiesRoomID[i] + "    ";
            }

            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (assparties[i][j] != -1) {
                    if (asspartiesPos[i][j] < 10) {
                        line2 += (assparties[i][j] + 1) + "   " + asspartiesPos[i][j] + "  " + hasCanvas[assparties[i][j]] + "   ";
                    } else {
                        line2 += (assparties[i][j] + 1) + "  " + asspartiesPos[i][j] + "  " + hasCanvas[assparties[i][j]] + "   ";
                    }
                } else {
                    line2 += "-" + "  " + "--" + "  " + "-" + "   ";
                }
            }
            line2 += "";
        }
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            if (roomspaintings[i] < 10) {
                line2 += " " + roomspaintings[i] + " " + roomsdistance[i] + "   ";
            } else {
                line2 += roomspaintings[i] + " " + roomsdistance[i] + "   ";
            }
        }
        if (!dontPrint) {
            if (!(lastLine.equals(line) && lastLine2.equals(line2))) {
                log.writelnString(line);
                log.writelnString(line2);
                lastLine = line;
                lastLine2 = line2;
            }
        }

        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
    }

    /**
     * Log the final status of the Heist.
     *
     */
    public synchronized void reportFinalStatus() {
        if (!log.openForAppending(".", fileName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }

        log.writelnString("My friends, tonight's effor produced " + nPaintings + " priceless paintings!");
        log.writelnString("\nLegend:");
        log.writelnString("MstT Stat – state of the master thief");
        log.writelnString("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)");
        log.writelnString("Thief # S – situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)");
        log.writelnString("Thief # MD – maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6");
        log.writelnString("Assault party # RId – assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)");
        log.writelnString("Assault party # Elem # Id – assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)");
        log.writelnString("Assault party # Elem # Pos – assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)");
        log.writelnString("Assault party # Elem # Cv – assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)");
        log.writelnString("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls");
        log.writelnString("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30");

        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
    }

    @Override
    public void signalShutdown() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
