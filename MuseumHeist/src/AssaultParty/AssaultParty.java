/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AssaultParty;

import Entities.Thief;
import static GenRepOfInfo.Heist.*;
import Museum.IMuseum;
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
    private final int roomID;
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

    public AssaultParty(int roomID, IMuseum museum, Thief[] thieves) {
        this.roomID = roomID;
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

    public synchronized boolean joinAssaultParty(int thiefID) {
        if (this.nThieves < MAX_ASSAULT_PARTY_THIEVES) {
            this.thieves[nThieves].setThiefID(thiefID);
            this.nThieves++;

            return true;
        }

        return false;
    }

    // Get position in array [0,1,2]
    public synchronized int getPos(int thiefID) {
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (thieves[i].getThiefid() == thiefID) {
                return i;
            }
        }

        return -1;
    }

    public synchronized boolean rollACanvas() {
        return this.museum.rollACanvas(this.roomID);
    }

    public synchronized int getNAssaultThieves() {
        return this.nAssaultThieves;
    }

    // Get position of AssaultThief in thieves
    public synchronized int getPosition(int thiefID) {
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (thieves[i].getThiefid() == thiefID) {
                return thieves[i].getPosition();
            }
        }

        return -1;
    }

    public synchronized void waitTurn(int thiefID, int[] clock) {
        notifyAll();

        while (!myTurn[thiefID]) {

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public int getDistOutsideRoom() {
        return this.museum.getDistOutside(this.roomID);
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
            for (i = thief.getMaxDisp(); i >= THIEVES_MIN_DISPLACEMENT; i--) {
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
                            if (thief.getId() == thieves[j].getThiefid()) {
                                thieves[j].setPosition(this.getDistOutsideRoom()); // atualizar posiçao na sala
                                assaultThievesPos[indexPos] = this.getDistOutsideRoom();
                            }
                        }

                        this.nThievesRoom++;
                        inRoom[indexPos] = true;
                    } else {
                        for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                            if (thief.getId() == thieves[j].getThiefid()) {
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

}
