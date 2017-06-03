package serverSide;

import static auxiliary.Heist.PORT_CS;
import auxiliary.VectorTimestamp;
import interfaces.RegisterInterface;
import interfaces.APInterface;
import interfaces.CCSInterface;
import interfaces.CSInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import serverSide.ConcentrationSite;
import registry.RegistryConfig;

public class ConcentrationSiteServer {

    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */
    private static final int portNumb = PORT_CS;
    
    private VectorTimestamp clocks;

    /**
     * Programa principal.
     */
    public static void main(String[] args) {

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

        CSInterface csi = null;
        CCSInterface ccs = null;
        APInterface ap[] = new APInterface[auxiliary.Heist.MAX_ASSAULT_PARTIES];

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            ccs = (CCSInterface) registry.lookup(RegistryConfig.REGISTRY_COLLECTION_SITE_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating ccs: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ccs is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            ap[0] = (APInterface) registry.lookup(RegistryConfig.REGISTRY_ASSAULT_PARTY0_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating ap0: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ap0 is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            ap[1] = (APInterface) registry.lookup(RegistryConfig.REGISTRY_ASSAULT_PARTY1_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating ap1: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ap1 is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        ConcentrationSite cs = new ConcentrationSite(ccs, ap);
        try {
            csi = (CSInterface) UnicastRemoteObject.exportObject(cs, RegistryConfig.REGISTRY_CONCENTRATION_SITE_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o CS: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O stub para o CS foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_CONCENTRATION_SITE_NAME;
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
            reg.bind(nameEntryObject, csi);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do CS: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O CS já está registado: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O CS foi registado!");

    }
}
