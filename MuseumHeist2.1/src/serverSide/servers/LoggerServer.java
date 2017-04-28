package serverSide.servers;

import genclass.GenericIO;
import serverSide.com.ClientProxy;
import serverSide.Interfaces.LoggerInterface;
import serverSide.Logger;
import serverSide.com.ServerCom;

public class LoggerServer {

    /**
     * Número do port de escuta do serviço a ser prestado
     *
     * @serialField portNumb
     */

    private static final int portNumb = 4000;

    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        Logger logger;
        LoggerInterface iLogger;
        ServerCom scon, sconi;                        // canais de comunicação
        ClientProxy cliProxy;                         // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                 // criação do canal de escuta e sua associação
        scon.start();                                   // com o endereço público
        logger = new Logger();                          // activação do serviço
        iLogger = new LoggerInterface(logger);          // activação do interface com o serviço
        GenericIO.writelnString("LOGGER - PORT " + portNumb);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                          // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iLogger);     // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
