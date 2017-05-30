package clientSide;

import genclass.*;
import interfaces.LoggerInterface;
import interfaces.CSInterface;
import interfaces.CCSInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import registry.RegistryConfig;

/**
 * Client for MasterThief
 */
public class MasterClient {

    /**
     * Programa principal.
     * @param args
     */
    public static void main(String[] args) {
        
        GenericIO.writelnString("\n" + "      Heist to the Museum - Master Thief\n");
        
        // nome do sistema onde está localizado o serviço de registos RMI
        String rmiRegHostName;
        // port de escuta do serviço
        int rmiRegPortNumb;
        
        rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        
        String fName = "logger.log";                                       // nome do ficheiro de logging

        LoggerInterface loggerInt = null;
        CSInterface csInt = null;
        CCSInterface ccsInt = null;
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            loggerInt = (LoggerInterface) registry.lookup (RegistryConfig.REGISTRY_LOGGER_NAME);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating logger: " + e.getMessage () + "!");
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("logger is not registered: " + e.getMessage () + "!");
            System.exit(1);
        }
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            csInt = (CSInterface) registry.lookup (RegistryConfig.REGISTRY_CONCENTRATION_SITE_NAME);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating CS: " + e.getMessage () + "!");
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("CS is not registered: " + e.getMessage () + "!");
            System.exit(1);
        }
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            ccsInt = (CCSInterface) registry.lookup (RegistryConfig.REGISTRY_COLLECTION_SITE_NAME);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating CCS: " + e.getMessage () + "!");
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("CCS is not registered: " + e.getMessage () + "!");
            System.exit(1);
        }
        
        
        // init MT
        MasterThief mthief = new MasterThief(loggerInt,csInt,ccsInt);
        
        /* Arranque da simulação */
        mthief.start();
        /* Aguardar o fim da simulação */
        GenericIO.writelnString();
        while (mthief.isAlive()) {
            //mthief.sendInterrupt();
            Thread.yield();
        }
        try {
            mthief.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("O masterthief terminou.");

        GenericIO.writelnString();

    }
}
