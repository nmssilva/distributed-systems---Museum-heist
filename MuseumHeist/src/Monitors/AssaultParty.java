/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import Entities.Thief;
import static GenRepOfInfo.Heist.*;
import java.util.Arrays;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 *
 *
 */
public class AssaultParty implements IAssaultParty {

    private int nAssaultThieves;
    private final IRoom room;

    private final IMuseum museum;
    private Thief[] thieves = new Thief[MAX_ASSAULT_PARTY_THIEVES];
    private boolean[] myTurn;
    //
    private int nThievesRoom;
    private boolean[] inRoom = new boolean[MAX_ASSAULT_PARTY_THIEVES];
    // Number of AssaultThieves in AssaultParty
    private int nThieves;
    // Number of Thieves in the last position
    private int nThievesLastPos; // ????

    public AssaultParty(IRoom room, IMuseum museum, Thief[] thieves) {
        this.room = room;
        this.museum = museum;
        this.nThievesRoom = 0;
        this.nThieves = 0;
        this.nThievesLastPos = 0;
        this.thieves = thieves;

        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            this.myTurn[i] = false;
            this.inRoom[i] = false;
        }
    }

    public synchronized boolean joinAssaultParty(int thiefid) {
        if (this.nThieves < MAX_ASSAULT_PARTY_THIEVES) {
            this.thieves[nThieves].setThiefID(thiefid);
            this.nThieves++;

            return true;
        }

        return false;
    }

    // Get position in array [0,1,2]
    public synchronized int getPos(int thiefid) {
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (thieves[i].getThiefid() == thiefid) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public synchronized boolean rollACanvas() {
        return this.museum.rollACanvas(room.getId());
    }

    public synchronized int getNAssaultThieves() {
        return this.nAssaultThieves;
    }

    @Override
    public IRoom getRoom() {
        return room;
    }

    // Get position of AssaultThief in thieves
    public synchronized int getPosition(int thiefid) {
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (thieves[i].getThiefid() == thiefid) {
                return thieves[i].getPosition();
            }
        }

        return -1;
    }

    @Override
    public synchronized void waitTurn(int thiefid) {
        while (!myTurn[thiefid]) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public int getDistOutsideRoom() {
        return this.museum.getDistOutside(this.room.getId());
    }

    @Override
    public void prepareExcursion(int thiefid) {
        this.thieves[thiefid].setState(CRAWLING_INWARDS);
    }

    public synchronized void crawlIn(Thief thief) {

        boolean FarOrOccupied = false;
        while (!FarOrOccupied) {

            int[] assaultThievesPos = new int[MAX_ASSAULT_PARTY_THIEVES];
            int myPos = thief.getPosition();
            int i;
            int indexPos = 0;

            for (i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
                assaultThievesPos[i] = thieves[i].getPosition();
                if (thief.getThiefid() == thieves[i].getThiefid()) {
                    indexPos = i;
                }
            }

            Arrays.sort(thieves);

            // prever maximo avanço
            for (i = thief.getMaxDisp(); i >= MIN_DISPLACEMENT; i--) {
                FarOrOccupied = false;
                int[] posAfterMove = assaultThievesPos;
                posAfterMove[indexPos] = myPos + i;
                Arrays.sort(posAfterMove);

                for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES - 1; j++) {
                    if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE)
                            || (posAfterMove[j + 1] - posAfterMove[j] == 0
                            && (posAfterMove[j + 1] != 0 || posAfterMove[j + 1] != this.getDistOutsideRoom()))) {
                        //se posiçoes entre thieves for maior que 3, ou thieves lado a lado expecto na posiçao 0 e na sala
                        FarOrOccupied = true;
                        break;
                    }

                }

                // avançar
                if ((!FarOrOccupied)) {
                    if (myPos + i >= this.getDistOutsideRoom()) {
                        for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) { // pesquisar no array thieves o thief atual
                            if (thief.getThiefid() == thieves[j].getThiefid()) {
                                thieves[j].setPosition(this.getDistOutsideRoom()); // atualizar posiçao na sala
                                assaultThievesPos[indexPos] = this.getDistOutsideRoom();
                            }
                        }

                        this.nThievesRoom++;
                        inRoom[indexPos] = true;
                    } else {
                        for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                            if (thief.getThiefid() == thieves[j].getThiefid()) {
                                thieves[j].setPosition(myPos + 1);
                                assaultThievesPos[indexPos] = myPos + i;
                            }
                        }
                    }

                    break;
                }
            }
        }
        notifyAll();
    }

    @Override
    public void crawlOut(Thief aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reverseDirection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
