package serverSide.servers;

import auxiliary.constants.Heist;
import genclass.*;
import serverSide.com.ClientProxy;
import serverSide.interfaces.Museum_Interface;
import serverSide.Museum;
import serverSide.com.ServerCom;

public class MuseumServer {

    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */
    private static final int portNumb = Heist.PORT_MUSEUM;

    /**
     * Programa principal.
     */
    public static void main(String[] args) {
        Museum museum;                                    // barbearia (representa o serviço a ser prestado)
        Museum_Interface iMuseum;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        ClientProxy cliProxy;                                // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        museum = new Museum();                           // activação do serviço
        iMuseum = new Museum_Interface(museum);        // activação do interface com o serviço

        GenericIO.writelnString("MUSEUM - PORT " + Heist.PORT_MUSEUM);
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
