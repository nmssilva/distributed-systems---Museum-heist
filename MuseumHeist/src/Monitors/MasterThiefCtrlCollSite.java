/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import Entities.Thief;
import static GenRepOfInfo.Heist.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva
 */
public class MasterThiefCtrlCollSite implements IMasterThiefCtrlCollSite {

    private int nPaintings;

    private int MasterThiefState;
    private int[][] assaultparties = new int[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES][MAX_ASSAULT_PARTY_THIEVES];
    private boolean[] freeAP = new boolean[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES];
    private boolean[] emptyRooms = new boolean[ROOMS_NUMBER];

    private final IMuseum museum;
    private final IOrdThievesConcSite cs;
    private final IAssaultParty[] ap;

    public MasterThiefCtrlCollSite(IMuseum museum, IOrdThievesConcSite cs, IAssaultParty[] ap) {
        this.nPaintings = 0;
        this.MasterThiefState = PLANNING_THE_HEIST;
        this.museum = museum;
        this.cs = cs;
        this.ap = ap;

        // salas com quadros inicialmente
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.emptyRooms[i] = false;
        }

        //iniciar assaultparties vazias
        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                assaultparties[i][j] = -1;
            }
        }

        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            freeAP[i] = true;
        }

    }

    @Override
    public boolean[] getEmptyRooms() {
        return emptyRooms;
    }

    @Override
    public void setEmptyRooms(int i, boolean f) {
        this.emptyRooms[i] = f;
    }

    @Override
    public synchronized void startOperations() {
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        notifyAll();
    }

    @Override
    public synchronized int[] getEmptySlotParty() {
        int emptySlot[] = {-1, -1};

        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (this.assaultparties[i][j] == -1) {
                    emptySlot[0] = i;
                    emptySlot[1] = j;
                    return emptySlot;
                }
            }
        }

        return emptySlot;
    }

    @Override
    public synchronized boolean getPartiesFull() {
        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (this.assaultparties[i][j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public synchronized void waitArrival() {
        int nPaint = this.nPaintings;
        while (this.nPaintings == nPaint) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MasterThiefCtrlCollSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void prepareAssaultParty() {
        this.MasterThiefState = ASSEMBLING_A_GROUP;
        notifyAll();
    }

    @Override
    public synchronized boolean addThiefToParty(int thiefid) {
        int emptySlot[] = this.getEmptySlotParty();

        if (emptySlot[0] != -1) {
            this.assaultparties[emptySlot[0]][emptySlot[1]] = thiefid;
            ASSAULT_PARTIES[emptySlot[0]][emptySlot[1]] = thiefid;
            return true;
        }

        return false;
    }

    @Override
    public synchronized void sendAssaultParty(int freeap) {
        //dizer qual o room
        System.out.println("FreeAP " + freeap);
        ap[freeap].setRoom(this.museum.nextRoom());
        System.out.println("AP " + freeap + " goes to room " + ap[freeap].getRoom().getId());
        freeAP[freeap] = false;

        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        System.out.println("AP0 status: " + Arrays.toString(ASSAULT_PARTIES[0]) + "\nAP1 status: " + Arrays.toString(ASSAULT_PARTIES[1]));
        notifyAll();
    }

    @Override
    public int getFreeAP() {
        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (this.freeAP[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public synchronized void takeARest() {
        this.MasterThiefState = WAITING_FOR_ARRIVAL;
        notifyAll();
    }

    @Override
    public synchronized void sumUpResults() {
        this.MasterThiefState = PRESENTING_THE_REPORT;
        notifyAll();
    }

    @Override
    public synchronized void appraiseSit() {
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        this.cs.waitAssaultThief();
        notifyAll();
    }

    @Override
    public synchronized void collectCanvas() {
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        notifyAll();
    }

    @Override
    public synchronized void handACanvas(boolean hasCanvas, int i) {
        if (hasCanvas) {
            this.nPaintings++;
        }
        System.out.println("TOTAL COLLECTED CANVAS: " + this.nPaintings);
        
        Thief thief = (Thief) Thread.currentThread();
        int partyid = getParty(thief.getThiefid());
        
        
        this.freeAP[partyid] = true;
        
        thief.getAp()[partyid].getRoom().setFree(true); // libertar room
        this.assaultparties[partyid][getPosInParty(thief.getThiefid())] = -1; //desassociar thief da ap
        
        removeThiefFromParty(thief.getThiefid()); // tirar thief da ap
        
        this.cs.setFreeAssaultThief(thief.getThiefid()); // libertar thief
        
        this.cs.setnAssaultThievesCs(this.cs.getNAssaultThievesCs() + 1); //incrementar thief na CS
        
        
        if (!hasCanvas) {
            System.out.println("Canvas not recovered");
            thief.getAp()[partyid].getRoom().setFree(false);// bloquear room se estiver vazio
            this.emptyRooms[i] = true;
        }
        notifyAll();
    }
}
