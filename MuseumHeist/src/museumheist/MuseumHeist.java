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
        cs = new OrdThievesConcSite();
        assaultParties = new AssaultParty[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES];
        mtccs = new MasterThiefCtrlCollSite(museum,cs,assaultParties);

        masterThief = new MasterThief(mtccs, cs, museum); // master theif

        masterThief.start(); // lady master thief starts!
        System.out.println("Lady Master Thief begins!!");

        thieves = new Thief[THIEVES_NUMBER]; // thieves
        

        for (int i = 0; i < THIEVES_NUMBER/MAX_ASSAULT_PARTY_THIEVES ; i++) {
            assaultParties[i] = new AssaultParty(museum, ASSAULT_PARTIES[i]);
        }
        
        for (int i = 0; i < THIEVES_NUMBER; i++) { // start thieves!
            thieves[i] = new Thief(i); // no assault party yet
            thieves[i].setMonitors(cs, mtccs, assaultParties);
            thieves[i].start();
            System.out.println("Thief " + i + " begins!!");
        }

        

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            try {
                thieves[i].join();
                System.err.println("Thief " + i + " completed the heist!!");
            } catch (InterruptedException ex) {
                Logger.getLogger(MuseumHeist.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        try {
            masterThief.join();
            System.err.println("Lady Master Thief completed the heist!!");
        } catch (InterruptedException ex) {
            Logger.getLogger(MuseumHeist.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        log.writeEnd();

    }
}
