package serverSide.com;

import serverSide.interfaces.Interface;
import auxiliary.messages.*;
import genclass.GenericIO;

public class ClientProxy extends Thread {

    private static int nProxy;

    private ServerCom sconi;
    private Interface inter;

    public ClientProxy(ServerCom sconi, Interface inter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.inter = inter;
    }

    @Override
    public void run() {
        Message inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject();                     // ler pedido do cliente
        try {
            outMessage = inter.processAndReply(inMessage);         // processá-lo
        } catch (MessageException e) {

            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                // enviar resposta ao cliente
        sconi.close();                                                // fechar canal de comunicação
    }

    private static int getProxyId() {
        Class<serverSide.com.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<serverSide.com.ClientProxy>) Class.forName("serverSide.com.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("O tipo de dados ConcentrationSiteProxy não foi encontrado!");
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
