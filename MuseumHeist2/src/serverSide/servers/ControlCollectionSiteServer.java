package serverSide.servers;

import auxiliary.Heist;
import genclass.GenericIO;
import serverSide.com.ClientProxy;
import serverSide.ControlCollectionSite;
import serverSide.Interfaces.ControlCollectionSiteInterface;
import serverSide.com.ServerCom;

public class ControlCollectionSiteServer {

    /**
     * Número do port de escuta do serviço a ser prestado
     *
     * @serialField portNumb
     */
    private static final int portNumb = Heist.PORT_CCS;

    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        ControlCollectionSite ccs;
        ControlCollectionSiteInterface iControlCollectionSite;
        ServerCom scon, sconi;                        // canais de comunicação
        ClientProxy cliProxy;                         // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        ccs = new ControlCollectionSite();                             // activação do serviço
        iControlCollectionSite = new ControlCollectionSiteInterface(ccs);  // activação do interface com o serviço

        GenericIO.writelnString("CCS - PORT " + portNumb);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iControlCollectionSite);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
