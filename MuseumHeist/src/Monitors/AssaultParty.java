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

    private Room room;

    private final IMuseum museum;
    private int[] thievespos = new int[MAX_ASSAULT_PARTY_THIEVES];
    private boolean[] myTurn = new boolean[MAX_ASSAULT_PARTY_THIEVES];
    //
    private int nThievesRoom;
    private boolean[] inRoom = new boolean[MAX_ASSAULT_PARTY_THIEVES];

    public AssaultParty(IMuseum museum, int[] thieves) {
        this.room = new Room(-1);
        this.museum = museum;
        this.nThievesRoom = 0;

        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            if (i == 0) {
                this.myTurn[i] = true;
            } else {
                this.myTurn[i] = false;
            }
            this.inRoom[i] = false;
            this.thievespos[i] = 0;
        }
    }

    @Override
    public synchronized boolean rollACanvas() {
        return this.museum.rollACanvas(room.getId());
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public int getDistOutsideRoom() {
        return this.room.getDistance();
    }

    @Override
    public synchronized void crawlIn() {
        Thief thief = ((Thief) Thread.currentThread());
        while (!myTurn[getPosInParty(thief.getThiefid())]) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        boolean GoodToGo = true;
        int i;
        while (GoodToGo) {

            for (i = thief.getMaxDisp(); i > 0; i--) {
                System.out.println("Thief " + thief.getThiefid() + " crawling in in room with distance " + this.getDistOutsideRoom());
                // nao há avanço
                GoodToGo = true;
                int[] posAfterMove = new int[MAX_ASSAULT_PARTY_THIEVES];

                System.arraycopy(thievespos, 0, posAfterMove, 0, thievespos.length);

                posAfterMove[getPosInParty(thief.getThiefid())] = thief.getPosition() + i;
                Arrays.sort(posAfterMove);

                // verificar se um avanço i é legal
                for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES - 1; j++) {
                    //se posiçoes entre thieves for maior que 3, ou thieves lado a lado expecto na posiçao 0 e na sala
                    if (posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) {
                        GoodToGo = false;
                    }

                    if (posAfterMove[j + 1] == posAfterMove[j]) {
                        if (!(posAfterMove[j] == 0 || posAfterMove[j] == this.getDistOutsideRoom())) {
                            GoodToGo = false;
                        }
                    }
                }

                // avançar
                if (GoodToGo) {
                    thievespos[getPosInParty(thief.getThiefid())] = thief.getPosition() + i;
                    thief.setPosition(thief.getPosition() + i);

                    if (thief.getPosition() > this.getDistOutsideRoom()) {
                        thievespos[getPosInParty(thief.getThiefid())] = this.getDistOutsideRoom();
                        thief.setPosition(this.getDistOutsideRoom());
                        this.nThievesRoom++;
                        inRoom[getPosInParty(thief.getThiefid())] = true;

                    }

                    break;
                }

            }
        }

        myTurn[getPosInParty(thief.getThiefid())] = false;

        int min = this.getDistOutsideRoom();
        int minIndex = -1;

        for (int x = 0; x < MAX_ASSAULT_PARTY_THIEVES; x++) {
            if (min >= this.thievespos[x]) {
                min = this.thievespos[x];
                minIndex = x;
            }
        }
        ;
        myTurn[minIndex] = true;
        System.out.println("Party " + getParty(thief.getThiefid()) + " positions " + Arrays.toString(thievespos));
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
