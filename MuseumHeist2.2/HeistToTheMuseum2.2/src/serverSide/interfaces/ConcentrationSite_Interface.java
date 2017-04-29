package serverSide.interfaces;

import auxiliary.messages.*;
import serverSide.ConcentrationSite;
import static auxiliary.messages.Message.*;

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
        switch (inMessage.getType()) {
            case AMINEEDED:
                //System.out.println("CS - AMINEEDED");
                int partyID = cs.amINeeded(inMessage.getValue(), inMessage.getValue2());

                outMessage = new Message(Message.ACK, partyID);
                break;
            case STARTOP:
                //System.out.println("CS - STARTOP");
                cs.startOfOperations();
                outMessage = new Message(Message.ACK, 2);
                break;
            case PREPARE_AP:
                //System.out.println("CS - PREPARE AP");
                cs.prepareAssaultParty();
                outMessage = new Message(Message.ACK);
                break;
            case GET_ASSAULT_THIEVES_CS:
                //System.out.println("CS - GET ASSAULT THIEVES CS");
                int thievesCS = cs.getnAssaultThievesCS();
                outMessage = new Message(Message.ACK, thievesCS);
                break;
        }

        return outMessage;
    }
}
