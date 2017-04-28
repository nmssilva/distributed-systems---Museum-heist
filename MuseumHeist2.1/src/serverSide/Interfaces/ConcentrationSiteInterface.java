package serverSide.Interfaces;

import static auxiliary.Heist.THIEVES_NUMBER;
import auxiliary.Message;
import auxiliary.MessageException;
import interfaces.IConcentrationSite;
import serverSide.ConcentrationSite;

/**
 *
 * @author Nuno Silva
 */
public class ConcentrationSiteInterface extends Interface {

    private IConcentrationSite cs;

    public ConcentrationSiteInterface(IConcentrationSite cs) {

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
            case Message.GETCS:
                System.out.println("CS - GETCS ");
                outMessage = new Message(Message.ACK, cs);
                break;
            case Message.AMINEEDED:
                System.out.println("CS - AMINEEDED ");
                outMessage = new Message(Message.ACK_BOOL, cs.amINeeded());
                break;
            case Message.GET_N_THIEVES_IN_CS:
                System.out.println("CS - GET_N_THIEVES_IN_CS");
                outMessage = new Message(Message.ACK_INT, cs.getnAssaultThievesCS());
                break;
            case Message.PREPARE_AP:
                System.out.println("CS - PREPARE_AP");
                cs.prepareAssaultParty();
                outMessage = new Message(Message.ACK);
                break;
            case Message.STARTOP:
                System.out.println("CS - STARTOP");
                cs.startOfOperations();
                outMessage = new Message(Message.ACK);
                break;
        }
        return (outMessage);
    }
}
