/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AssaultParty.AssaultParty;
import MasterThiefCtrlCollSite.MasterThiefCtrlCollSite;
import Museum.Museum;
import OrdThievesConcSite.OrdThievesConcSite;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public class Thief extends Thread { //implements IThief

    private final static int OUTSIDE = 0,
            CRAWLING_INWARDS = 1,
            AT_A_ROOM = 2,
            CRAWLING_OUTWARDS = 3,
            AT_COLLECTION_SITE = 4;

    private int state;
    private final int thiefid;
    private final int maxDisp;
    private static int id = 0;

    // monitores
    private OrdThievesConcSite cs;
    private AssaultParty assaultParty; // acho que isto é criado dinamicamente no life cycle
    private Museum museum;
    private MasterThiefCtrlCollSite mtcs;

    public Thief(int id, OrdThievesConcSite cs, AssaultParty ass, Museum museum, MasterThiefCtrlCollSite mtcs) {
        this.state = OUTSIDE;
        this.thiefid = id++;
        this.maxDisp = (int) (Math.random() * 4 + 2);
        this.cs = cs;
        this.assaultParty = ass;
        this.museum = museum;
        this.mtcs = mtcs;
    }

    // ciclo de vida
    @Override
    public void run() {
        while (!mtcs.sumUpResults()) {
            switch (this.state) {
                case OUTSIDE:
                    this.cs.amINeeded();
                    this.cs.prepareExcursion();

                    if (this.mtcs.sumUpResults()) {
                        continue; // sair do ciclo while acho eu
                    }
                    assaultParty = this.cs.prepareExcursion();
                    this.waitForSendAssaultParty();
                    this.state = CRAWLING_INWARDS;
                case CRAWLING_INWARDS:
                    assaultParty.crawlIn();
                    this.state = AT_A_ROOM;
                    break;
                case AT_A_ROOM:
                    this.museum.rollACanvas(); //missing roomId
                    assaultParty.reverseDirection();
                    this.state = CRAWLING_OUTWARDS;
                    break;
                case CRAWLING_OUTWARDS:
                    assaultParty.crawlOut();
                    this.state = AT_COLLECTION_SITE;
                    break;
                case AT_COLLECTION_SITE:
                    this.mtcs.handCanvas();
                    this.state = OUTSIDE;
                    break;
            }
        }

        // missing end of code
    }

    private void waitForSendAssaultParty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
