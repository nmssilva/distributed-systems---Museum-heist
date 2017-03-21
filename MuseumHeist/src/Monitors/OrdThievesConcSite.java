package OrdThievesConcSite;

import static GenRepOfInfo.Heist.*;
import GenRepOfInfo.MemFIFO;

/**
 *
 * @author Nuno Silva
 */
public class OrdThievesConcSite {

    // Declaração de variáveis
    private MemFIFO waitQueue;
    private int[] assaultThiefstate = new int[THIEVES_NUMBER];
    private boolean[] busyAssaultThief = new boolean[THIEVES_NUMBER];
    private int nAssaultThievesCs;
    private int maxDispAssaultThief[] = new int[THIEVES_NUMBER];
    private int assaultParty[] = new int[THIEVES_NUMBER];  //array de thiefs na concentration site

    // construtor
    public OrdThievesConcSite(int nThief, int nMax) {
        this.nAssaultThievesCs = 0;
        this.waitQueue = new MemFIFO(THIEVES_NUMBER);

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            this.assaultThiefstate[i] = OUTSIDE;
            this.busyAssaultThief[i] = false;
            this.assaultParty[i] = -1;
            this.maxDispAssaultThief[i] = (int) (Math.random() * (THIEVES_MAX_DISPLACEMENT + 1 - THIEVES_MIN_DISPLACEMENT)) + THIEVES_MIN_DISPLACEMENT;
        }

    }

    public int getAssaultParty(int id) {
        return this.assaultParty[id];
    }

    // procedimentos do monitor 
    // @Override
    public synchronized void amINeeded(int thiefID) {
        notifyAll();

        while (!this.busyAssaultThief[thiefID] && this.assaultThiefstate[thiefID] != HEIST_END) { //???
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public synchronized void waitAssaultThief(int thiefID) {  //master thief waits to assemble assaultparty
        while (this.nAssaultThievesCs < MAX_ASSAULT_PARTY_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        notifyAll();
    }

    public synchronized void waitThievesEnd(int thiefID) {  //master thief waits for all thiefs to return
        while (this.nAssaultThievesCs < THIEVES_NUMBER) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            this.assaultThiefstate[i] = HEIST_END; // heist ends
        }

        notifyAll();
    }

    public synchronized void amReady(int thiefID) {  // thief is ready to assault
        if (!waitQueue.full()) {
            //this.busyAssaultThief[thiefID] = false;
            this.waitQueue.write(thiefID);      // thief waits in queue
            this.nAssaultThievesCs++;           // number of thieves in CS ++
            this.assaultParty[thiefID] = -1;    // thief goes out of concentration site
        } else {
            System.out.println("ERROR!! Wait Queue full.");
        }

        if (this.nAssaultThievesCs >= MAX_ASSAULT_PARTY_THIEVES) {  // number of thieves sufficient to assemble party
            notifyAll();
        } else {
            try {
                wait(); // block if otherwise
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public synchronized int getNAssaultThievesCs() {
        return this.nAssaultThievesCs;
    }

    public synchronized int getStateAssaultThiefState(int thiefID) {
        return this.assaultThiefstate[thiefID];
    }

    public synchronized void callAssaultThief(int thiefid) {
        int id = -1;

        if (!waitQueue.empty()) {
            id = ((Integer) this.waitQueue.read());
            this.nAssaultThievesCs--;
            this.busyAssaultThief[id] = true;
            this.assaultParty[id] = thiefid; //thief returns

            notifyAll();
        }
    }

    public synchronized boolean getBusyAssaultThief(int thiefID) {
        return this.busyAssaultThief[thiefID];
    }

    public synchronized boolean getAssaultThievesConcentrationSite() {
        return !this.waitQueue.empty();
    }

    public synchronized void sendAssaultParty(int thiefID) {
        this.assaultThiefstate[thiefID] = CRAWLING_INWARDS;
    }

    public synchronized void regressarAssalto(int thiefID) {
        this.assaultThiefstate[thiefID] = CRAWLING_OUTWARDS;
    }

    public synchronized void atRoom(int thiefID) {
        this.assaultThiefstate[thiefID] = AT_A_ROOM;
    }

    public synchronized void indicarRegresso(int thiefID) {
        //this.busyAssaultThief[thiefID] = false;
        this.assaultParty[thiefID] = -1;
        this.assaultThiefstate[thiefID] = OUTSIDE;

        this.amReady(thiefID);
    }

    public synchronized int getMaxDisp(int thiefID) {
        return this.maxDispAssaultThief[thiefID];
    }

}
