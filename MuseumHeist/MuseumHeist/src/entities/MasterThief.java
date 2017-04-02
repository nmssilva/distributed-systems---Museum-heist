/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import static GenRepOfInfo.Heist.*;
import GenRepOfInfo.Log;
import java.util.Arrays;
import monitors.IAssaultParty;
import monitors.IMasterThiefCtrlCollSite;
import monitors.IMuseum;
import monitors.IOrdThievesConcSite;
import monitors.IRoom;

/**
 *
 * @author Nuno Silva
 */
public class MasterThief extends Thread {

    private int state;

    private IMasterThiefCtrlCollSite mtccs;
    private IOrdThievesConcSite cs;
    private IMuseum museum;

    private IAssaultParty[] ap;
    private Thief[] thieves;
    private Log log;

    /**
     *
     * @param mtccs Master Thief Control Collection Site
     * @param cs Concentration Site
     * @param museum Museum
     * @param ap Assault Parties
     * @param thieves Thieves
     */
    public MasterThief(IMasterThiefCtrlCollSite mtccs, IOrdThievesConcSite cs, IMuseum museum, IAssaultParty[] ap, Thief[] thieves) {
        this.mtccs = mtccs;
        this.cs = cs;
        this.museum = museum;
        this.ap = ap;
        this.thieves = thieves;
        this.log = Log.getInstance(mtccs, cs, ap, museum, thieves);

        this.state = PLANNING_THE_HEIST;

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
     * @return MasterThief state
     */
    public int getMasterState() {
        return state;
    }
    
    /**
     *
     * @return Museum
     */
    public IMuseum getMuseum() {
        return museum;
    }

    /**
     *
     * @return Assault Parties
     */
    public IAssaultParty[] getAp() {
        return ap;
    }

    @Override
    public void run() {

        boolean heistOver = false;

        while (!heistOver) {

            switch (this.state) {

                case PLANNING_THE_HEIST:
                    System.out.println("Lady Master Thief is PLANNING THE HEIST");
                    this.mtccs.startOperations();
                    this.log.writeStatusLine();
                    this.state = DECIDING_WHAT_TO_DO;
                    break;

                case DECIDING_WHAT_TO_DO:
                    System.out.println("Lady Master Thief is DECIDING_WHAT_TO_DO");

                    System.out.println("N ASSAULT THIEVES CS: " + this.cs.getnAssaultThievesCs());
                    
                    // verificar se rooms estão vazios
                    if (this.cs.checkThievesEndHeist()) {
                        this.mtccs.sumUpResults();
                        this.log.writeStatusLine();
                        this.state = PRESENTING_THE_REPORT;
                    }

                    // verificar se existe thieves para fazer uma nova assault party
                    // se sim, forma uma party
                    
                    else if (this.cs.getnAssaultThievesCs() >= MAX_ASSAULT_PARTY_THIEVES 
                            && !this.mtccs.getPartiesFull() 
                            && !areAllFalse(this.museum.getFreeRooms())
                            && countNotZero(this.museum.getNPaintingsRoom()) > 0) {
                        this.mtccs.prepareAssaultParty();
                        this.log.writeStatusLine();
                        this.state = ASSEMBLING_A_GROUP;
                    }

                    
                    
                    //verificar se as assault parties partiram
                    else if (this.cs.getnAssaultThievesCs() < MAX_ASSAULT_PARTY_THIEVES) {
                        this.mtccs.takeARest();
                        this.log.writeStatusLine();
                        this.state = WAITING_FOR_ARRIVAL;
                    }

                    else {
                        this.mtccs.appraiseSit();
                        this.log.writeStatusLine();
                        this.state = DECIDING_WHAT_TO_DO;
                    }

                    break;

                case ASSEMBLING_A_GROUP:
                    System.out.println("Lady Master Thief is ASSEMBLING_A_GROUP");

                    //get free AP
                    IAssaultParty apToSend = null;
                    IRoom roomToSend = null;

                    for (int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++) {
                        if (ap[i].isFree()) {
                            apToSend = ap[i];
                            break;
                        }
                    }

                    System.out.println("FREE ROOMS: " + Arrays.toString(this.museum.getFreeRooms()) + "\nNPAINTING: " + Arrays.toString(this.museum.getNPaintingsRoom()));

                    for (int i = 0; i < ROOMS_NUMBER; i++) {
                        if (this.museum.getFreeRooms()[i] == true) {
                            if (this.museum.getNPaintingsRoom()[i] > 0) {
                                roomToSend = this.museum.getRooms()[i];
                                break;
                            }
                        }
                    }

                    int nThievesinAP = 0;
                    Thief thievestobeinAP[] = new Thief[MAX_ASSAULT_PARTY_THIEVES];

                    for (int i = 0; i < THIEVES_NUMBER || nThievesinAP < MAX_ASSAULT_PARTY_THIEVES; i++) {
                        Thief th = this.cs.getThieves()[i];
                        if (cs.getThievesInCs()[i]) {
                            thievestobeinAP[nThievesinAP] = th;
                            nThievesinAP++;
                            if (nThievesinAP == MAX_ASSAULT_PARTY_THIEVES) {
                                break;
                            }

                        }
                    }

                    System.out.println(nThievesinAP + " THIEVES TO BE IN AP" + apToSend.getId() + " : " + Arrays.toString(thievestobeinAP) + "\nGoing to Room " + roomToSend.getId());

                    for (Thief th : thievestobeinAP) {
                        this.cs.callAssaultThief(th.getThiefid());
                    }

                    apToSend.setThieves(thievestobeinAP);
                    apToSend.setRoom(roomToSend);
                    roomToSend.setFree(false);

                    this.mtccs.sendAssaultParty(apToSend, roomToSend);

                    this.cs.setnAssaultThievesCs(this.cs.getnAssaultThievesCs() - MAX_ASSAULT_PARTY_THIEVES);

                    this.log.writeStatusLine();
                    this.state = DECIDING_WHAT_TO_DO;

                    break;

                case WAITING_FOR_ARRIVAL:
                    System.out.println("Lady Master Thief is WAITING_FOR_ARRIVAL");

                    this.cs.waitForArrival();

                    this.mtccs.collectCanvas();

                    this.log.writeStatusLine();

                    this.state = DECIDING_WHAT_TO_DO;

                    break;

                case PRESENTING_THE_REPORT:
                    System.out.println("Lady Master Thief is PRESENTING_THE_REPORT");
                    this.log.writeEnd();
                    heistOver = true;
                    break;
            }   
        }
    }

}
