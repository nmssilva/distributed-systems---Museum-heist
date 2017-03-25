package Monitors;

import Entities.Thief;
import static GenRepOfInfo.Heist.*;
import GenRepOfInfo.MemFIFO;

/**
 *
 * @author Nuno Silva
 */
public class OrdThievesConcSite implements IOrdThievesConcSite {

    // Declaração de variáveis
    private MemFIFO waitQueue;
    private int nAssaultThievesCs;

    private int[] assaultThiefstate = new int[THIEVES_NUMBER];
    private int[] maxDispAssaultThief = new int[THIEVES_NUMBER];
    private int[] thievesInCs = new int[THIEVES_NUMBER];  //array de thiefs na concentration site
    private boolean[] freeAssaultThief = new boolean[THIEVES_NUMBER];

    // construtor
    public OrdThievesConcSite() {
        this.nAssaultThievesCs = 0;
        this.waitQueue = new MemFIFO(THIEVES_NUMBER);

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            this.assaultThiefstate[i] = OUTSIDE;
            this.freeAssaultThief[i] = false;
            this.thievesInCs[i] = -1;
            this.maxDispAssaultThief[i] = (int) (Math.random() * (MAX_DISPLACEMENT + 1 - MIN_DISPLACEMENT)) + MIN_DISPLACEMENT;
        }

    }

    @Override
    public int getAssaultParty(int id) {
        return this.thievesInCs[id];
    }

    
    @Override
    public synchronized void amReady(int thiefid) {  // thief is ready to assault
        if (!waitQueue.full()) {
            this.waitQueue.write(thiefid);      // thief waits in queue
            this.nAssaultThievesCs++;           // number of thieves in CS ++
            this.thievesInCs[thiefid] = -1;    // thief goes out of concentration site
        } else {
            System.err.println("ERROR!! WaitQueue is full.");
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
    
    @Override
    public synchronized void amINeeded(int thiefid) {
        notifyAll();

        while (getParty(thiefid) == -1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public synchronized void waitAssaultThief() {  //master thief waits to assemble assaultparty
        while (this.nAssaultThievesCs < MAX_ASSAULT_PARTY_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        notifyAll();
    }

    public synchronized void waitThievesEnd(int thiefid) {  //master thief waits for all thiefs to return
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


    @Override
    public synchronized int getNAssaultThievesCs() {
        return this.nAssaultThievesCs;
    }
   
    @Override
    public void setnAssaultThievesCs(int nAssaultThievesCs) {
        this.nAssaultThievesCs = nAssaultThievesCs;
    }
    
    public synchronized int getStateAssaultThiefState(int thiefid) {
        return this.assaultThiefstate[thiefid];
    }

    @Override
    public synchronized void callAssaultThief(int thiefid) {

        if (!waitQueue.empty()) {
            int id = ((Integer) this.waitQueue.read());
            this.nAssaultThievesCs--;
            this.freeAssaultThief[id] = false;
            this.thievesInCs[id] = thiefid;
            notifyAll();

        }
    }

    @Override
    public synchronized boolean getFreeAssaultThief(int thiefid) {

        return this.freeAssaultThief[thiefid];
    }

    @Override
    public void setFreeAssaultThief(int thiefid) {
        this.freeAssaultThief[thiefid] = true;
    }

    @Override
    public synchronized void prepareExcursion() {
        Thief thief = (Thief) Thread.currentThread();
        //thief.getAp()[getParty(thief.getThiefid())].setRoom();
        this.assaultThiefstate[thief.getThiefid()] = CRAWLING_INWARDS;
    }

    public synchronized void regressarAssalto(int thiefid) {
        this.assaultThiefstate[thiefid] = CRAWLING_OUTWARDS;
    }

    public synchronized void atRoom(int thiefid) {
        this.assaultThiefstate[thiefid] = AT_A_ROOM;
    }

    public synchronized void indicarRegresso(int thiefid) {
        //this.freeAssaultThief[thiefid] = false;
        this.thievesInCs[thiefid] = -1;
        this.assaultThiefstate[thiefid] = OUTSIDE;

        this.amReady(thiefid);
    }

    public synchronized int getMaxDisp(int thiefid) {
        return this.maxDispAssaultThief[thiefid];
    }

}
