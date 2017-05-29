package serverSide;

import static auxiliary.Heist.PORT_LOG;
import interfaces.RegisterInterface;
import interfaces.LoggerInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import serverSide.Logger;
import structures.RegistryConfig;

public class LoggerServer {

    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */
    private static final int portNumb = PORT_LOG;

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
        
        LoggerInterface li = null;
        Logger logger = new Logger();
        
        try {
            li = (LoggerInterface) UnicastRemoteObject.exportObject(logger, rc.loggerPort());
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o bench: " + e.getMessage());
            System.exit(1);
        }
        
        System.out.println("O stub para o logger foi gerado!");
        
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
            reg.bind(nameEntryObject, li);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do log: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O log já está registado: " + e.getMessage());
            System.exit(1);
        }
        
        System.out.println("O logger foi registado!");
        
    }
}
