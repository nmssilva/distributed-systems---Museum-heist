/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MasterThiefCtrlCollSite;

import AssaultParty.AssaultParty;
import static GenRepOfInfo.Heist.*;
import Museum.IMuseum;

/**
 *
 * @author Nuno Silva
 */
public class MasterThiefCtrlCollSite {

    private int nPaintings;
    private int MasterThiefState;
    private AssaultParty[] assaultParties = new AssaultParty[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES];
    
    private IMuseum museum;

    public MasterThiefCtrlCollSite(IMuseum museum) {
        this.nPaintings = 0;
        this.MasterThiefState = PLANNING_THE_HEIST;
        this.museum = museum;

//        // inicializar AssaultParties vazias
//        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
//            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
//                this.assaultParties[i][j].setThiefID(-1);
//                this.assaultParties[i][j].setPosition(-1);
//            }
//        }
    }

    public synchronized void startOfOperations() {
        this.MasterThiefState = DECIDING_WHAT_TO_DO;
    }

    public synchronized int getAssaultRoom() {
        return this.museum.nextRoom();
    }


//    private synchronized int[] getPartyNotFull() {
//        int emptySlot[] = {-1, -1};
//
//        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
//            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
//                if (this.assaultParties[i].getThiefid() == -1) {
//                    emptySlot[0] = i;
//                    emptySlot[1] = j;
//                    return emptySlot;
//                }
//            }
//        }
//
//        return emptySlot;
//    }
//    
//    
//    public synchronized boolean addAssaultThiefToParty(int thiefID) {
//        int emptySlot[] = this.getPartyNotFull();
//
//        if (emptySlot[0] != -1) {
//            this.assaultParties[emptySlot[0]][emptySlot[1]].setThiefID(thiefID);
//            return true;
//        }
//
//        return false;
//    }

    public synchronized void prepareAssaultParty() {

    }

    public synchronized void sendAssaultParty() {

    }
}
