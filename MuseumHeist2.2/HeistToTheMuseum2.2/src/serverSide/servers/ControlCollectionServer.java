package serverSide.servers;

import static auxiliary.constants.Heist.PORT_CCS;
import genclass.*;
import serverSide.com.ClientProxy;
import serverSide.ControlCollectionSite;
import serverSide.interfaces.ControlCollectionSite_Interface;
import serverSide.com.ServerCom;

/**
 * Este tipo de dados simula uma solução do lado do servidor do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro. A
 * comunicação baseia-se em passagem de mensagens sobre sockets usando o
 * protocolo TCP.
 */
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
        ControlCollectionSite CCS;                                    // barbearia (representa o serviço a ser prestado)
        ControlCollectionSite_Interface iCCS;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        ClientProxy cliProxy;                                // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        CCS = new ControlCollectionSite();                           // activação do serviço
        iCCS = new ControlCollectionSite_Interface(CCS);        // activação do interface com o serviço

        GenericIO.writelnString("CCS - PORT " + PORT_CCS);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iCCS);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
