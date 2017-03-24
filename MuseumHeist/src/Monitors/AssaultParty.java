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
    private int room;

    private final IMuseum museum;
    private int[] thievesid = new int[MAX_ASSAULT_PARTY_THIEVES];
    private int[] thievespos = new int[MAX_ASSAULT_PARTY_THIEVES];
    private boolean[] myTurn = new boolean[MAX_ASSAULT_PARTY_THIEVES];
    //
    private int nThievesRoom;
    private boolean[] inRoom = new boolean[MAX_ASSAULT_PARTY_THIEVES];
    // Number of AssaultThieves in AssaultParty
    private int nThievesInAP;

    public AssaultParty(int room, IMuseum museum, int[] thieves) {
        this.room = room;
        this.museum = museum;
        this.nThievesRoom = 0;
        this.nThievesInAP = 0;

        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (i == 0) {
                this.myTurn[i] = true;
            } else {
                this.myTurn[i] = false;
            }
            this.inRoom[i] = false;
            this.thievesid[i] = -1;
            this.thievespos[i] = 0;
        }
    }

    public synchronized boolean joinAssaultParty(int thiefid) {
        if (this.nThievesInAP < MAX_ASSAULT_PARTY_THIEVES) {
            this.thievesid[nThievesInAP] = thiefid;
            this.nThievesInAP++;

            return true;
        }

        return false;
    }

    // Get position in array [0,1,2]
    public synchronized int getPos(int thiefid) {
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (thievesid[i] == thiefid) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public synchronized boolean rollACanvas() {
        return this.museum.rollACanvas(room);
    }

    public synchronized int getNAssaultThieves() {
        return this.nAssaultThieves;
    }

    @Override
    public int getRoom() {
        return room;
    }

    // Get position of AssaultThief in thievesid
    public synchronized int getPosition(int thiefid) {
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (thievesid[i] == thiefid) {
                return thievespos[i];
            }
        }

        return -1;
    }

    @Override
    public void setRoom(int room) {
        this.room = room;
    }

    @Override
    public void setThieves(int[] thieves) {
        this.thievesid = thieves;
    }

    @Override
    public synchronized void waitTurn(int thiefid) {
        while (!myTurn[getPosInParty(thiefid)]) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public int getDistOutsideRoom() {
        return this.museum.getDistOutside(this.room);
    }

    @Override
    public synchronized void crawlIn(Thief thief) {

        boolean FarOrOccupied = false;
        while (!FarOrOccupied) {

            int myPos = thief.getPosition();
            int indexPos = 0;

            for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
                
                if (thief.getThiefid() == thievesid[i]) {
                    indexPos = i;
                }
            }
            
            System.out.print("Crawl in positions AP#" + getParty(thief.getThiefid()) + " [ ");
            for( int i : thievespos){
                System.out.print(i + " ");
            }
            System.out.println("]");
            Arrays.sort(thievesid);

            // prever maximo avanço
            for (int i = thief.getMaxDisp(); i >= MIN_DISPLACEMENT; i--) {
                FarOrOccupied = false;
                int[] posAfterMove = thievespos;
                posAfterMove[indexPos] = myPos + i;
                Arrays.sort(posAfterMove);

                for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES - 1; j++) {
                    if ((posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE)
                            || (posAfterMove[j + 1] - posAfterMove[j] == 0
                            && (posAfterMove[j + 1] != 0 || posAfterMove[j + 1] != this.getDistOutsideRoom()))) {
                        //se posiçoes entre thievesid for maior que 3, ou thievesid lado a lado expecto na posiçao 0 e na sala
                        FarOrOccupied = true;
                        break;
                    }

                }

                // avançar
                if ((!FarOrOccupied)) {
                    if (myPos + i >= this.getDistOutsideRoom()) {
                        for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) { // pesquisar no array thievesid o thief atual
                            if (thief.getThiefid() == thievesid[j]) {
                                thievespos[j] = this.getDistOutsideRoom(); // atualizar posiçao na sala
                            }
                        }

                        this.nThievesRoom++;
                        inRoom[indexPos] = true;
                    } else {
                        for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                            if (thief.getThiefid() == thievesid[j]) {
                                thievespos[j] = myPos + i;
                            }
                        }
                    }

                    break;
                }
            }
        }
        myTurn[getPosInParty(thief.getThiefid())] = false;
        myTurn[(getPosInParty(thief.getThiefid())+1) % MAX_ASSAULT_PARTY_THIEVES] = true;
        
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
