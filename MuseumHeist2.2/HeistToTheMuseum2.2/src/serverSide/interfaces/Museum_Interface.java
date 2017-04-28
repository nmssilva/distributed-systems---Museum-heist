package serverSide.interfaces;

import auxiliary.messages.*;
import serverSide.Museum;
import static auxiliary.messages.Message.*;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */
public class IMuseum extends Interface {

    /**
     * Concentration site (represents the provided service)
     *
     * @serialField cs
     */
    private Museum museum;

    /**
     * Concentration site interface instantiation
     *
     * @param museum
     */
    public IMuseum(Museum museum) {
        this.museum = museum;
    }

    /**
     * Processamento das mensagens através da execução da tarefa correspondente.
     * Geração de uma mensagem de resposta.
     *
     * @param inMessage mensagem com o pedido
     *
     * @return mensagem de resposta
     *
     * @throws MessageException se a mensagem com o pedido for considerada
     * inválida
     */
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                           // mensagem de resposta

        /* seu processamento */
        
        if (inMessage.getType() == GET_DIST_OUTSIDE) {
            System.out.println("MUSEUM - GET DIST OUTSIDE" + inMessage.getInteger());
            int distOutside = museum.getRoom(inMessage.getInteger()).getDistOutside();
            
            outMessage = new Message(Message.ACK, distOutside);
        }
         if (inMessage.getType() == ROLL_A_CANVAS) {
            System.out.println("MUSEUM - ROLL_A_CANVAS");
            int hasCanvas = museum.rollACanvas(inMessage.getInteger());
            
            outMessage = new Message(Message.ACK, hasCanvas);
        }
        
        return outMessage;
    }
}
