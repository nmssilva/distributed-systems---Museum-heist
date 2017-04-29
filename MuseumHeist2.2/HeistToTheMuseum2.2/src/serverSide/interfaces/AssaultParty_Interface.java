package serverSide.interfaces;

import auxiliary.messages.*;
import serverSide.AssaultParty;
import static auxiliary.messages.Message.*;

public class AssaultParty_Interface extends Interface {

    private AssaultParty assparty;

    public AssaultParty_Interface(AssaultParty assparty) {
        this.assparty = assparty;
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                           // mensagem de resposta

        switch (inMessage.getType()) {

            case IS_EMPTY_AP:
                System.out.println("AP: IS_EMPTY_AP");
                boolean emptyAP = assparty.isEmptyAP();

                outMessage = new Message(Message.ACK, emptyAP);
                break;

            case ADDTHIEF:
                System.out.println("AP: ADDTHIEF");
                assparty.addThief(inMessage.getInteger(), inMessage.getInteger2());

                outMessage = new Message(Message.ACK);
                break;

            case GET_PTHIEVES:
                System.out.println("AP: GET_PTHIEVES");
                int[] pthieves = assparty.getPartyThieves();

                outMessage = new Message(Message.ACK, pthieves);
                break;
            case GET_DIST_OUTSIDE:
                System.out.println("AP: GET_DIST_OUTSIDE");
                int distOutside = assparty.getDistOutsideRoom();

                outMessage = new Message(Message.ACK, distOutside);
                break;
            case SET_AP_ROOM:
                System.out.println("AP: SET_AP_ROOM: " + inMessage.getInteger());

                assparty.setRoom(inMessage.getInteger());

                outMessage = new Message(Message.ACK);
                break;
            case CRAWL_IN:
                System.out.println("AP: CRAWL_IN");
                assparty.crawlIn(inMessage.getInteger());

                outMessage = new Message(Message.ACK);
                break;
            case SETFIRST:
                System.out.println("AP: SETFIRST");
                assparty.setFirst();

                outMessage = new Message(Message.ACK);
                break;
            case GET_ROOM_ID:
                System.out.println("AP: GET_ROOM_ID");
                int roomID = assparty.getRoomID();

                outMessage = new Message(Message.ACK, roomID);
                break;
            case REVERSE_DIRECTION:
                System.out.println("AP: REVERSE_DIRECTION");
                assparty.reverseDirection(inMessage.getInteger());

                outMessage = new Message(Message.ACK);
                break;
            case CRAWL_OUT:
                System.out.println("AP: CRAWL_OUT");
                assparty.crawlOut(inMessage.getInteger());

                outMessage = new Message(Message.ACK);
                break;
            case SET_PTHIEVES:
                System.out.println("AP: SET_PTHIEVES");
                assparty.setPartyThieves(inMessage.getInteger(), inMessage.getInteger2());

                outMessage = new Message(Message.ACK);
                break;
        }

        return outMessage;
    }
}
