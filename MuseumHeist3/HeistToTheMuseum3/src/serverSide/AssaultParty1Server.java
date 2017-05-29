package serverSide;

import serverSide.AssaultParty;
import interfaces.RegisterInterface;
import interfaces.APInterface;
import interfaces.MuseumInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import structures.RegistryConfig;

public class AssaultParty1Server {

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

        APInterface api = null;
        MuseumInterface museum = null;

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
        
        AssaultParty ap = new AssaultParty(1, museum);
        
        try {
            api = (APInterface) UnicastRemoteObject.exportObject(ap, rc.ap1Port());
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o bench: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O stub para o bench foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.AP1NameEntry;
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
            reg.bind(nameEntryObject, api);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do AP1: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O AP1 já está registado: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O AP1 foi registado!");
    }
}
