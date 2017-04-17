package auxiliary;

import java.io.*;

/**
 *   Este tipo de dados define as mensagens que são trocadas entre os clientes e o servidor numa solução do Problema
 *   dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 *   estático dos threads barbeiro.
 *   A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable
{
  /**
   *  Chave de serialização
   *    @serialField serialVersionUID
   */

   private static final long serialVersionUID = 1001L;

  /* Tipos das mensagens */

  /**
   *  Inicialização do ficheiro de logging (operação pedida pelo cliente)
   *    @serialField SETNFIC
   */

   public static final int SETNFIC  =  1;

  /**
   *  Ficheiro de logging foi inicializado (resposta enviada pelo servidor)
   *    @serialField NFICDONE
   */

   public static final int NFICDONE =  2;

  /**
   *  Corte de cabelo (operação pedida pelo cliente)
   *    @serialField REQCUTH
   */

   public static final int REQCUTH  =  3;

  /**
   *  Cabelo cortado (resposta enviada pelo servidor)
   *    @serialField CUTHDONE
   */

   public static final int CUTHDONE =  4;

  /**
   *  Barbearia cheia (resposta enviada pelo servidor)
   *    @serialField BSHOPF
   */

   public static final int BSHOPF   =  5;

  /**
   *  Alertar o thread barbeiro do fim de operações (operação pedida pelo cliente)
   *    @serialField ENDOP
   */

   public static final int ENDOP    =  6;

  /**
   *  Operação realizada com sucesso (resposta enviada pelo servidor)
   *    @serialField ACK
   */

   public static final int ACK      =  7;

  /**
   *  Mandar o barbeiro dormir (operação pedida pelo cliente)
   *    @serialField GOTOSLP
   */

   public static final int GOTOSLP  =  8;

  /**
   *  Continuação do ciclo de vida do barbeiro (resposta enviada pelo servidor)
   *    @serialField CONT
   */

   public static final int CONT     =  9;

  /**
   *  Terminação do ciclo de vida do barbeiro (resposta enviada pelo servidor)
   *    @serialField END
   */

   public static final int END      = 10;

  /**
   *  Chamar um cliente pelo barbeiro (operação pedida pelo cliente)
   *    @serialField CALLCUST
   */

   public static final int CALLCUST = 11;

  /**
   *  Enviar a identificação do cliente (resposta enviada pelo servidor)
   *    @serialField CUSTID
   */

   public static final int CUSTID   = 12;

  /**
   *  Receber pagamento pelo barbeiro (operação pedida pelo cliente)
   *    @serialField GETPAY
   */

   public static final int GETPAY   = 13;


  /* Campos das mensagens */

  /**
   *  Tipo da mensagem
   *    @serialField msgType
   */

   private int msgType = -1;

  /**
   *  Identificação do cliente
   *    @serialField custId
   */

   private int thiefId = -1;

  /**
   *  Nome do ficheiro de logging
   *    @serialField fName
   */

   private String fName = null;

  /**
   *  Número de iterações do ciclo de vida dos clientes
   *    @serialField nIter
   */

   private int nIter = -1;

  /**
   *  Instanciação de uma mensagem (forma 1).
   *
   *    @param type tipo da mensagem
   */

   public Message (int type)
   {
      msgType = type;
   }

  /**
   *  Instanciação de uma mensagem (forma 2).
   *
   *    @param type tipo da mensagem
   *    @param id identificação do cliente/barbeiro
   */

   public Message (int type, int id)
   {
      msgType = type;
      if ((msgType == REQCUTH) || (msgType == CUSTID))
         thiefId= id;
   }

  /**
   *  Instanciação de uma mensagem (forma 4).
   *
   *    @param type tipo da mensagem
   *    @param name nome do ficheiro de logging
   *    @param nIter número de iterações do ciclo de vida dos clientes
   */

   public Message (int type, String name, int nIter)
   {
      msgType = type;
      fName= name;
      this.nIter = nIter;
   }

  /**
   *  Obtenção do valor do campo tipo da mensagem.
   *
   *    @return tipo da mensagem
   */

   public int getType ()
   {
      return (msgType);
   }

  /**
   *  Obtenção do valor do campo identificador do thief.
   *
   *    @return identificação do thief
   */

   public int getThiefId ()
   {
      return (thiefId);
   }

  /**
   *  Obtenção do valor do campo nome do ficheiro de logging.
   *
   *    @return nome do ficheiro
   */

   public String getFName ()
   {
      return (fName);
   }

  /**
   *  Obtenção do valor do campo número de iterações do ciclo de vida dos clientes.
   *
   *    @return número de iterações do ciclo de vida dos clientes
   */

   public int getNIter ()
   {
      return (nIter);
   }

  /**
   *  Impressão dos campos internos.
   *  Usada para fins de debugging.
   *
   *    @return string contendo, em linhas separadas, a concatenação da identificação de cada campo e valor respectivo
   */

   @Override
   public String toString ()
   {
      return ("Tipo = " + msgType +
              "\nId Thief = " + thiefId +
              "\nNome Fic. Logging = " + fName +
              "\nN. de Iteracoes = " + nIter);
   }
}
