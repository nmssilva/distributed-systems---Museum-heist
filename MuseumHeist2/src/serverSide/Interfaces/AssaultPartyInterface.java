/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import static auxiliary.Heist.*;
import auxiliary.Message;
import auxiliary.MessageException;

/**
 *
 * @author Nuno Silva
 */
public class AssaultPartyInterface {
    
    private AssaultParty ap;

    public AssaultPartyInterface(AssaultParty ap) {

        this.ap = ap;

    }
    
    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;

        /* validacao */
        switch (inMessage.getType()) {
            case Message.ADD_THIEF:
                if ((inMessage.getThiefId() >= THIEVES_NUMBER) || (inMessage.getThiefId() < 0)) {
                    throw new MessageException("Id do thief inválido!!", inMessage);
                }
                break;
            case Message.SET_PARTY_THIEVES:
                if ((inMessage.getThiefId() >= THIEVES_NUMBER) || (inMessage.getThiefId() < 0)) {
                    throw new MessageException("Id do thief inválido!!", inMessage);
                }
                break;
            case Message.SET_ROOM:
                if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0)) {
                    throw new MessageException("Id da room inválido!!", inMessage);
                }
                break;

        }

        /* processamento */
        switch (inMessage.getType()) {
            
            case Message.ADD_THIEF:
                outMessage = new Message(Message.ACK_INT, ap.getDistOutsideRoom());
                break;
            case Message.CRAWLIN:
                ap.crawlIn();
                outMessage = new Message(Message.ACK);
                break;
            case Message.CRAWLOUT:
                ap.crawlOut();
                outMessage = new Message(Message.ACK);
                break;
            case Message.GETDIST:
                outMessage = new Message(Message.ACK_INT, ap.getDistOutsideRoom());
                break;
            case Message.GET_INDEX_AP:
                outMessage = new Message(Message.ACK_INT, ap.getIndexParty());
                break;
            case Message.GET_PARTY_THIEVES:
                outMessage = new Message(Message.ACK_INT_ARRAY, ap.getPartyThieves());
                break;
            case Message.GET_PARTY_THIEVES_MAX_DISP:
                outMessage = new Message(Message.ACK_INT_ARRAY, ap.getPartyThievesMaxDisp());
                break;
            case Message.GET_PARTY_THIEVES_POS:
                outMessage = new Message(Message.ACK_INT_ARRAY, ap.getPartyThievesPos());
                break;
            case Message.GET_ROOM_ID:
                outMessage = new Message(Message.ACK_INT, ap.getRoomID());
                break;
            case Message.REVDIR:
                ap.reverseDirection();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_FIRST_TO_CRAWL:
                ap.setFirstToCrawl();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_PARTY_THIEVES:
                ap.setPartyThieves(inMessage.getValue(), inMessage.getThiefId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_ROOM:
                ap.setRoom(inMessage.getRoomId());
                outMessage = new Message(Message.ACK);
                break;
        }
        return (outMessage);
    }
    
}
