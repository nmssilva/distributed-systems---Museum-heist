package clientSide;

import static auxiliary.Heist.*;
import static auxiliary.States.*;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class AssaultThief extends Thread {

    private final int thiefID;
    private int status;
    private final int maxDisp;
    private int partyID;
    private int hasCanvas;
    
    
    private interfaces.CSIAssaultThief cs;
    private interfaces.CCSIAssaultThief ccs;
    private interfaces.APIAssaultThief ap[];
    private interfaces.MuseumIAssaultThief museum;
    private interfaces.LogIAssaultThief log;

    /**
     *
     * Assault Thief Constructor 
     * 
     * @param thiefID ID of Assault Thief
     * @param cs Concentration Site
     * @param ccs Control Collection Site
     * @param ap Assault Parties
     * @param museum Museum
     * @param log Logger
     */
    public AssaultThief(int thiefID,interfaces.CSIAssaultThief cs,interfaces.CCSIAssaultThief ccs,interfaces.APIAssaultThief ap[],interfaces.MuseumIAssaultThief museum,interfaces.LogIAssaultThief log) {
        super("Thief_" + thiefID);
        
        this.cs = cs;
        this.ccs = ccs;
        this.ap = ap;
        this.museum = museum;
        this.log = log;

        this.thiefID = thiefID;
        status = OUTSIDE;
        maxDisp = (int) (Math.random() * (THIEVES_MAX_DISPLACEMENT + 1 - THIEVES_MIN_DISPLACEMENT)) + THIEVES_MIN_DISPLACEMENT;
        partyID = -1;
        hasCanvas = 0;

        logSetThief();

        ////System.out.println("THIEF #" + thiefID + " created");
    }

    /**
     *
     */
    @Override
    public void run() {
        while (amINeeded() != -1) {
            prepareExcursion();
            crawlIn();
            rollACanvas(getRoomID());
            reverseDirection();
            crawlOut();
            handCanvas();
        }
    }

    private int amINeeded() {
        partyID = -1;
        hasCanvas = -1;
        status = OUTSIDE;
        
        int party = this.cs.amINeeded(this.thiefID, this.maxDisp);

        if (party != -1) {
            ////System.out.println("THIEF #" + thiefID + "NEEDED");
            this.partyID = party;
            this.hasCanvas = 0;
            reportStatus();
        }

        logSetThief();
        reportStatus();

        return party;
    }

    private void prepareExcursion() {
        
        this.ccs.prepareExcursion();

        status = CRAWLING_INWARDS;
        logSetThief();
        reportStatus();

    }

    private void crawlIn() {
        
        this.ap[this.partyID].crawlIn(this.thiefID);

        status = CRAWLING_INWARDS;
        logSetThief();
        reportStatus();

    }

    private int getRoomID() {
        return this.ap[this.partyID].getRoomID();
    }

    private void rollACanvas(int roomID) {

        hasCanvas = this.museum.rollACanvas(roomID);

        status = AT_A_ROOM;
        logSetThief();
        reportStatus();

    }

    private void reverseDirection() {
        
        this.ap[this.partyID].reverseDirection(this.thiefID);

        status = CRAWLING_OUTWARDS;
        logSetThief();
        reportStatus();

    }

    private void crawlOut() {
        
        this.ap[this.partyID].crawlOut(this.thiefID);

        status = CRAWLING_OUTWARDS;
        logSetThief();
        reportStatus();

    }

    private void handCanvas() {
        
        this.ccs.handCanvas(this.thiefID, this.partyID, this.hasCanvas);

        status = AT_COLLECTION_SITE;
        logSetThief();
        reportStatus();

    }

    private void reportStatus() {
        this.log.reportStatus();
    }

    private void logSetThief() {
        
        this.log.setAssaultThief(this.thiefID, this.status, this.maxDisp, this.partyID, this.hasCanvas);
    }
}
