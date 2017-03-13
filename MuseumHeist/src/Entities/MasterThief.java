/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */

public class MasterThief extends Thread{ //implements IMasterThief
    
    private final static int PLANNING_HEIST = 0,
                            DECIDING_WHAT_DO = 1,
                            ASSEMBLE_GROUP = 2,
                            WAITING_ARRIVAL = 3,
                            PRESENT_REPORT = 4;
    
    private int state = PLANNING_HEIST; 
}
