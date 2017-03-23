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
public class MasterThief extends Thread {

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
                    System.out.println("Lady Master Thief is PLANNING THE HEIST");
                    this.mtccs.startOperations();
                    this.state = DECIDING_WHAT_TO_DO;
                    break;

                case DECIDING_WHAT_TO_DO:
                    System.out.println("Lady Master Thief is DECIDING WHAT TO DO");

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
                    int nThievesFree = 0;
                    for (int i = 0; i < THIEVES_NUMBER; i++) {
                        if (this.cs.getFreeAssaultThief(i)) {
                            nThievesFree++;
                        }
                    }
                    System.out.println("nThievesFree:" + nThievesFree);
                    // se sim, muda de estado
                    if (nThievesFree >= MAX_ASSAULT_PARTY_THIEVES) {

                        appraise = false;
                        this.mtccs.prepareAssaultParty();
                        this.state = ASSEMBLING_A_GROUP;
                    }

                    //verificar se as assault parties partiram
                    if (this.mtccs.getPartiesFull()) {
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
                    System.out.println("Lady Master Thief is ASSEMBLING A GROUP");

                    for (int i = 0; i < THIEVES_NUMBER; i++) {
                        if (!this.cs.getFreeAssaultThief(i)) {
                            this.cs.callAssaultThief(i);
                            this.mtccs.addThiefToParty(i);
                        }
                    }
                    this.mtccs.sendAssaultParty();
                    this.state = DECIDING_WHAT_TO_DO;
                    break;

                case WAITING_FOR_ARRIVAL:
                    System.out.println("Lady Master Thief is WAITING FOR ARRIVAL");
                    this.mtccs.collectCanvas();
                    break;

                case PRESENTING_THE_REPORT:
                    System.out.println("Lady Master Thief is PRESENTING THE REPORT");
                    heistOver = true;
                    break;

            }
            if (!heistOver) {
                this.log.setMasterThiefState(state);
            }
        }
    }

}
