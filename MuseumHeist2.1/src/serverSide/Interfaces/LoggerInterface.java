package serverSide.Interfaces;

import static auxiliary.Heist.*;
import auxiliary.Message;
import auxiliary.MessageException;
import serverSide.Logger;

/**
 *
 * @author Nuno Silva
 */
public class LoggerInterface extends Interface {

    private Logger log;

    public LoggerInterface(Logger log) {
        this.log = log;
    }

    @Override
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
            case Message.SET_ASSAULT_THIEF:
                if ((inMessage.getThief().getThiefID() >= THIEVES_NUMBER) || (inMessage.getThief().getThiefID() < 0)) {
                    throw new MessageException("Valor inválido!!", inMessage);
                }
                break;
            case Message.SETNFIC:
                if ((inMessage.getNIter() < 1 )) {
                    throw new MessageException("NIter inválido!!", inMessage);
                }
                if ((inMessage.getFName().length() == 0 )) {
                    throw new MessageException("Nome do ficherio de log inválido!!", inMessage);
                }
                break;
        }
        /* processamento */
        switch (inMessage.getType()) {

            case Message.GETLOG:
                System.out.println("LOGGER - GETLOG ");
                outMessage = new Message(Message.ACK, log);
                break;
            case Message.REPORT_FINAL_STATUS:
                System.out.println("LOGGER - REPORT_FINAL_STATUS");
                log.reportFinalStatus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.REPORT_INITIAL_STATUS:
                System.out.println("LOGGER - REPORT_INITIAL_STATUS");
                log.reportInitialStatus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.REPORT_STATUS:
                System.out.println("LOGGER - REPORT_STATUS");
                log.reportStatus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_ASSAULT_PARTY:
                System.out.println("LOGGER - SET_ASSAULT_PARTY");
                log.setAssaultParty(inMessage.getApId(), inMessage.getElements(), inMessage.getPositions(), inMessage.getRoomId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_ASSAULT_THIEF:
                System.out.println("LOGGER - SET_ASSAULT_THIEF");
                log.setAssaultThief();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_MASTER_STATE:
                System.out.println("LOGGER - SET_MASTER_STATE");
                log.setMasterState();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_MUSEUM:
                System.out.println("LOGGER - SET_MUSEUM");
                log.setMuseum(inMessage.getRooms());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_N_PAINTINGS:
                System.out.println("LOGGER - SET_N_PAINTINGS");
                log.setnPaintings(inMessage.getValue());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETNFIC:            
                System.out.println("LOGGER - SETNFIC");
                outMessage = new Message(Message.NFICDONE);
                break;
            

        }
        return (outMessage);
    }
}

