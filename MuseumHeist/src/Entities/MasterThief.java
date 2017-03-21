/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import static GenRepOfInfo.Heist.*;
import GenRepOfInfo.Log;
import Monitors.*;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public class MasterThief extends Thread { //implements IMasterThief

    private int state;

    private final Log log;
    private final IMasterThiefCtrlCollSite mtccs;
    private final IOrdThievesConcSite cs;
    private final IMuseum museum;

    public MasterThief(IMasterThiefCtrlCollSite mtccs, IOrdThievesConcSite cs, IMuseum museum) {
        this.mtccs = mtccs;
        this.cs = cs;
        this.museum = museum;
        this.log = Log.getInstance();

        this.state = PLANNING_THE_HEIST;

    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void run() {

        boolean heistOver = false;

        while (!heistOver) {
            
            switch (this.state) {
                
                case PLANNING_THE_HEIST:
                    this.mtccs.startOperations();
                    this.state = DECIDING_WHAT_TO_DO;
                    break;
                    
                case DECIDING_WHAT_TO_DO:

                    boolean appraise = true;

                    // verificar se rooms estão vazios
                    for (int i = 0; i < this.mtccs.getEmptyRooms().length; i++) {
                        if (this.mtccs.getEmptyRooms()[i] == true) {
                            if (i == this.mtccs.getEmptyRooms().length - 1) {
                                this.mtccs.sumUpResults();
                                appraise = false;
                                this.state = PRESENTING_THE_REPORT;
                            }
                        } else {
                            break;
                        }
                    }

                    //verificar se existe thieves para fazer uma nova assault party
                    for (int i = 0; i < THIEVES_NUMBER; i++) {
                        if (!this.cs.getBusyAssaultThief(i)) {
                            appraise = false;
                            this.mtccs.prepareAssaultParty();
                            this.state = ASSEMBLING_A_GROUP;
                        }
                    }

                    //verificar se as assault parties partiram
                    if (this.mtccs.getPartyNotFull()[0] != -1) {
                        appraise = false;
                        this.mtccs.takeARest();
                        this.state = WAITING_FOR_ARRIVAL;
                    }

                    if (appraise) {
                        this.mtccs.appraiseSit();
                        this.state = DECIDING_WHAT_TO_DO;
                    }

                    break;

                case ASSEMBLING_A_GROUP:

                    int nThievesInAP = 0;
                    int i = 0;

                    while (nThievesInAP < MAX_ASSAULT_PARTY_THIEVES) {
                        if (this.cs.getBusyAssaultThief(i)) {
                            this.cs.callAssaultThief(i);
                            nThievesInAP++;
                        }
                        i++;
                    }
                    this.mtccs.sendAssaultParty();
                    this.state = DECIDING_WHAT_TO_DO;
                    break;
                    
                case WAITING_FOR_ARRIVAL:
                    this.mtccs.collectCanvas();
                    break;
                    
                case PRESENTING_THE_REPORT:
                    heistOver = true;
                    break;
                    
            }
            if (!heistOver) {
                this.log.setMasterThiefState(state);
            }
        }
    }

}
