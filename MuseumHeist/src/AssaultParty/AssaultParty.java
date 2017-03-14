/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AssaultParty;

import Entities.Thief;
import GenRepOfInfo.MemFIFO;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 *
 *
 */
public class AssaultParty {

    private Thief[] party;
    /* grupo de ladroes */
    // declaração de variaveis
    private final int assaultPartyId;
    private MemFIFO elements;
    private final int maxElements;
    private static int id = -1;

    public AssaultParty(int nelem) {
        this.assaultPartyId = id++;
        this.maxElements = nelem;
        this.elements = new MemFIFO(nelem);
    }

    // procedimentos do monitor
    public synchronized boolean prepareExcursion(int roomID) {
        return true;
    }

    public boolean addThief(Thief thief) {
        if (!elements.empty()) {
            elements.write(thief);
            return true;
        }
        return false;
    }

    public void crawlIn() {
    }

    public void reverseDirection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void crawlOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
