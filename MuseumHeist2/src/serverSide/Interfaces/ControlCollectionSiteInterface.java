package serverSide.Interfaces;

import static auxiliary.Heist.*;
import auxiliary.Message;
import auxiliary.MessageException;
import serverSide.ControlCollectionSite;

/**
 *
 * @author Nuno Silva
 */
public class ControlCollectionSiteInterface extends Interface {

    private ControlCollectionSite ccs;

    public ControlCollectionSiteInterface(ControlCollectionSite ccs) {
        this.ccs = ccs;
    }

    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;

        /* validacao */
        switch (inMessage.getType()) {

            case Message.APPRAISE:
                if ((inMessage.getValue() >= THIEVES_NUMBER) || (inMessage.getValue() < 0)) {
                    throw new MessageException("Valor inválido!!", inMessage);
                }
                break;

            case Message.SET_NEXT_PARTY:
                if ((inMessage.getApId() >= MAX_ASSAULT_PARTIES) || (inMessage.getApId() < 0)) {
                    throw new MessageException("Assault Party ID inválido!!", inMessage);
                }
                break;

            case Message.SET_NEXT_ROOM:
                if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0)) {
                    throw new MessageException("Room ID inválido!!", inMessage);
                }
                break;
        }

        /* processamento */
        switch (inMessage.getType()) {

            case Message.APPRAISE:
                outMessage = new Message(Message.ACK_INT, ccs.appraiseSit(inMessage.getValue()));
                break;
            case Message.COLLECT_CANVAS:
                ccs.collectCanvas();
                outMessage = new Message(Message.ACK);
                break;
            case Message.GET_NEXT_PARTY:
                outMessage = new Message(Message.ACK_INT, ccs.getNextParty());
                break;
            case Message.GET_NEXT_ROOM:
                outMessage = new Message(Message.ACK_INT, ccs.getNextRoom());
                break;
            case Message.GET_N_PAINTING:
                outMessage = new Message(Message.ACK_INT, ccs.getnPaintings());
                break;
            case Message.HANDCANVAS:
                ccs.handCanvas();
                outMessage = new Message(Message.ACK);
                break;
            case Message.IN_PARTY:
                outMessage = new Message(Message.ACK_BOOL, ccs.inParty());
                break;
            case Message.SET_READY:
                ccs.setReady();
                outMessage = new Message(Message.ACK);
                break;
            case Message.NEXT_EMPTY_PARTY:
                outMessage = new Message(Message.ACK_INT, ccs.nextEmptyParty());
                break;
            case Message.NEXT_EMPTY_ROOM:
                outMessage = new Message(Message.ACK_INT, ccs.nextEmptyRoom());
                break;
            case Message.PREPARE_EXCURSION:
                ccs.prepareExcursion();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SEND_AP:
                ccs.sendAssaultParty();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_NEXT_PARTY:
                ccs.setNextParty(inMessage.getApId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_NEXT_ROOM:
                ccs.setNextRoom(inMessage.getRoomId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SUM_UP_RESULTS:
                ccs.sumUpResults();
                outMessage = new Message(Message.ACK);
                break;
            case Message.TAKE_REST:
                ccs.takeARest();
                outMessage = new Message(Message.ACK);
                break;

        }
        return (outMessage);
    }
}
