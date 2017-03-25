/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import static GenRepOfInfo.Heist.*;
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

    public MasterThiefCtrlCollSite(IMuseum museum, IOrdThievesConcSite cs) {
        this.nPaintings = 0;
        this.MasterThiefState = PLANNING_THE_HEIST;
        this.museum = museum;
        this.cs = cs;
        this.ap = new IAssaultParty[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES];

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
            ap[i] = new AssaultParty(museum, assaultparties[i]);
            freeAP[i] = true;
        }

    }

    @Override
    public boolean[] getEmptyRooms() {
        return emptyRooms;
    }

    @Override
    public synchronized void startOperations() {
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        notifyAll();
    }

    public synchronized Room getAssaultRoom() {
        return this.museum.nextRoom();
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

    public synchronized Room checkRoomWithPaintings() {
        return this.museum.nextRoom();
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
    public synchronized void sendAssaultParty() {
        //dizer qual o room
        ap[getFreeAP()].setRoom(checkRoomWithPaintings());
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        notifyAll();
    }
    
    public int getFreeAP(){
        for(int i=0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++){
            if(this.freeAP[i]){
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
    public synchronized void handACanvas() {
        this.nPaintings++;
        notifyAll();
    }
}
