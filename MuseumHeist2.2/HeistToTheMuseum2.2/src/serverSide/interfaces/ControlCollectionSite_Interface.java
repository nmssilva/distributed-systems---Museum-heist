package serverSide.interfaces;

import auxiliary.messages.*;
import serverSide.ControlCollectionSite;
import static auxiliary.messages.Message.*;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */
public class ControlCollectionSite_Interface extends Interface {

    /**
     * Concentration site (represents the provided service)
     *
     * @serialField cs
     */
    private ControlCollectionSite ccs;

    /**
     * Concentration site interface instantiation
     *
     * @param ccs concentration site
     */
    public ControlCollectionSite_Interface(ControlCollectionSite ccs) {
        this.ccs = ccs;
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
        if (inMessage.getType() == GET_NEXT_ROOM) {
            System.out.println("CCS - GET NEXT ROOM");
            int nextRoom = ccs.getNextRoom();

            outMessage = new Message(Message.ACK, nextRoom);
        }
        if (inMessage.getType() == ISREADY) {
            System.out.println("CCS - ISREADY");
            ccs.isReady();

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == APPRAISE_SIT) {
            System.out.println("CCS - APPRAISE SIT");
            try {
                Thread.sleep((long) (100));
            } catch (InterruptedException e) {
            }
            outMessage = new Message(Message.ACK, ccs.appraiseSit(inMessage.getInteger()));
        }
        if (inMessage.getType() == GET_NEXT_AP) {
            System.out.println("CCS - GET_NEXT_AP");
            int partyID = ccs.getNextParty();

            outMessage = new Message(Message.ACK, partyID);
        }
        if (inMessage.getType() == SENDAP) {
            System.out.println("CCS - SENDAP");
            ccs.sendAssaultParty();

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == PREPAREEXCURSION) {
            System.out.println("CCS - PREPARE EXCURSION");
            ccs.prepareExcursion();

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == HAND_CANVAS) {
            System.out.println("CCS - HAND_CANVAS");
            ccs.handCanvas(inMessage.getInteger(), inMessage.getInteger2(), inMessage.getInteger3());

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == TAKE_A_REST) {
            System.out.println("CCS - TAKE_A_REST");
            ccs.takeARest();

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == COLLECT_CANVAS) {
            System.out.println("CCS - COLLECT_CANVAS");
            ccs.collectCanvas();

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == SUM_UP_RESULTS) {
            System.out.println("CCS - SUM_UP_RESULTS");
            ccs.sumUpResults();

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == NEXT_EMPTY_ROOM) {
            System.out.println("CCS - NEXT_EMPTY_ROOM");
            int nextEmptyRoom = ccs.nextEmptyRoom();

            outMessage = new Message(Message.ACK, nextEmptyRoom);
        }

        return outMessage;
    }
}
