/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumheist;

import static GenRepOfInfo.Heist.*;
import GenRepOfInfo.Log;
import entities.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import monitors.*;

/**
 *
 * @author Nuno Silva
 */

public class MuseumHeist {

    /**
     * @param args the command line arguments
     */
    
    private static MasterThief masterThief;         // Master Thief
    private static Thief[] thieves;                 // Thiefs

    private static Log log;                         // Log
    private static AssaultParty[] assaultParties;   // Assault Parties
    private static OrdThievesConcSite cs;           // Concentration Site
    private static MasterThiefCtrlCollSite mtccs;   // Collection Site
    private static Museum museum;                   // Museum
    
    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {     
        
        // create Thieves

        thieves = new Thief[THIEVES_NUMBER]; // thieves
        
        for (int i = 0; i < THIEVES_NUMBER; i++) { // start thieves!
            thieves[i] = new Thief(i); 
        }   
         
        // START MONITORS
        
        museum = new Museum();
        cs = new OrdThievesConcSite(thieves);
        assaultParties = new AssaultParty[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES];
        mtccs = new MasterThiefCtrlCollSite();
        
        log = Log.getInstance(mtccs, cs, assaultParties, museum, thieves);
        
        for(int i = 0; i < THIEVES_NUMBER/MAX_ASSAULT_PARTY_THIEVES; i++){
            assaultParties[i] = new AssaultParty(i);
        }
        
         // set monitors to thieves
         
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            thieves[i].setMonitors(cs, mtccs, assaultParties, museum);
            thieves[i].start();
            System.out.println("Thief " + i + " begins!!");
        }   
        
        cs.setThievesInCs(thieves);
        
        //start MASTER THIEF
        masterThief = new MasterThief(mtccs, cs, museum, assaultParties, thieves);
        
        mtccs.setMasterThief(masterThief);
        
        masterThief.start(); // lady master thief starts!
        System.out.println("Lady Master Thief begins!!");
        
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            try {
                thieves[i].join();
                System.out.println("Thief " + i + " completed the heist!!");
            } catch (InterruptedException ex) {
                Logger.getLogger(MuseumHeist.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        try {
            masterThief.join();
            System.out.println("Lady Master Thief completed the heist!!");
        } catch (InterruptedException ex) {
            Logger.getLogger(MuseumHeist.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        log.writeEnd();
        
        
    }
    
}
