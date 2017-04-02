/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import entities.Thief;
import static GenRepOfInfo.Heist.*;
import java.util.Arrays;

/**
 *
 * @author Nuno Silva
 */
public class AssaultParty implements IAssaultParty {

    private int id;
    private Thief thieves[];
    private int thievespos[];
    private boolean free;
    private boolean myTurn[];
    private IRoom room;

    /**
     *
     * @param id Assault Party ID
     */
    public AssaultParty(int id) {
        this.id = id;
        this.free = true;
        
        this.room = new Room(-1);
        
        this.thieves = new Thief[MAX_ASSAULT_PARTY_THIEVES];
        this.thievespos = new int[MAX_ASSAULT_PARTY_THIEVES];
        this.myTurn = new boolean[MAX_ASSAULT_PARTY_THIEVES];

        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            thievespos[i] = 0;
            this.myTurn[i] = (i == 0);
        }

    }

    /**
     *
     * @return Assault Party ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     *
     * @param id ID to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return array of thieves in party
     */
    @Override
    public Thief[] getThieves() {
        return thieves;
    }

    /**
     *
     * @param thieves array of thieves to be set
     */
    @Override
    public void setThieves(Thief[] thieves) {
        this.thieves = thieves;
    }

    /**
     *
     * @return array of thieves positions
     */
    public int[] getThievespos() {
        return thievespos;
    }

    /**
     *
     * @param thievespos sets thieves positions
     */
    public void setThievespos(int[] thievespos) {
        this.thievespos = thievespos;
    }

    /**
     *
     * @return returns true is Assault PArty is free. false if otherwise;
     */
    @Override
    public boolean isFree() {
        return free;
    }

    /**
     *
     * @param free true to set Assault party free (not used) or false if not
     * free (with thieves)
     */
    @Override
    public void setFree(boolean free) {
        this.free = free;
    }

    /**
     *
     * @return gets Room where Assault Party is going
     */
    @Override
    public IRoom getRoom() {
        return room;
    }

    /**
     *
     * @param room sets Room where Assault Party is going
     */
    @Override
    public void setRoom(IRoom room) {
        this.room = room;
    }

    /**
     * Crawl in function
     */
    @Override
    public synchronized void crawlIn() {

        Thief thief = ((Thief) Thread.currentThread());

        while (!myTurn[thief.getPosInParty()]) {
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
                GoodToGo = true;
                int[] posAfterMove = new int[MAX_ASSAULT_PARTY_THIEVES];

                System.arraycopy(thievespos, 0, posAfterMove, 0, thievespos.length);

                posAfterMove[thief.getPosInParty()] = thief.getPosition() + i;
                Arrays.sort(posAfterMove);

                // verificar se um avanço i é ilegal
                for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES - 1; j++) {
                    //se posiçoes entre thieves for maior que 3, ou thieves lado a lado expecto na posiçao 0 e na sala
                    if (posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) {
                        GoodToGo = false;
                    }

                    if (posAfterMove[j + 1] == posAfterMove[j]) {
                        if (!(posAfterMove[j] == 0 || posAfterMove[j] == this.room.getDistance())) {
                            GoodToGo = false;
                        }
                    }
                }

                // avançar
                if (GoodToGo) {
                    thievespos[thief.getPosInParty()] = thief.getPosition() + i;
                    thief.setPosition(thief.getPosition() + i);

                    if (thief.getPosition() > this.room.getDistance()) {
                        thievespos[thief.getPosInParty()] = this.room.getDistance();
                        thief.setPosition(this.room.getDistance());
                        GoodToGo = false;
                    }

                    break;
                }

            }
        }

        myTurn[thief.getPosInParty()] = false;

        int min = this.room.getDistance();
        int minIndex = 0;

        for (int x = 0; x < MAX_ASSAULT_PARTY_THIEVES; x++) {
            if (min > this.thievespos[x]) {
                min = this.thievespos[x]; //calculcar a posição do mais atrás
                minIndex = x; //calcular o índice desse thief
            }
        }

        myTurn[minIndex] = true; // acorda o mais atrás
        System.out.println("Party " + this.id + " positions " + Arrays.toString(thievespos));
        notifyAll();
    }

    /**
     *
     * @return true if room has painting, false otherwise
     * 
     */
    @Override
    public boolean rollACanvas() {
        
        Thief thief = (Thief) Thread.currentThread();
        
        if (this.room.getNPaintings() > 0) {
            this.room.setnPaintings(this.room.getNPaintings() - 1);
            thief.setHasCanvas(true);
            System.out.println("PAINTING LEFT ROOM " + this.room.getId() + ": " + this.room.getNPaintings());
            return true;
        }
        
        return false;
    }

    /**
     *  function to transit state to crawling outwards
     */
    @Override
    public synchronized void reverseDirection() {
        
        Thief thief = (Thief) Thread.currentThread();
        thief.setState(CRAWLING_OUTWARDS);
        
        notifyAll();
    }

    /**
     * crawl out function
     */
    @Override
    public synchronized void crawlOut() {
        Thief thief = ((Thief) Thread.currentThread());

        while (!myTurn[thief.getPosInParty()]) {
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
                GoodToGo = true;
                int[] posAfterMove = new int[MAX_ASSAULT_PARTY_THIEVES];

                System.arraycopy(thievespos, 0, posAfterMove, 0, thievespos.length);

                posAfterMove[thief.getPosInParty()] = thief.getPosition() - i;
                Arrays.sort(posAfterMove);

                // verificar se um avanço i é ilegal
                for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES - 1; j++) {
                    //se posiçoes entre thieves for maior que 3, ou thieves lado a lado expecto na posiçao 0 e na sala
                    if (posAfterMove[j + 1] - posAfterMove[j] > THIEVES_MAX_DISTANCE) {
                        GoodToGo = false;
                    }

                    if (posAfterMove[j + 1] == posAfterMove[j]) {
                        if (!(posAfterMove[j] == 0 || posAfterMove[j] == this.room.getDistance())) {
                            GoodToGo = false;
                        }
                    }
                }

                // avançar
                if (GoodToGo) {
                    thievespos[thief.getPosInParty()] = thief.getPosition() - i;
                    thief.setPosition(thief.getPosition() - i);

                    if (thief.getPosition() < 0 ) {
                        thievespos[thief.getPosInParty()] = 0;
                        thief.setPosition(0);
                        GoodToGo = false;
                    }

                    break;
                }

            }
        }

        myTurn[thief.getPosInParty()] = false;

        int max = 0;
        int maxIndex = 0;

        for (int x = 0; x < MAX_ASSAULT_PARTY_THIEVES; x++) {
            if (max < this.thievespos[x]) {
                max = this.thievespos[x]; //calculcar a posição do mais atrás
                maxIndex = x; //calcular o índice desse thief
            }
        }

        myTurn[maxIndex] = true; // acorda o mais atrás
        System.out.println("Party " + this.id + " positions " + Arrays.toString(thievespos));
        notifyAll();
    }

}
