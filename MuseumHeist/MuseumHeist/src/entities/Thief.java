/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import static GenRepOfInfo.Heist.*;
import java.util.Random;
import monitors.IAssaultParty;
import monitors.IMasterThiefCtrlCollSite;
import monitors.IMuseum;
import monitors.IOrdThievesConcSite;

/**
 *
 * @author Nuno Silva
 */
public class Thief extends Thread {

    private int thiefid;
    private int maxDisp;
    private int state;

    private boolean hasCanvas;
    private boolean free;
    private int position;

    private IOrdThievesConcSite cs;
    private IMuseum museum;
    private IAssaultParty[] ap;
    private IMasterThiefCtrlCollSite mtccs;

    /**
     *
     * @param id Thief id
     */
    public Thief(int id) {
        this.thiefid = id;
        this.hasCanvas = false;
        this.free = true;
        this.position = 0;
        this.maxDisp = new Random().nextInt((MAX_DISPLACEMENT - MIN_DISPLACEMENT) + 1) + MIN_DISPLACEMENT;
        this.state = OUTSIDE;
    }

    /**
     *
     * @param cs Concentration Site
     * @param mtccs Master Thief Control Collection Site
     * @param ap Assault Thieves
     * @param museum Museum
     */
    public void setMonitors(IOrdThievesConcSite cs, IMasterThiefCtrlCollSite mtccs, IAssaultParty[] ap, IMuseum museum) {
        this.cs = cs;
        this.mtccs = mtccs;
        this.ap = ap;
        this.museum = museum;
    }

    
    /**
     *
     * @return Thief state
     */
    public int getThiefState() {
        return state;
    }
    
    /**
     *
     * @return Maximum Displacement of Thief
     */
    public int getMaxDisp() {
        return maxDisp;
    }

    /**
     *
     * @return Thief ID
     */
    public int getThiefid() {
        return thiefid;
    }

    /**
     *
     * @return true if Thief is free. false if otherwise.
     */
    public boolean isFree() {
        return free;
    }

    /**
     *
     * @param free true to set thief free (not in party), false to set thief not
     * free (in party).
     */
    public void setFree(boolean free) {
        this.free = free;
    }

    /**
     *
     * @param state state to be set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     *
     * @return position of Thief
     */
    public int getPosition() {
        return position;
    }

    /**
     *
     * @param position position to be set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     *
     * @return true if thief has canvas, false if otherwise
     */
    public boolean isHasCanvas() {
        return hasCanvas;
    }

    /**
     *
     * @param hasCanvas sets thief to have canvas (or not)
     */
    public void setHasCanvas(boolean hasCanvas) {
        this.hasCanvas = hasCanvas;
    }

    /**
     *
     * @return Assault Party of thief
     */
    public IAssaultParty getAP() {
        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (ap[i] != null) {
                    if (ap[i].getThieves()[j] != null) {
                        if (ap[i].getThieves()[j].getThiefid() == this.thiefid) {
                            return ap[i];
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @return Position/Index of thief in Assault Party
     */
    public int getPosInParty() {
        for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
            for (int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++) {
                if (ap[i] != null) {
                    if (ap[i].getThieves()[j] != null) {
                        if (ap[i].getThieves()[j].getThiefid() == this.thiefid) {
                            return j;
                        }
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public void run() {

        boolean heistOver = false;

        while (!heistOver) {
            switch (this.state) {
                case OUTSIDE:
                    System.out.println("Thief " + this.thiefid + " is OUTSIDE");

                    this.cs.amINeeded(); //blocking state
                    this.cs.prepareExcursion();
                    this.state = CRAWLING_INWARDS;
                    break;

                case CRAWLING_INWARDS:
                    System.out.println("Thief " + this.thiefid + " is CRAWLING_INWARDS");

                    while (this.position < this.getAP().getRoom().getDistance()) {
                        this.getAP().crawlIn();
                    }
                    this.state = AT_A_ROOM;

                    break;

                case AT_A_ROOM:
                    System.out.println("Thief " + this.thiefid + " is AT_A_ROOM");

                    if (this.getAP().rollACanvas()) {

                        System.out.println("Thief " + this.thiefid + " ROLLED CANVAS");
                        setHasCanvas(true);
                    }

                    this.getAP().reverseDirection();
                    this.state = CRAWLING_OUTWARDS;

                    break;

                case CRAWLING_OUTWARDS:
                    System.out.println("Thief " + this.thiefid + " is CRAWLING_OUTWARDS");

                    while (this.position > 0) {
                        this.getAP().crawlOut();
                    }

                    this.state = AT_COLLECTION_SITE;

                    break;

                case AT_COLLECTION_SITE:
                    System.out.println("Thief " + this.thiefid + " is AT_COLLECTION_SITE");

                    //prever próximo estado
                    int roomsWithPaint = countNotZero(this.museum.getNPaintingsRoom());
                    boolean emptyRooms = areAllFalse(this.museum.getFreeRooms());

                    if (roomsWithPaint == 0 || emptyRooms) {
                        this.state = HEIST_END;
                    } else {
                        this.state = OUTSIDE;
                    }

                    this.mtccs.waitForMaster();
                    this.mtccs.handACanvas();

                    System.out.println("N thieves CS: " + this.cs.getnAssaultThievesCs());

                    break;

                case HEIST_END:
                    System.out.println("Thief " + this.thiefid + " HEIST END");
                    heistOver = true;
                    break;
            }
        }

    }

}
