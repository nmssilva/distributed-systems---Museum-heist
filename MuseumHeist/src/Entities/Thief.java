/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import static GenRepOfInfo.Heist.*;
import Monitors.IAssaultParty;
import Monitors.IMasterThiefCtrlCollSite;
import Monitors.IOrdThievesConcSite;
import java.util.Random;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public class Thief extends Thread implements Comparable<Thief> { //implements IThief

    private int thiefid;
    private int maxDisp;
    private int state;

    private int position;
    private final IOrdThievesConcSite cs;
    private IAssaultParty ap;
    private IMasterThiefCtrlCollSite mtccs;

    public Thief(int id, IOrdThievesConcSite cs, IMasterThiefCtrlCollSite mtccs) {
        this.thiefid = id;
        this.maxDisp = new Random().nextInt((MAX_DISPLACEMENT - MIN_DISPLACEMENT) + 1) + MIN_DISPLACEMENT;; 
        this.cs = cs;
        this.mtccs = mtccs;
    }

    public int getMaxDisp() {
        return maxDisp;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    public int getThiefid() {
        return thiefid;
    }

    public void setThiefID(int thiefID) {
        this.thiefid = thiefID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public void setMaxDisp(int maxDisp) {
        this.maxDisp = maxDisp;
    }

    @Override
    public void run() {
        // diz que está ready e fica waiting
        cs.amReady(this.thiefid);

        boolean heistOver = false;

        while (!heistOver) {
            switch (this.state) {
                case OUTSIDE:
                    this.cs.amINeeded(this.thiefid); //blocking state
                    this.ap.prepareExcursion(this.thiefid);
                    this.state = CRAWLING_INWARDS;
                    break;

                case CRAWLING_INWARDS:
                    while (this.position < this.ap.getRoom().getDistance()) {
                        this.ap.waitTurn(this.thiefid);
                        this.ap.crawlIn(this);
                    }
                    this.state = AT_A_ROOM;
                    break;

                case AT_A_ROOM:
                    this.ap.rollACanvas();
                    this.ap.reverseDirection();
                    this.state = CRAWLING_OUTWARDS;
                    break;

                case CRAWLING_OUTWARDS:
                    while (this.position > 0) {
                        this.ap.waitTurn(this.thiefid); //blocking state
                        this.ap.crawlOut(this);
                    }
                    this.state = CRAWLING_OUTWARDS;
                    break;

                case AT_COLLECTION_SITE:
                    this.mtccs.handACanvas();
                    if (areAllTrue(this.mtccs.getEmptyRooms())) {
                        this.state = HEIST_END;
                    } else {
                        this.state = OUTSIDE;
                    }
                    break;

            }
        }
    }

    @Override
    public int compareTo(Thief o) {
        return (this.position - o.position);
    }

    public static boolean areAllTrue(boolean[] array) {
        for (boolean b : array) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
