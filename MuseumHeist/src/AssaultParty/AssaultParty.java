/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AssaultParty;

import Thieves.Thief;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 * 
 * 
 */
public class AssaultParty {
    
    private int nelem;  /* numero de elementos da assault party */
    private Thief[] party; /* grupo de ladroes */

    public AssaultParty(int nelem) {
        this.nelem = nelem;
        this.party = new Thief[nelem];
    }
    
    public Thief[] getParty(){
        return party;
    }
    
    
}
