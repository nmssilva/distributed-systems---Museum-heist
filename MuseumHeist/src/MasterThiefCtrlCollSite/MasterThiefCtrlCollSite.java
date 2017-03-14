/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MasterThiefCtrlCollSite;

/**
 *
 * @author Nuno Silva
 */
public class MasterThiefCtrlCollSite {

    // declaração de variáveis
    private boolean heistEnd = false;

    // construtor
    public MasterThiefCtrlCollSite() {

    }

    // procedimentos do monitor
    public synchronized boolean startOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized boolean sendAssaultParty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized boolean waitForHandACanvas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized boolean collectCanvas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized boolean sumUpResults() {
        //if()  //missing conditions
        return this.heistEnd;
    }

    public void handCanvas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
