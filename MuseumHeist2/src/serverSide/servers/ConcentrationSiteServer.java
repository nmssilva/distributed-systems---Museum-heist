package serverSide.servers;

import genclass.GenericIO;
import serverSide.com.ClientProxy;
import serverSide.ConcentrationSite;
import serverSide.Interfaces.ConcentrationSiteInterface;
import serverSide.com.ServerCom;

public class ConcentrationSiteServer
{
  /**
   *  Número do port de escuta do serviço a ser prestado
   *
   *    @serialField portNumb
   */

   private static final int portNumb = 4001;

  /**
   *  Programa principal.
     * @param args
   */

   public static void main (String [] args)
   {
      ConcentrationSite cs;                         
      ConcentrationSiteInterface iConcentrationSite;                     
      ServerCom scon, sconi;                        // canais de comunicação
      ClientProxy cliProxy;                         // thread agente prestador do serviço

     /* estabelecimento do servico */

      scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      cs = new ConcentrationSite();                             // activação do serviço
      iConcentrationSite = new ConcentrationSiteInterface(cs);  // activação do interface com o serviço
      GenericIO.writelnString("CS - PORT " + portNumb);
      GenericIO.writelnString ("O serviço foi estabelecido!");
      GenericIO.writelnString ("O servidor esta em escuta.");

     /* processamento de pedidos */

      while (true)
      { sconi = scon.accept ();                            // entrada em processo de escuta
        cliProxy = new ClientProxy (sconi, iConcentrationSite);    // lançamento do agente prestador do serviço
        cliProxy.start ();
      }
   }
}