/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import static GenRepOfInfo.Heist.*;
import OrdThievesConcSite.IOrdThievesConcSite;

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

    public Thief(int id, int maxDisp, IOrdThievesConcSite cs) {
        this.thiefid = id;
        this.maxDisp = maxDisp;
        this.cs = cs;
    }
    
    public int getMaxDisp() {
        return maxDisp;
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
                    if (!this.cs.getBusyAssaultThief(this.thiefid) && this.cs.getAssaultParty(this.thiefid) == -1) {
                        this.cs.amINeeded(this.thiefid);
                    } else {
                        
                    }

            }
        }
    }

    @Override
    public int compareTo(Thief o) {
        return (this.position - o.position);
    }
}
