/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.monitors;

import interfaces.IMuseum;
import serverSide.Room;

/**
 *
 * @author Nuno Silva
 */
public class Museum_Client implements IMuseum{
    
    private String host;
    private int port;

    public Museum_Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Room getRoom(int roomID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int rollACanvas(int nRoom) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
