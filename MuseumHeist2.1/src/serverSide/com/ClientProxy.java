package serverSide.com;

import genclass.GenericIO;
import auxiliary.Message;
import auxiliary.MessageException;
import serverSide.Interfaces.*;

public class ClientProxy extends Thread {

    /**
     * Contador de threads lançados
     *
     * @serialField nProxy
     */

    private static int nProxy;

    /**
     * Canal de comunicação
     *
     * @serialField sconi
     */
    private ServerCom sconi;

    /**
     * Interface genérica
     *
     * @serialField inter
     */
    private Interface inter;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param sconi canal de comunicação
     * @param inter interface
     */
    public ClientProxy(ServerCom sconi, Interface inter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.inter = inter;
    }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        Message inMessage = null, // mensagem de entrada
                outMessage = null;                            // mensagem de saída

        inMessage = (Message) sconi.readObject();            // ler pedido do cliente

        try {
            outMessage = inter.processAndReply(inMessage);   // processá-lo
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
        sconi.writeObject(outMessage);       // enviar resposta ao cliente
        sconi.close();                       // fechar canal de comunicação
    }

    /**
     * Geração do identificador da instanciação.
     *
     * @return identificador da instanciação
     */
    private static int getProxyId() {
        Class<serverSide.com.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<serverSide.com.ClientProxy>) Class.forName("serverSide.com.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("O tipo de dados ClientProxy não foi encontrado!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }
}
