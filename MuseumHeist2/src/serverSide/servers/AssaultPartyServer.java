package serverSide.servers;

import static auxiliary.Heist.PORT_AP;
import genclass.GenericIO;
import serverSide.AssaultParty;
import serverSide.com.ClientProxy;
import serverSide.Interfaces.AssaultPartyInterface;
import serverSide.com.ServerCom;

public class AssaultPartyServer
{
  /**
   *  Número do port de escuta do serviço a ser prestado
   *
   *    @serialField portNumb
   */

   private static final int portNumb0 = PORT_AP;
   private static final int portNumb1 = PORT_AP+1;

  /**
   *  Programa principal.
     * @param args
   */

   public static void main (String [] args)
   {
      AssaultParty ap0,ap1;                         
      AssaultPartyInterface iAssaultParty0, iAssaultParty1;                     
      ServerCom scon0, sconi0, scon1, sconi1;       // canais de comunicação
      ClientProxy cliProxy0, cliProxy1;             // thread agente prestador do serviço

     /* estabelecimento do servico */

      scon0 = new ServerCom (portNumb0);
      scon1 = new ServerCom (portNumb1);// criação do canal de escuta e sua associação
      scon0.start ();                                   // com o endereço público
      scon1.start ();                                   // com o endereço público
      ap0 = new AssaultParty(0);                        // activação do serviço
      ap1 = new AssaultParty(1);                        // activação do serviço
      iAssaultParty0 = new AssaultPartyInterface(ap0);  // activação do interface com o serviço
      iAssaultParty1 = new AssaultPartyInterface(ap1);  // activação do interface com o serviço
      GenericIO.writelnString("AP0 - PORT " + portNumb0);
      GenericIO.writelnString("AP1 - PORT " + portNumb1);
      GenericIO.writelnString ("O serviço foi estabelecido!");
      GenericIO.writelnString ("O servidor esta em escuta.");

     /* processamento de pedidos */

      while (true)
      { sconi0 = scon0.accept ();                               // entrada em processo de escuta
        cliProxy0 = new ClientProxy (sconi0, iAssaultParty0);   // lançamento do agente prestador do serviço
        cliProxy0.start ();
        
        sconi1 = scon1.accept ();                               // entrada em processo de escuta
        cliProxy1 = new ClientProxy (sconi1, iAssaultParty1);   // lançamento do agente prestador do serviço
        cliProxy1.start ();
      }
   }
}