package serverSide.Interfaces;

import static auxiliary.Heist.THIEVES_NUMBER;
import auxiliary.Message;
import auxiliary.MessageException;
import serverSide.ConcentrationSite;

/**
 *
 * @author Nuno Silva
 */
public class ConcentrationSiteInterface extends Interface {
    private ConcentrationSite cs;

    public ConcentrationSiteInterface(ConcentrationSite cs) {

        this.cs = cs;

    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;

        /* validacao */
        switch (inMessage.getType()) {
            case Message.AMINEEDED:
                if ((inMessage.getThief().getThiefID() >= THIEVES_NUMBER) || (inMessage.getThief().getThiefID() < 0)) {
                    throw new MessageException("Valor inválido!!", inMessage);
                }
                break;
        }

        /* processamento */
        switch (inMessage.getType()) {

            case Message.AMINEEDED:
                outMessage = new Message(Message.ACK_BOOL, cs.amINeeded(inMessage.getThief()));
                break;
            case Message.GET_N_THIEVES_IN_CS:
                outMessage = new Message(Message.ACK_INT, cs.getnAssaultThievesCS());
                break;
            case Message.PREPARE_AP:
                cs.prepareAssaultParty(); 
                outMessage = new Message(Message.ACK);
                break;
            case Message.STARTOP:
                cs.startOfOperations(inMessage.getMThief());
                outMessage = new Message(Message.ACK);
                break;
        }
        return (outMessage);
    }
}
