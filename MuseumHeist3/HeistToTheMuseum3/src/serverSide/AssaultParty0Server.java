package serverSide;

import interfaces.RegisterInterface;
import interfaces.APInterface;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import registry.RegistryConfig;

public class AssaultParty0Server {

    public static void main(String[] args) throws RemoteException {
          // nome do sistema onde está localizado o serviço de registos RMI
        String rmiRegHostName;

        // port de escuta do serviço
        int rmiRegPortNumb;
        
        rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        
         /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        APInterface api = null;
        MuseumInterface museum = null;
        LoggerInterface log = null;
        
        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            museum = (MuseumInterface) registry.lookup(RegistryConfig.REGISTRY_MUSEUM_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating museum: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("museum is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }
        
        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            log = (LoggerInterface) registry.lookup(RegistryConfig.REGISTRY_LOGGER_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating museum: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("museum is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }
        
        AssaultParty ap = new AssaultParty(0,museum,log);
        try {
            api = (APInterface) UnicastRemoteObject.exportObject(ap, RegistryConfig.REGISTRY_ASSAULT_PARTY_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o AP0: " + e.getMessage());
            System.exit(1);
        }
        
        System.out.println("O stub para o AP0 foi gerado!");
        
                 /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_ASSAULT_PARTY0_NAME;
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
            System.out.println("Excepção no registo do AP0: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O AP0 já está registado: " + e.getMessage());
            System.exit(1);
        }
        
        System.out.println("O AP0 foi registado!");
    }
}
