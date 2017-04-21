package serverSide;

import static auxiliary.Heist.*;
import auxiliary.Message;
import auxiliary.MessageException;

/**
 *
 * @author Nuno Silva
 */
public class LoggerInterface {

    private Logger log;

    public LoggerInterface(Logger log) {
        this.log = log;
    }

    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;

        /* validacao */
        switch (inMessage.getType()) {

            case Message.SET_ASSAULT_PARTY:
                if ((inMessage.getApId()>= MAX_ASSAULT_PARTIES) || (inMessage.getApId() < 0)) {
                    throw new MessageException("Valor inválido!!", inMessage);
                }
                if ((inMessage.getElements().length >= MAX_ASSAULT_PARTY_THIEVES)) {
                    throw new MessageException("Array com comprimento inválido!!", inMessage);
                }
                if ((inMessage.getPositions().length >= MAX_ASSAULT_PARTY_THIEVES)) {
                    throw new MessageException("Array com comprimento inválido!!", inMessage);
                }
                if ((inMessage.getRoomId()>= ROOMS_NUMBER) || (inMessage.getRoomId() < 0)) {
                    throw new MessageException("Valor inválido!!", inMessage);
                }
                break;
                
            case Message.SET_MUSEUM:
                if ((inMessage.getRooms().length != ROOMS_NUMBER)) {
                    throw new MessageException("Array com comprimento inválido!!!", inMessage);
                }
                break;
                
            case Message.SET_N_PAINTINGS:
                if ((inMessage.getValue() >= MAX_PAINTINGS)) {
                    throw new MessageException("Valor inválido!!", inMessage);
                }
                break;
        }

        /* processamento */
        switch (inMessage.getType()) {

            case Message.REPORT_FINAL_STATUS:
                log.reportFinalStatus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.REPORT_INITIAL_STATUS:
                log.reportInitialStatus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.REPORT_STATUS:
                log.reportStatus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_ASSAULT_PARTY:
                log.setAssaultParty(inMessage.getApId(), inMessage.getElements(), inMessage.getPositions(), inMessage.getRoomId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_ASSAULT_THIEF:
                log.setAssaultThief();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_MASTER_STATE:
                log.setMasterState();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_MUSEUM:
                log.setMuseum(inMessage.getRooms());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_N_PAINTINGS:
                log.setnPaintings(inMessage.getValue());
                outMessage = new Message(Message.ACK);
                break;
            

        }
        return (outMessage);
    }
}

