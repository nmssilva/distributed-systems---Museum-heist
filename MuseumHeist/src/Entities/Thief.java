/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import static GenRepOfInfo.Heist.*;
import Monitors.AssaultParty;
import Monitors.IAssaultParty;
import Monitors.IMasterThiefCtrlCollSite;
import Monitors.IOrdThievesConcSite;
import java.util.Random;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public class Thief extends Thread { //implements IThief

    private int thiefid;
    private int maxDisp;
    private int state;
    private boolean hasCanvas;

    private int position;
    private IOrdThievesConcSite cs;
    private IAssaultParty[] ap;
    private IMasterThiefCtrlCollSite mtccs;

    public Thief(int id) {
        this.thiefid = id;
        this.hasCanvas = false;
        this.ap = new IAssaultParty[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES];
        this.maxDisp = new Random().nextInt((MAX_DISPLACEMENT - MIN_DISPLACEMENT) + 1) + MIN_DISPLACEMENT;
        this.state = OUTSIDE;
    }

    public boolean getHasCanvas() {
        return hasCanvas;
    }

    public void setHasCanvas(boolean hasCanvas) {
        this.hasCanvas = hasCanvas;
    }

    public IAssaultParty[] getAp() {
        return ap;
    }
    
    public void setMonitors(IOrdThievesConcSite cs, IMasterThiefCtrlCollSite mtccs, AssaultParty[] ap) {
        this.cs = cs;
        this.mtccs = mtccs;
        this.ap = ap;
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

        // flag para indicar thief livre
        this.cs.setFreeAssaultThief(thiefid);

        // diz que está ready e fica waiting
        this.cs.amReady(this.thiefid);

        boolean heistOver = false;

        while (!heistOver) {
            switch (this.state) {
                case OUTSIDE:
                    System.out.println("Thief " + this.thiefid + " is OUTSIDE");
                    this.cs.amINeeded(this.thiefid); //blocking state
                    this.cs.prepareExcursion();
                    this.state = CRAWLING_INWARDS;
                    break;

                case CRAWLING_INWARDS:
                    System.out.println("Thief " + this.thiefid + " is CRAWLING INWARDS room " + this.ap[getParty(this.thiefid)].getRoom().getId());
                    while (this.position < this.ap[getParty(this.thiefid)].getDistOutsideRoom()) {
                        this.ap[getParty(this.thiefid)].crawlIn();
                    }
                    this.state = AT_A_ROOM;
                    break;

                case AT_A_ROOM:
                    System.out.println("Thief " + this.thiefid + " is AT A ROOM");
                    if (this.ap[getParty(thiefid)].rollACanvas()) {

                        System.out.println("Thief " + this.thiefid + " ROLLED CANVAS");
                        setHasCanvas(true);
                    }
                    this.ap[getParty(thiefid)].reverseDirection();
                    this.state = CRAWLING_OUTWARDS;
                    break;

                case CRAWLING_OUTWARDS:
                    System.out.println("Thief " + this.thiefid + " is CRAWLING OUTWARDS");
                    while (this.position > 0) {
                        this.ap[getParty(thiefid)].crawlOut();
                    }
                    this.state = AT_COLLECTION_SITE;
                    break;

                case AT_COLLECTION_SITE:
                    System.out.println("Thief " + this.thiefid + " is AT COLLECTION SITE");
                    this.mtccs.handACanvas(this.hasCanvas, this.ap[getParty(thiefid)].getRoom().getId());
                    setHasCanvas(false);
                    if (areAllTrue(this.mtccs.getEmptyRooms())) {
                        this.state = HEIST_END;
                    } else {
                        this.state = OUTSIDE;
                    }
                    break;

            }
        }
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
