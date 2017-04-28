/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide.Interfaces;

import auxiliary.Message;
import auxiliary.MessageException;

/**
 *
 * @author Nuno Silva
 */
public abstract class Interface{

    /**
     *
     * Função de tratamento dos paramentros de entrada, chamada aos métodos e
     * resposta das mensagens
     *
     * @param inMessage
     * @return Mensagem de retorno
     * @throws auxiliary.MessageException
     */
    abstract public Message processAndReply(Message inMessage) throws MessageException;

}
