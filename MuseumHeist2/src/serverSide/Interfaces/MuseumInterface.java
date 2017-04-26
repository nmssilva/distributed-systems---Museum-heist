package serverSide.Interfaces;

import static auxiliary.Heist.*;
import auxiliary.Message;
import auxiliary.MessageException;
import serverSide.Museum;

/**
 *
 * @author Nuno Silva
 */
public class MuseumInterface extends Interface {

    private Museum museum;

    public MuseumInterface(Museum museum) {

        this.museum = museum;

    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;

        /* validacao */
        switch (inMessage.getType()) {
            case Message.GETDIST:
                if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0)) {
                    throw new MessageException("Id da room inválido!!", inMessage);
                }
                break;
            case Message.GET_N_PAINTING:
                if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0)) {
                    throw new MessageException("Id do room inválido!", inMessage);
                }
                break;
            case Message.ROLLCANVAS:
                if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0)) {
                    throw new MessageException("Id do room inválido!", inMessage);
                }
                break;

        }

        /* processamento */
        switch (inMessage.getType()) {

            case Message.GETDIST:
                System.out.println("MUSEUM - GETDIST");
                outMessage = new Message(Message.ACK_INT, museum.getRoomDistance(inMessage.getRoomId()));        
                break;
            case Message.GET_N_PAINTING:
                System.out.println("MUSEUM - GET_N_PAINTING");
                outMessage = new Message(Message.ACK_INT, museum.getPaintingsNumber(inMessage.getRoomId())); 
                break;
            case Message.ROLLCANVAS:
                System.out.println("MUSEUM - ROLLCANVAS");
                outMessage = new Message(Message.ACK_INT, museum.rollACanvas(inMessage.getRoomId()));      
                break;
        }
        return (outMessage);
    }

}
