package serverSide;

import static auxiliary.Heist.*;
import genclass.GenericIO;

/**
 *   Este tipo de dados simula uma solução do lado do servidor do Problema dos Barbeiros Sonolentos que implementa o
 *   modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */

public class ServerHeist
{
  /**
   *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
   *
   *    @serialField portNumb
   */

   private static final int portNumb = 4000;

  /**
   *  Programa principal.
   */

   public static void main (String [] args)
   {
      Museum museum;                                       // Museum
      ConcentrationSite cs;                                // Concentration Site
      ControlCollectionSite ccs;                           // Control Collection Site
      Logger log;                                          // Logger
      AssaultParty assaultparty[];                         // Assault Parties
              
      BarberShopInterface bShopInter;                      // interface à barbearia
      ServerCom scon, sconi;                               // canais de comunicação
      ClientProxy cliProxy;                                // thread agente prestador do serviço

     /* estabelecimento do servico */

      scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      
      log = new Logger();
      
      museum = new Museum(log);
      assaultparty = new AssaultParty[MAX_ASSAULT_PARTIES];
      for(int i = 0; i < MAX_ASSAULT_PARTIES; i++){
          assaultparty[i] = new AssaultParty(i, museum, log);
      }
      ccs = new ControlCollectionSite(assaultparty, log);
      cs = new ConcentrationSite(ccs, assaultparty, log);
      
      
      bShopInter = new BarberShopInterface (bShop);        // activação do interface com o serviço
      GenericIO.writelnString ("O serviço foi estabelecido!");
      GenericIO.writelnString ("O servidor esta em escuta.");

     /* processamento de pedidos */

      while (true)
      { sconi = scon.accept ();                            // entrada em processo de escuta
        cliProxy = new ClientProxy (sconi, bShopInter);    // lançamento do agente prestador do serviço
        cliProxy.start ();
      }
   }
}
