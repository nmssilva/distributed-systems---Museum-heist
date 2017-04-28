package serverSide.interfaces;

import auxiliary.messages.*;
import serverSide.ConcentrationSite;
import static auxiliary.messages.Message.*;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */
public class ConcentrationSite_Interface extends Interface {

    /**
     * Concentration site (represents the provided service)
     *
     * @serialField cs
     */
    private ConcentrationSite cs;

    /**
     * Concentration site interface instantiation
     *
     * @param cs concentration site
     */
    public ConcentrationSite_Interface(ConcentrationSite cs) {
        this.cs = cs;
    }

    /**
     * Processamento das mensagens através da execução da tarefa correspondente.
     * Geração de uma mensagem de resposta.
     *
     * @param inMessage mensagem com o pedido
     *
     * @return mensagem de resposta
     *
     * @throws MessageException se a mensagem com o pedido for considerada
     * inválida
     */
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                           // mensagem de resposta

        /* seu processamento */
        if (inMessage.getType() == AMINEEDED) {
            System.out.println("CS - AMINEEDED");
            int partyID = cs.amINeeded(inMessage.getInteger(), inMessage.getInteger2());
            
            outMessage = new Message(Message.ACK, partyID);
        }
        if (inMessage.getType() == STARTOP) {
            System.out.println("CS - STARTOP");
            cs.startOfOperations();
            outMessage = new Message(Message.ACK, 2);
        }
        if (inMessage.getType() == PREPARE_AP) {
            System.out.println("CS - PREPARE AP");
            cs.prepareAssaultParty();
            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == GET_ASSAULT_THIEVES_CS) {
            System.out.println("CS - GET ASSAULT THIEVES CS");
            int thievesCS = cs.getnAssaultThievesCS();
            outMessage = new Message(Message.ACK, thievesCS);
        }
        return outMessage;
    }
}
