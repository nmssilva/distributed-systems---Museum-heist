package clientSide;

import auxiliary.Heist;
import static auxiliary.Heist.*;
import static auxiliary.States.*;
import auxiliary.VectorTimestamp;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private VectorTimestamp myClock;
    private VectorTimestamp receivedClock;

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
    public AssaultThief(int thiefID, interfaces.CSIAssaultThief cs, interfaces.CCSIAssaultThief ccs, interfaces.APIAssaultThief ap[], interfaces.MuseumIAssaultThief museum, interfaces.LogIAssaultThief log) throws RemoteException {
        super("Thief_" + thiefID);

        this.cs = cs;
        this.ccs = ccs;
        this.ap = ap;
        this.museum = museum;
        this.log = log;

        this.myClock = new VectorTimestamp(Heist.THIEVES_NUMBER+1, thiefID+1);

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
        try {
            while (amINeeded() != -1) {

                this.myClock.increment();
                prepareExcursion();
                this.myClock.update(this.receivedClock);

                this.myClock.increment();
                crawlIn();
                this.myClock.update(this.receivedClock);

                this.myClock.increment();
                rollACanvas(getRoomID());
                this.myClock.update(this.receivedClock);

                this.myClock.increment();
                reverseDirection();
                this.myClock.update(this.receivedClock);

                this.myClock.increment();
                crawlOut();
                this.myClock.update(this.receivedClock);

                this.myClock.increment();
                handCanvas();
                this.myClock.update(this.receivedClock);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(AssaultThief.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int amINeeded() throws RemoteException {
        partyID = -1;
        hasCanvas = -1;
        status = OUTSIDE;

        this.myClock.increment();
        this.receivedClock = this.cs.amINeeded(this.thiefID, this.maxDisp, this.myClock.clone());
        this.myClock.update(this.receivedClock);

        int party = this.receivedClock.getInteger();

        if (party != -1) {
            //System.out.println("THIEF #" + thiefID + "NEEDED");
            this.partyID = party;
            this.hasCanvas = 0;
            reportStatus();
        }

        logSetThief();
        reportStatus();

        return party;
    }

    private void prepareExcursion() throws RemoteException {

        this.receivedClock = this.ccs.prepareExcursion(this.myClock.clone());

        status = CRAWLING_INWARDS;
        logSetThief();
        reportStatus();

    }

    private void crawlIn() throws RemoteException {

        this.receivedClock = this.ap[this.partyID].crawlIn(this.thiefID, this.myClock.clone());

        status = CRAWLING_INWARDS;
        logSetThief();
        reportStatus();

    }

    private int getRoomID() throws RemoteException {
        return this.ap[this.partyID].getRoomID(this.myClock.clone()).getInteger();
    }

    private void rollACanvas(int roomID) throws RemoteException {
        
        this.receivedClock = this.museum.rollACanvas(roomID, this.myClock.clone());

        hasCanvas = this.receivedClock.getInteger();

        status = AT_A_ROOM;
        logSetThief();
        reportStatus();

    }

    private void reverseDirection() throws RemoteException {

        this.receivedClock = this.ap[this.partyID].reverseDirection(this.thiefID, this.myClock.clone());

        status = CRAWLING_OUTWARDS;
        logSetThief();
        reportStatus();

    }

    private void crawlOut() throws RemoteException {

        this.receivedClock = this.ap[this.partyID].crawlOut(this.thiefID, this.myClock.clone());

        status = CRAWLING_OUTWARDS;
        logSetThief();
        reportStatus();

    }

    private void handCanvas() throws RemoteException {

        this.receivedClock = this.ccs.handCanvas(this.thiefID, this.partyID, this.hasCanvas, this.myClock.clone());

        status = AT_COLLECTION_SITE;
        logSetThief();
        reportStatus();

    }

    private void reportStatus() throws RemoteException {
        this.receivedClock = this.log.reportStatus(this.myClock.clone());
    }

    private void logSetThief() throws RemoteException {

        this.receivedClock = this.log.setAssaultThief(this.thiefID, this.status, this.maxDisp, this.partyID, this.hasCanvas, this.myClock.clone());
    }
}
