/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MuseumHeist;

import static GenRepOfInfo.Heist.*;
import Monitors.AssaultParty;
import Entities.MasterThief;
import Entities.Thief;
import GenRepOfInfo.Log;
import Monitors.MasterThiefCtrlCollSite;
import Monitors.Museum;
import Monitors.OrdThievesConcSite;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 */
public class MuseumHeist {

    private static MasterThief masterThief;
    private static Thief[] thieves;

    private static Log log;
    private static AssaultParty[] assaultParties;
    private static OrdThievesConcSite cs;
    private static MasterThiefCtrlCollSite mtccs;
    private static Museum museum;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        log = Log.getInstance();

        museum = new Museum();
        mtccs = new MasterThiefCtrlCollSite(museum);
        cs = new OrdThievesConcSite();
        assaultParties = new AssaultParty[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES];

        masterThief = new MasterThief(mtccs, cs, museum); // master theif

        masterThief.start(); // lady master thief starts!

        thieves = new Thief[THIEVES_NUMBER]; // thieves

        for (int i = 0; i < THIEVES_NUMBER; i++) { // start thieves!
            thieves[i] = new Thief(i, cs, mtccs);
            thieves[i].start();
        }

       
        try {
            masterThief.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MuseumHeist.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            try {
                thieves[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MuseumHeist.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        log.writeEnd();

    }
}
