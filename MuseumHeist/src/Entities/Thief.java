/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */

public class Thief extends Thread  { //implements IThief

    private final static int OUTSIDE = 0,
                            CRAWLING_INWARDS = 1,
                            AT_A_ROOM = 2,
                            CRAWLING_OUTWARDS = 3,
                            AT_COLLECTION_SITE = 4;
    
    private int state;
    private final int id;
    private final int str;

    public Thief(int id) {
        this.state = OUTSIDE;
        this.id = id;
        this.str = (int) (Math.random() * 4 + 2);
    }

}
