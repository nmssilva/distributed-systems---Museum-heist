/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all the constants related with the Registry.
 * @author António Ferreira, 67405; Rodrigo Cunha, 67800
 */
public class RegistryConfig {
    /**
     * Logger name entry on the registry.
     */
    public static String loggerNameEntry = "LoggerInt";
    
    /**
     * Museum name entry on the registry.
     */
    public static String museumNameEntry = "MuseumInt";

    /**
     * Concentration Site name entry on the registry.
     */
    public static String CSNameEntry = "CSInt";

    /**
     * CCS name entry on the registry.
     */
    public static String CCSNameEntry = "CCSInt";
    
    
    /**
     * AP0 name entry on the registry.
     */
    public static String AP0NameEntry = "AP0Int";
    
    
    /**
     * AP1 name entry on the registry.
     */
    public static String AP1NameEntry = "AP1Int";

    /**
     * RegisterHandler name entry on the registry.
     */
    public static String registerHandler = "RegisterHandler";
    
    /**
     * Bash property of the file.
     */
    private Properties prop;

    /**
     * Constructor that receives the file with the configurations.
     * @param filename path for the configuration file
     */
    public RegistryConfig(String filename) {
        prop = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(filename);
            prop.load(in);
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(RegistryConfig.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(RegistryConfig.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
    }
    
    /** 
     * Loads the parameter REGISTER_HOST from the configuration file.
     * @return parameter value
     */
    public String registryHost() {
        return prop.getProperty("registry_host");
    }
    
    /** 
     * Loads the parameter REGISTER_PORT from the configuration file.
     * @return parameter value
     */
    public int registryPort() {
        return Integer.parseInt(prop.getProperty("registry_port"));
    }
    /** 
     * Loads the parameter REGISTER_OBJECT_PORT from the configuration file.
     * @return parameter value
     */
    public int objectPort() {
        return Integer.parseInt(prop.getProperty("registryobject"));
    }
    /** 
     * Loads the parameter LOGGER_PORT from the configuration file.
     * @return parameter value
     */
    public int loggerPort() {
        return Integer.parseInt(prop.getProperty("logger_port"));
    }
        /** 
     * Loads the parameter MUSEUM_PORT from the configuration file.
     * @return parameter value
     */
    public int museumPort() {
        return Integer.parseInt(prop.getProperty("museum_port"));
    }
    /** 
     * Loads the parameter CCS_PORT from the configuration file.
     * @return parameter value
     */
    public int ccsPort() {
        return Integer.parseInt(prop.getProperty("ccs_port"));
    }
    /** 
     * Loads the parameter CS_PORT from the configuration file.
     * @return parameter value
     */
    public int csPort() {
        return Integer.parseInt(prop.getProperty("cs_port"));
    }
        /** 
     * Loads the parameter AP0_PORT from the configuration file.
     * @return parameter value
     */
    public int ap0Port() {
        return Integer.parseInt(prop.getProperty("ap0_port"));
    }
    /** 
     * Loads the parameter AP1_PORT from the configuration file.
     * @return parameter value
     */
    public int ap1Port() {
        return Integer.parseInt(prop.getProperty("ap1_port"));
    }
}