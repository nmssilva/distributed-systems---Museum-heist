/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenRepOfInfo;

import com.sun.javafx.binding.Logging;
import Entities.MasterThiefState;
import Entities.ThievesState;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This log will be the gateway to all the information of the heist
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public class Log {

    private final File log;
    private static PrintWriter pw;
    private static Log instance = null;

    private Log(String filename) {
        if (filename.length() == 0) {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
            filename = "Heistothemuseum_" + date.format(today) + ".log";
        }
        this.log = new File(filename);
    }

    /**
     * This static constructor is used to init the log file, this is a singleton
     * and we need to specify what is the filename, so we created this method
     * that will allow to instantiate the singleton.
     */
    static {
        instance = new Log("");
        instance.writeInit();
    }

    /**
     * This is a singleton, this is important to return the Log instance.
     *
     * @return log intance, this is a singleton.
     */
    public synchronized static Log getInstance() {
        return instance;
    }

    /**
     * This method writes the head of the logging file.
     */
    private void writeInit() {
        try {
            pw = new PrintWriter(log);
            pw.println("                               Heist to the Museum - Description of the internal state");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method will be called to finish write the logging file.
     */
    public synchronized void writeEnd() {
        pw.println("\nLegend:");
        pw.println("MstT Stat – state of the master thief");
        pw.println("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)");
        pw.println("Thief # S – situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)");
        pw.println("Thief # MD – maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6");
        pw.println("Assault party # RId – assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)");
        pw.println("Assault party # Elem # Id – assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)");
        pw.println("Assault party # Elem # Pos – assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)");
        pw.println("Assault party # Elem # Cv – assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)");
        pw.println("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls");
        pw.println("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30");
        pw.flush();
        pw.close();
    }
}
