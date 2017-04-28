/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.monitors;

import interfaces.IConcentrationSite;

/**
 *
 * @author Nuno Silva
 */
public class ConcentrationSite_Client implements IConcentrationSite {

    private String host;
    private int port;

    public ConcentrationSite_Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean amINeeded() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getnAssaultThievesCS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepareAssaultParty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startOfOperations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
