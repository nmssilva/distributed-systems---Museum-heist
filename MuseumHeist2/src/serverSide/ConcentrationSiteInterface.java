package serverSide;

import auxiliary.Message;
import auxiliary.MessageException;

/**
 *
 * @author Nuno Silva
 */
public class ConcentrationSiteInterface {
    private ConcentrationSite cs;

    public ConcentrationSiteInterface(ConcentrationSite cs) {

        this.cs = cs;

    }

    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;

        /* validacao */
        /*switch (inMessage.getType()) {

        }*/

        /* processamento */
        switch (inMessage.getType()) {

            case Message.AMINEEDED:
                outMessage = new Message(Message.ACK_BOOL, cs.amINeeded());
                break;
            case Message.GET_N_THIEVES_IN_CS:
                outMessage = new Message(Message.ACK_INT, cs.getnAssaultThievesCS());
                break;
            case Message.PREPARE_AP:
                cs.prepareAssaultParty(); 
                outMessage = new Message(Message.ACK);
                break;
            case Message.STARTOP:
                cs.startOfOperations();
                outMessage = new Message(Message.ACK);
                break;
        }
        return (outMessage);
    }
}
