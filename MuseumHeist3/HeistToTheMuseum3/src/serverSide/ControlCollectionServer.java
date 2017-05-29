package serverSide;

import static auxiliary.Heist.PORT_CCS;
import interfaces.RegisterInterface;
import interfaces.APInterface;
import interfaces.CCSInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import serverSide.ControlCollectionSite;
import structures.RegistryConfig;

public class ControlCollectionServer {

    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */
    private static final int portNumb = PORT_CCS;

    /**
     * Programa principal.
     */
    public static void main(String[] args) {
        // nome do sistema onde está localizado o serviço de registos RMI
        String rmiRegHostName;

        // port de escuta do serviço
        int rmiRegPortNumb;

        RegistryConfig rc = new RegistryConfig("config.ini");
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        CCSInterface ccsi = null;
        APInterface ap[] = new APInterface[auxiliary.Heist.MAX_ASSAULT_PARTIES];

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

        ControlCollectionSite ccs = new ControlCollectionSite(ap);
        try {
            ccsi = (CCSInterface) UnicastRemoteObject.exportObject(ccs, rc.ccsPort());
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o bench: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O stub para o bench foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.loggerNameEntry;
        Registry registry = null;
        RegisterInterface reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O registo RMI foi criado!");

        try {
            reg = (RegisterInterface) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, ccsi);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do CCS: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O CCS já está registado: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O CCS foi registado!");
    }
}
