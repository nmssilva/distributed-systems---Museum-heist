/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import Entities.Thief;
import static GenRepOfInfo.Heist.*;

/**
 *
 * @author Nuno Silva
 */
public class MasterThiefCtrlCollSite implements IMasterThiefCtrlCollSite{

    private int nPaintings;
    private int MasterThiefState;
    private Thief[][] assaultparties = new Thief[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES][MAX_ASSAULT_PARTY_THIEVES];
    private boolean[] emptyRooms = new boolean[ROOMS_NUMBER];

    private final IMuseum museum;
    private IOrdThievesConcSite cs;

    public MasterThiefCtrlCollSite(Museum museum) {
        this.nPaintings = 0;
        this.MasterThiefState = PLANNING_THE_HEIST;
        this.museum = museum;

        // inicializar AssaultParties vazias
        Thief tmpThief = new Thief(-1, -1, this.cs);

        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                assaultparties[i][j] = tmpThief;
            }
        }

        // salas com quadros inicialmente
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.emptyRooms[i] = false;
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

    public synchronized int getAssaultRoom() {
        return this.museum.nextRoom();
    }

    public synchronized boolean addAssaultThiefToParty(Thief thief) {
        int emptySlot[] = this.getPartyNotFull();

        if (emptySlot[0] != -1) {
            this.assaultparties[emptySlot[0]][emptySlot[1]] = thief;
            return true;
        }

        return false;
    }

    @Override
    public synchronized int[] getPartyNotFull() {
        int emptySlot[] = {-1, -1};

        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (this.assaultparties[i][j].getThiefid() == -1) {
                    emptySlot[0] = i;
                    emptySlot[1] = j;
                    return emptySlot;
                }
            }
        }

        return emptySlot;
    }

    public synchronized boolean getPartiesFull() {
        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (this.assaultparties[i][j].getThiefid() == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public synchronized int checkRoomWithPaintings() {
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            if (emptyRooms[i] != false) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public synchronized void prepareAssaultParty() {
        this.MasterThiefState = ASSEMBLING_A_GROUP;
        notifyAll();
    }

    @Override
    public synchronized void sendAssaultParty() {
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        notifyAll();
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
    public synchronized void appraiseSit(){
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        notifyAll();
    }
        
    @Override
    public synchronized void collectCanvas(){
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
        notifyAll();
    }
}
