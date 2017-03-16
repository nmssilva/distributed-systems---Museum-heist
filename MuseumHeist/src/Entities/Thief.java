/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AssaultParty.AssaultParty;
import static GenRepOfInfo.Heist.*;
import MasterThiefCtrlCollSite.MasterThiefCtrlCollSite;
import Museum.Museum;
import OrdThievesConcSite.OrdThievesConcSite;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public class Thief extends Thread { //implements IThief

    private int state;
    private final int thiefid;
    private final int maxDisp;
    private final int position;

    // monitores
    private OrdThievesConcSite cs;

    public Thief(int id, OrdThievesConcSite cs, AssaultParty ass, Museum museum, MasterThiefCtrlCollSite mtcs) {
        this.state = OUTSIDE;
        this.thiefid = id;
        this.maxDisp = (int) (Math.random() * (THIEVES_MAX_DISPLACEMENT + 1 - THIEVES_MIN_DISPLACEMENT)) + THIEVES_MIN_DISPLACEMENT;
        this.cs = cs;
        this.position = 0;
    }

    // ciclo de vida
    @Override
    public void run() {
        
        
    }

    private void waitForSendAssaultParty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
