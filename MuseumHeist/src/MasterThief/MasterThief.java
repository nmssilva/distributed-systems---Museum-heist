/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MasterThief;


import Logger.Log;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */

public class MasterThief extends Thread{ //implements IMasterThief
    private MasterThiefState state = MasterThiefState.PLANNING_THE_HEIST;
    private Log log;
    
    private void startOperations(){
        this.state = MasterThiefState.DECIDING_WHAT_TO_DO;
    }
    
    private void prepareAssaultParty(){
        this.state = MasterThiefState.ASSEMBLING_A_GROUP;
        
    }
    
    private void sendAssaultParty(){
        this.state = MasterThiefState.DECIDING_WHAT_TO_DO;
    }
    
    private void appraiseSit(){
        this.state = MasterThiefState.DECIDING_WHAT_TO_DO;
    }
    
    private void takeARest(){        
        this.state = MasterThiefState.WAINTING_FOR_GROUP_ARRIVAL;
    }
    
    private void collectCanvas(){
        this.state = MasterThiefState.DECIDING_WHAT_TO_DO;
    }
    
    private void sumUpResults(){        
        this.state = MasterThiefState.PRESENTING_THE_REPORT;
    }
    
}
