/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenRepOfInfo;

import com.sun.javafx.binding.Logging;
import monitors.*;
import static GenRepOfInfo.Heist.*;
import entities.Thief;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private static IMasterThiefCtrlCollSite mtccs;
    private static IOrdThievesConcSite cs;
    private static IAssaultParty[] ap = new AssaultParty[THIEVES_NUMBER/MAX_ASSAULT_PARTY_THIEVES];
    private static IMuseum museum;
    
    private static Thief[] thieves = new Thief[THIEVES_NUMBER];

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
     * @param mtccs
     * @param cs
     * @param ap
     * @param museum
     * @return log instance, this is a singleton.
     */
    public synchronized static Log getInstance(IMasterThiefCtrlCollSite mtccs, IOrdThievesConcSite cs, IAssaultParty[] ap, IMuseum museum, Thief [] thieves) {
        Log.mtccs = mtccs;
        Log.cs = cs;
        Log.ap = ap;
        Log.museum = museum;
        Log.thieves = thieves;
        return instance;
    }

    /**
     * This method writes the head of the logging file.
     */
    private void writeInit() {
        try {
            pw = new PrintWriter(log);
            pw.println("                               Heist to the Museum - Description of the internal state");
            pw.println("MstT   Thief 1     Thief 2     Thief 3     Thief 4     Thief 5     Thief 6");
            pw.println("Stat  Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD");
            pw.println("                  Assault party 1                    Assault party 2                          Museum");
            pw.println("           Elem 1     Elem 2    Elem 3        Elem 1    Elem 2    Elem 3       Room 1  Room 2  Room 3  Room 4  Room 5");
            pw.println("    RId  Id Pos Cv  Id Pos Cv Id Pos Cv RId Id Pos Cv Id Pos Cv Id Pos Cv       NP DT   NP DT   NP DT   NP DT   NP DT");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method will be called to finish write the logging file.
     */
    public synchronized void writeStatusLine() {
        //master thief
        pw.print(Log.mtccs.getMasterthief().getMasterState() + "     ");

        //AP 0
        //thief 0
        pw.print(thieves[0].getThiefState() + " ");
        String situation;
        if (thieves[0].getAP() == null) {
            situation = "W";
        } else {
            situation = "P";
        }
        pw.print(situation + " ");
        pw.print(thieves[0].getMaxDisp() + "    ");
        
        //thief 1
        pw.print(thieves[1].getThiefState() + " ");
        if (thieves[1].getAP() == null) {
            situation = "W";
        } else {
            situation = "P";
        }
        pw.print(situation + " ");
        pw.print(thieves[1].getMaxDisp() + "    ");
        
        //thief 2
        pw.print(thieves[2].getThiefState() + " ");
        if (thieves[2].getAP() == null) {
            situation = "W";
        } else {
            situation = "P";
        }
        pw.print(situation + " ");
        pw.print(thieves[2].getMaxDisp() + "    ");
        
        //AP 1
        //thief 0
        pw.print(thieves[3].getThiefState() + " ");
        if (thieves[3].getAP() == null) {
            situation = "W";
        } else {
            situation = "P";
        }
        pw.print(situation + " ");
        pw.print(thieves[3].getMaxDisp() + "    ");
        
        //thief 1
        pw.print(thieves[4].getThiefState() + " ");
        if (thieves[4].getAP() == null) {
            situation = "W";
        } else {
            situation = "P";
        }
        pw.print(situation + " ");
        pw.print(thieves[4].getMaxDisp() + "    ");
        
        //thief 2
        pw.print(thieves[5].getThiefState() + " ");
        if (thieves[5].getAP() == null) {
            situation = "W";
        } else {
            situation = "P";
        }
        pw.print(situation + " ");
        pw.println(thieves[5].getMaxDisp() + "    ");
        
        
        
        //AP0
        pw.print("     ");
        pw.print(this.ap[0].getRoom().getId() + "    ");
        // thief 0
        pw.print(this.ap[0].getThieves()[0].getThiefid() + "  ");
        pw.print(this.ap[0].getThieves()[0].getPosition() + "  ");
        if(this.ap[0].getThieves()[0].isHasCanvas()){
            situation = "1";
        } else {
            situation = "0";
        }
        
        pw.print(situation + "   ");
        
        // thief 1
        pw.print(this.ap[0].getThieves()[1].getThiefid() + "  ");
        pw.print(this.ap[0].getThieves()[1].getPosition() + "  ");
        if(this.ap[0].getThieves()[1].isHasCanvas()){
            situation = "1";
        } else {
            situation = "0";
        }
        
        pw.print(situation + "   ");
        
        // thief 2
        pw.print(this.ap[0].getThieves()[2].getThiefid() + "  ");
        pw.print(this.ap[0].getThieves()[2].getPosition() + "  ");
        if(this.ap[0].getThieves()[2].isHasCanvas()){
            situation = "1";
        } else {
            situation = "0";
        }
        
        pw.print(situation + "   ");
        
        //AP1
        pw.print("     ");
        pw.print(this.ap[1].getRoom().getId() + "    ");
        // thief 0
        pw.print(this.ap[1].getThieves()[0].getThiefid() + "  ");
        pw.print(this.ap[1].getThieves()[0].getPosition() + "  ");
        if(this.ap[1].getThieves()[0].isHasCanvas()){
            situation = "1";
        } else {
            situation = "0";
        }
        
        pw.print(situation + "   ");
        
        // thief 1
        pw.print(this.ap[1].getThieves()[1].getThiefid() + "  ");
        pw.print(this.ap[1].getThieves()[1].getPosition() + "  ");
        if(this.ap[1].getThieves()[1].isHasCanvas()){
            situation = "1";
        } else {
            situation = "0";
        }
        
        pw.print(situation + "   ");
        
        // thief 2
        pw.print(this.ap[1].getThieves()[2].getThiefid() + "  ");
        pw.print(this.ap[1].getThieves()[2].getPosition() + "  ");
        if(this.ap[1].getThieves()[2].isHasCanvas()){
            situation = "1";
        } else {
            situation = "0";
        }
        
        pw.print(situation + "   ");
        
        //MUSEUM
        
        pw.print(this.museum.getRooms()[0].getNPaintings() + " ");
        pw.print(this.museum.getRooms()[0].getDistance() + "  ");
        
        pw.print(this.museum.getRooms()[1].getNPaintings() + " ");
        pw.print(this.museum.getRooms()[1].getDistance() + "  ");
        
        pw.print(this.museum.getRooms()[2].getNPaintings() + " ");
        pw.print(this.museum.getRooms()[2].getDistance() + "  ");
        
        pw.print(this.museum.getRooms()[3].getNPaintings() + " ");
        pw.print(this.museum.getRooms()[3].getDistance() + "  ");
        
        pw.print(this.museum.getRooms()[4].getNPaintings() + " ");
        pw.println(this.museum.getRooms()[4].getDistance() + "  ");

    }

    public synchronized void writeEnd() {
        pw.println("My friends, tonight's effort produced " + this.mtccs.getTotalPaintings() + " priceless paintings!\n");
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
