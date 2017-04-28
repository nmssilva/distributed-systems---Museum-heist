package serverSide.servers;

import genclass.*;
import serverSide.AssaultParty;
import serverSide.com.ClientProxy;
import serverSide.interfaces.IAssaultParty;
import serverSide.com.ServerCom;
import static auxiliary.constants.Heist.PORT_AP;

public class AssaultParty0Server {

    public static void main(String[] args) {
        AssaultParty assaultParty0;
        IAssaultParty iAssaultParty0;
        ServerCom scon0, sconi0;
        ClientProxy assaultPartyProxy0;

        /* estabelecimento do servico */
        scon0 = new ServerCom(PORT_AP);
        scon0.start();

        assaultParty0 = new AssaultParty(0);

        iAssaultParty0 = new IAssaultParty(assaultParty0);

        GenericIO.writelnString("AP#0 - PORT " + PORT_AP);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        while (true) {
            sconi0 = scon0.accept();
            assaultPartyProxy0 = new ClientProxy(sconi0, iAssaultParty0);
            assaultPartyProxy0.start();
        }
    }
}
