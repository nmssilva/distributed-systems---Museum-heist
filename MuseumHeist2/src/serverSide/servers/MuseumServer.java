package serverSide.servers;

import genclass.GenericIO;
import serverSide.com.ClientProxy;
import serverSide.Interfaces.MuseumInterface;
import serverSide.Museum;
import serverSide.com.ServerCom;

public class MuseumServer {

    /**
     * Número do port de escuta do serviço a ser prestado
     *
     * @serialField portNumb
     */
    private static final int portNumb = 4005;

    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        Museum museum;
        MuseumInterface iMuseum;
        ServerCom scon, sconi;                          // canais de comunicação
        ClientProxy cliProxy;                           // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                 // criação do canal de escuta e sua associação
        scon.start();                                   // com o endereço público
        museum = new Museum();                          // activação do serviço
        iMuseum = new MuseumInterface(museum);          // activação do interface com o serviço
        
        GenericIO.writelnString("MUSEUM - PORT " + portNumb);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iMuseum);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
