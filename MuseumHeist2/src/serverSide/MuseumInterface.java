package serverSide;

import static auxiliary.Heist.*;
import auxiliary.Message;
import auxiliary.MessageException;;

/**
 *
 * @author Nuno Silva
 */
public class MuseumInterface {
    
    private Museum museum;

   public MuseumInterface (Museum museum){

      this.museum = museum;
      
   }

   public Message processAndReply (Message inMessage) throws MessageException{

      Message outMessage = null;          


      /* validacao */ 

      switch (inMessage.getType ())
      { case Message.GETDIST:  if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0))
                                  throw new MessageException ("Id da room inválido!!", inMessage);
                               break;
        case Message.GETPAINTN:  if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0))
                                  throw new MessageException ("Id do room inválido!", inMessage);
                               break;
        case Message.ROLLCANVAS: if ((inMessage.getRoomId() >= ROOMS_NUMBER) || (inMessage.getRoomId() < 0))
                                  throw new MessageException ("Id do room inválido!", inMessage);
                               break;
        default:               throw new MessageException ("Tipo inválido!", inMessage);

      }

      /* processamento */  

      switch (inMessage.getType ()){

        case Message.GETDIST: outMessage = new Message (Message.DIST, museum.getRoomDistance (inMessage.getRoomId()));          // room distance, agr = int roomId
                              break;
        case Message.GETPAINTN: outMessage = new Message (Message.PAINTNUMB, museum.getPaintingsNumber (inMessage.getRoomId())); // number of paintings arg = int roomId
                              break;
        case Message.ROLLCANVAS:
                              if(museum.rollACanvas (inMessage.getRoomId())){            // attempt to rollacanvas, agr = int roomId
                                outMessage = new Message (Message.ROLLED);                // positive answer
                              }else{
                                outMessage = new Message (Message.NOT_ROLLED);            // negative answer
                              }
                              break; 
      }
     return (outMessage);
   }
    
}
