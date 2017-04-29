package serverSide.interfaces;

import auxiliary.messages.*;
import serverSide.Logger;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */
public class Logger_Interface extends Interface {

    /**
     * Logger (represents the provided service)
     *
     * @serialField log
     */
    private final Logger log;

    /**
     * Logger interface instantiation
     *
     * @param log
     */
    public Logger_Interface(Logger log) {
        this.log = log;
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
        Message outMessage = null;                           // Reply message

        /* Received message validation */
        switch (inMessage.getType()) {
            case Message.SETNFIC:
                if ((inMessage.getFName() == null) || (inMessage.getFName().equals(""))) {
                    throw new MessageException("Nome do ficheiro inexistente!", inMessage);
                }
                break;
            case Message.SETREP:
                if ((inMessage.getThiefID() < 0)) {
                    throw new MessageException("Thief ID inválido!!", inMessage);
                }
                break;
        }

        /* Message processing */
        switch (inMessage.getType()) {
            case Message.SETNFIC:                           // Initialize logging file
                System.out.println("LOGGER - SETNFIC");
                try {
                    Thread.sleep((long) (100));
                } catch (InterruptedException e) {
                }
                log.setFileName(inMessage.getFName(), inMessage.getNIter());
                outMessage = new Message(Message.NFICDONE);
                break;
            case Message.SETREP:                            // Update GRI and report in logging file
                System.out.println("LOGGER - SETREP");
                try {
                    Thread.sleep((long) (100));
                } catch (InterruptedException e) {
                }
                log.reportStatus();
                outMessage = new Message(Message.REPDONE);
                break;
            case Message.ENDOP:                             // End of operations of the logger and report final status in the logging file
                System.out.println("LOGGER - ENDOP");
                try {
                    Thread.sleep((long) (100));
                } catch (InterruptedException e) {
                }
                log.reportFinalStatus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETMUSEUM:                         // End of operations of the logger and report final status in the logging file                
                System.out.println("LOGGER - SETMUSEUM");
                try {
                    Thread.sleep((long) (100));
                } catch (InterruptedException e) {
                }
                log.setMuseum(inMessage.getIntarray(), inMessage.getIntarray2());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETAP:                             // End of operations of the logger and report final status in the logging file                
                System.out.println("LOGGER - SETAP");
                try {
                    Thread.sleep((long) (100));
                } catch (InterruptedException e) {
                }
                log.setAssaultParty(inMessage.getPartyID(), inMessage.getPartyThieves(), inMessage.getIntarray(), inMessage.getValue());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETMTSTATUS:                       // End of operations of the logger and report final status in the logging file                
                System.out.println("LOGGER - SETMTSTATUS");
                try {
                    Thread.sleep((long) (100));
                } catch (InterruptedException e) {
                }
                log.setMasterThief(inMessage.getValue());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_ASSAULT_THIEF:                 // End of operations of the logger and report final status in the logging file                
                System.out.println("LOGGER - SET_ASSAULT_THIEF");
                try {
                    Thread.sleep((long) (100));
                } catch (InterruptedException e) {
                }
                log.setAssaultThief(inMessage.getThiefID(), inMessage.getStatus(), inMessage.getMaxDisp(), inMessage.getPartyID(), inMessage.getHasCanvas());
                outMessage = new Message(Message.ACK);
                break;

        }

        return (outMessage);
    }
}
