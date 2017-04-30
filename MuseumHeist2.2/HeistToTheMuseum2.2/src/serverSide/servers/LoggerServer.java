package serverSide.servers;

import static auxiliary.constants.Heist.PORT_LOG;
import genclass.*;
import serverSide.com.ClientProxy;
import serverSide.interfaces.Logger_Interface;
import serverSide.Logger;
import serverSide.com.ServerCom;

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
        Logger logger;                               
        Logger_Interface iLogger;                 
        ServerCom scon, sconi;                               // canais de comunicação
        ClientProxy cliProxy;                                // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        logger = new Logger();                           // activação do serviço
        iLogger = new Logger_Interface(logger);        // activação do interface com o serviço

        GenericIO.writelnString("LOG - PORT " + PORT_LOG);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iLogger);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
