package serverSide.interfaces;

import auxiliary.messages.*;
import serverSide.AssaultParty;
import static auxiliary.messages.Message.*;

public class AssaultParty_Interface extends Interface {

    private AssaultParty assparty;

    public AssaultParty_Interface(AssaultParty assparty) {
        this.assparty = assparty;
    }

    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                           // mensagem de resposta

        if (inMessage.getType() == IS_EMPTY_AP) {
            System.out.println("AP: IS_EMPTY_AP");
            boolean emptyAP = assparty.isEmptyAP();

            outMessage = new Message(Message.ACK, emptyAP);
        }

        if (inMessage.getType() == ADDTHIEF) {
            System.out.println("AP: ADDTHIEF");
            assparty.addThief(inMessage.getInteger(), inMessage.getInteger2());

            outMessage = new Message(Message.ACK);
        }

        if (inMessage.getType() == GET_PTHIEVES) {
            System.out.println("AP: GET_PTHIEVES");
            int[] pthieves = assparty.getPartyThieves();

            outMessage = new Message(Message.ACK, pthieves);
        }
        if (inMessage.getType() == GET_DIST_OUTSIDE) {
            System.out.println("AP: GET_DIST_OUTSIDE");
            int distOutside = assparty.getDistOutsideRoom();

            outMessage = new Message(Message.ACK, distOutside);
        }
        if (inMessage.getType() == SET_AP_ROOM) {
            System.out.println("AP: SET_AP_ROOM: " + inMessage.getInteger());
            
            assparty.setRoom(inMessage.getInteger());

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == CRAWL_IN) {
            System.out.println("AP: CRAWL_IN");
            assparty.crawlIn(inMessage.getInteger());

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == SETFIRST) {
            System.out.println("AP: SETFIRST");
            assparty.setFirst();

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == GET_ROOM_ID) {
            System.out.println("AP: GET_ROOM_ID");
            int roomID = assparty.getRoomID();

            outMessage = new Message(Message.ACK, roomID);
        }
        if (inMessage.getType() == REVERSE_DIRECTION) {
            System.out.println("AP: REVERSE_DIRECTION");
            assparty.reverseDirection(inMessage.getInteger());

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == CRAWL_OUT) {
            System.out.println("AP: CRAWL_OUT");
            assparty.crawlOut(inMessage.getInteger());

            outMessage = new Message(Message.ACK);
        }
        if (inMessage.getType() == SET_PTHIEVES) {
            System.out.println("AP: SET_PTHIEVES");
            assparty.setPartyThieves(inMessage.getInteger(), inMessage.getInteger2());

            outMessage = new Message(Message.ACK);
        }

        return outMessage;
    }
}
