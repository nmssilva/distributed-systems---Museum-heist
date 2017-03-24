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

    private int room;

    private final IMuseum museum;
    private int[] thievespos = new int[MAX_ASSAULT_PARTY_THIEVES];
    private boolean[] myTurn = new boolean[MAX_ASSAULT_PARTY_THIEVES];
    //
    private int nThievesRoom;
    private boolean[] inRoom = new boolean[MAX_ASSAULT_PARTY_THIEVES];

    public AssaultParty(int room, IMuseum museum, int[] thieves) {
        this.room = room;
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
        return this.museum.rollACanvas(room);
    }

    @Override
    public int getRoom() {
        return room;
    }

    @Override
    public void setRoom(int room) {
        this.room = room;
    }

    @Override
    public synchronized void waitTurn(int thiefid) {
        while (!myTurn[getPosInParty(thiefid)]) {
            try {
                System.out.print("THIEF " + thiefid + " VAI DORMIR ");
                wait();
                System.out.print("THIEF " + thiefid + " SAIU ");
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public int getDistOutsideRoom() {
        return this.museum.getDistOutside(this.room);
    }

    @Override
    public synchronized void crawlIn(Thief thief) {

        System.out.println("CRAWLING Thief#" + thief.getThiefid() + " with maxDisp: " + thief.getMaxDisp());
        boolean GoodToGo = true;
        int i;
        while (GoodToGo) {

            for (i = thief.getMaxDisp(); i > 0; i--) {

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
                    System.out.println("GOOD TO GO");
                    thievespos[getPosInParty(thief.getThiefid())] = thief.getPosition() + i;
                    thief.setPosition(thief.getPosition() + i);

                    if (thief.getPosition() + i > this.getDistOutsideRoom()) {
                        thievespos[getPosInParty(thief.getThiefid())] = this.getDistOutsideRoom();
                        thief.setPosition(this.getDistOutsideRoom());
                        this.nThievesRoom++;
                        inRoom[getPosInParty(thief.getThiefid())] = true;

                    }

                    break;
                }

            }
        }

        System.out.print("Crawl in final positions AP#" + getParty(thief.getThiefid()) + " [ ");
        for (int x : thievespos) {
            System.out.print(x + " ");
        }
        System.out.println("]");

        myTurn[getPosInParty(thief.getThiefid())] = false;
        
        
        
        int min = this.getDistOutsideRoom();
        int minIndex = -1;
        
        for( int x = 0 ; x < MAX_ASSAULT_PARTY_THIEVES ; x++){
            if(min >= this.thievespos[x]){
                min = this.thievespos[x];
                minIndex = x;
            }
        }
        
        System.out.println("MININDEX: " + minIndex);
        myTurn[minIndex] = true;

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
