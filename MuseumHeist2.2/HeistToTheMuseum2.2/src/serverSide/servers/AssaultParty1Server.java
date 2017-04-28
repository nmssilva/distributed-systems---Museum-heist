package serverSide.servers;

import genclass.*;
import serverSide.AssaultParty;
import serverSide.com.ClientProxy;
import serverSide.interfaces.IAssaultParty;
import serverSide.com.ServerCom;
import static auxiliary.constants.Heist.PORT_AP;

public class AssaultParty1Server {

    public static void main(String[] args) {
        AssaultParty assaultParty1;
        IAssaultParty iAssaultParty1;
        ServerCom scon1, sconi1;
        ClientProxy assaultPartyProxy1;

        /* estabelecimento do servico */
        scon1 = new ServerCom(PORT_AP +1);
        scon1.start();

        assaultParty1 = new AssaultParty(1);

        iAssaultParty1 = new IAssaultParty(assaultParty1);

        GenericIO.writelnString("AP#1 - PORT " + (PORT_AP+1));
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        while (true) {
            sconi1 = scon1.accept();
            assaultPartyProxy1 = new ClientProxy(sconi1, iAssaultParty1);
            assaultPartyProxy1.start();
        }
    }
}
