package clientSide;

import static auxiliary.Heist.*;
import clientSide.AssaultThief;
import genclass.*;
import interfaces.APInterface;
import interfaces.CCSInterface;
import interfaces.CSInterface;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import structures.RegistryConfig;

/**
 * Client for AssaultThief
 */
public class ThiefClient {

    /**
     * Programa principal.
     *
     */
    public static void main(String[] args) {
        //int nIter;   // número de iterações do ciclo de vida dos clientes

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Heist to the Museum - AssaultThieves\n");
        //GenericIO.writeString("Numero de iterações? ");
        //nIter = N_ITER;
        //GenericIO.writeString("Nome do ficheiro de logging? ");
        //GenericIO.writeString("Nome do sistema computacional onde está o servidor? ");
        //serverHostName = InetAddress.getLocalHost().getHostName();
        
        // nome do sistema onde está localizado o serviço de registos RMI
        String rmiRegHostName;
        // port de escuta do serviço
        int rmiRegPortNumb;

        RegistryConfig rc = new RegistryConfig("config.ini");
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();

        interfaces.CSInterface cs = null;
        interfaces.CCSInterface ccs = null;
        interfaces.APInterface ap[] = null;
        interfaces.MuseumInterface museum = null;
        interfaces.LoggerInterface log = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            log = (LoggerInterface) registry.lookup(RegistryConfig.loggerNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating log: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("log is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            cs = (CSInterface) registry.lookup(RegistryConfig.CSNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating cs: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("cs is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            ccs = (CCSInterface) registry.lookup(RegistryConfig.CCSNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating ccs: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ccs is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            museum = (MuseumInterface) registry.lookup(RegistryConfig.museumNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating museum: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("museum is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            ap[0] = (APInterface) registry.lookup(RegistryConfig.AP0NameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating ap0: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ap0 is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }
        
        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            ap[1] = (APInterface) registry.lookup(RegistryConfig.AP1NameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating ap1: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ap1 is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        AssaultThief thieves[] = new AssaultThief[THIEVES_NUMBER];

        for (int i = 0;
                i < THIEVES_NUMBER;
                i++) {
            thieves[i] = new AssaultThief(i, cs, ccs, ap, museum, log);
        }

        /* Arranque da simulação */
        for (int i = 0;
                i < THIEVES_NUMBER;
                i++) {
            thieves[i].start();
        }

        /* Aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0;
                i < THIEVES_NUMBER;
                i++) {
            try {
                thieves[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O thief " + i + " terminou.");
        }

        GenericIO.writelnString();

    }
}
