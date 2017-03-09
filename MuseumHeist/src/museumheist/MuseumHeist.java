/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MuseumHeist;

import Logger.Log;
import MasterThief.MasterThief;
import Thieves.Thief;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 */

public class MuseumHeist {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        final int   K = 3,      /* Número de elementos da assault party */
                    M = 7,      /* Número máximo de peças de bagagem por passageiro */
                    N = 5,      /* Número de rooms */
                    Q = 3;      /* Número de paintings */
        
        Log loging;
        MasterThief masterThief;    /* Ladroa mestre */
        
        Thief[] thieves = new Thief[M-1];   /* Array de ladrões exluindo o master thief (M-1) */
        
    }
    
}
