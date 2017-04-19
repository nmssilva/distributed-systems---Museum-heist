package auxiliary;

import java.io.*;

public class Message implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */
    /**
     * Inicialização do ficheiro de logging (operação pedida pelo cliente)
     *
     * @serialField SETNFIC
     */
    public static final int SETNFIC = 1;

    /**
     * Ficheiro de logging foi inicializado (resposta enviada pelo servidor)
     *
     * @serialField NFICDONE
     */
    public static final int NFICDONE = 2;

    /**
     * Inicio das operações (operação pedida pelo cliente)
     *
     * @serialField STARTOP
     */
    public static final int STARTOP = 3;

    /**
     * MastherThief pensa no que fazer (operação pedida pelo cliente)
     *
     * @serialField APPRAISE
     */
    public static final int APPRAISE = 4;

    /**
     * Buscar numero de thieves no concentration site (operação pedida pelo
     * cliente)
     *
     * @serialField GETNTHICS
     */
    public static final int GETNTHICS = 5;

    /**
     * Preparar assault party (operação pedida pelo cliente)
     *
     * @serialField PREPAREAP
     */
    public static final int PREPAREAP = 6;

    /**
     * Enviar assault party (operação pedida pelo cliente)
     *
     * @serialField SENDAP
     */
    public static final int SENDAP = 7;

    /**
     * MAsterThief espera pela chegada dos thiefs (operação pedida pelo cliente)
     *
     * @serialField TAKEREST
     */
    public static final int TAKEREST = 8;

    /**
     * MasterThief colecta os quadros (operação pedida pelo cliente)
     *
     * @serialField COLLCANV
     */
    public static final int COLLCANV = 9;

    /**
     * Somatório dos resultados (operação pedida pelo cliente)
     *
     * @serialField SUMUPRES
     */
    public static final int SUMUPRES = 10;

    /**
     * Thief espera para ser necessitado (operação pedida pelo cliente)
     *
     * @serialField AMINEEDED
     */
    public static final int AMINEEDED = 11;

    /**
     * Preparação da excursão (operação pedida pelo cliente)
     *
     * @serialField PREPEXCU
     */
    public static final int PREPEXCU = 12;

    /**
     * Buscar distancia até ao room (operação pedida pelo cliente)
     *
     * @serialField GETDIST
     */
    public static final int GETDIST = 13;

    /**
     * Thief efetua movimento de crawlin (operação pedida pelo cliente)
     *
     * @serialField CRAWLIN
     */
    public static final int CRAWLIN = 14;

    /**
     * Thief enrola uma pintura (operação pedida pelo cliente)
     *
     * @serialField ROLLCANVAS
     */
    public static final int ROLLCANVAS = 15;

    /**
     * Thief prepara para voltar ao collection site (operação pedida pelo
     * cliente)
     *
     * @serialField REVDIR
     */
    public static final int REVDIR = 16;

    /**
     * Thief efetua movimento crawl out (operação pedida pelo cliente)
     *
     * @serialField CRAWLOUT
     */
    public static final int CRAWLOUT = 17;

    /**
     * Thief entrega um quadro (operação pedida pelo cliente)
     *
     * @serialField HANDCANVAS
     */
    public static final int HANDCANVAS = 18;

    /**
     * Alertar o thread MasterThief do fim de operações (operação pedida pelo
     * cliente)
     *
     * @serialField ENDOP
     */
    public static final int ENDOP = 19;

    /**
     * Operação realizada com sucesso (resposta enviada pelo servidor)
     *
     * @serialField ACK
     */
    public static final int ACK = 20;

    /**
     * Buscar numero de quadros
     *
     * @serialField GETPAINTN
     */
    public static final int GETPAINTN = 21;

    /**
     * Resposta à distancia ao room
     *
     * @serialField DIST
     */
    public static final int DIST = 22;

    /**
     * Numero de pinturas
     *
     * @serialField PAINTNUMB
     */
    public static final int PAINTNUMB = 23;

    /**
     * Pintura enrolada
     *
     * @serialField DIST
     */
    public static final int ROLLED = 24;

    /**
     * Pintura nao enrolada
     *
     * @serialField DIST
     */
    public static final int NOT_ROLLED = 25;


    /* Campos das mensagens */
    /**
     * Tipo da mensagem
     *
     * @serialField msgType
     */
    private int msgType = -1;

    /**
     * Identificação do thief
     *
     * @serialField thiefId
     */
    private int thiefId = -1;

    /**
     * Identificação da assault party
     *
     * @serialField apId
     */
    private int apId = -1;

    /**
     * Identificação do room
     *
     * @serialField roomId
     */
    private int roomId = -1;

    /**
     * Nome do ficheiro de logging
     *
     * @serialField fName
     */
    private String fName = null;

    /**
     * Número de iterações do ciclo de vida dos clientes
     *
     * @serialField nIter
     */
    private int nIter = -1;

    /**
     * Instanciação de uma mensagem (forma 1).
     *
     * @param type tipo da mensagem
     */
    public Message(int type) {
        msgType = type;
    }

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type tipo da mensagem
     * @param id identificação do cliente/barbeiro
     */
    public Message(int type, int id) {
        msgType = type;
        if ((msgType == AMINEEDED) || (msgType == PREPEXCU) || (msgType == CRAWLIN)
                || (msgType == ROLLCANVAS) || (msgType == REVDIR)
                || (msgType == CRAWLOUT) || (msgType == HANDCANVAS)) {
            thiefId = id;
        }
    }

    /**
     * Instanciação de uma mensagem (forma 4).
     *
     * @param type tipo da mensagem
     * @param name nome do ficheiro de logging
     * @param nIter número de iterações do ciclo de vida dos clientes
     */
    public Message(int type, String name, int nIter) {
        msgType = type;
        fName = name;
        this.nIter = nIter;
    }

    /**
     * Obtenção do valor do campo tipo da mensagem.
     *
     * @return tipo da mensagem
     */
    public int getType() {
        return (msgType);
    }

    /**
     * Obtenção do valor do campo identificador da assault party.
     *
     * @return identificação da assault party
     */
    public int getApId() {
        return (apId);
    }

    /**
     * Obtenção do valor do campo identificador do room.
     *
     * @return identificação do room
     */
    public int getRoomId() {
        return (roomId);
    }

    /**
     * Obtenção do valor do campo identificador do thief.
     *
     * @return identificação do thief
     */
    public int getThiefId() {
        return (thiefId);
    }

    /**
     * Obtenção do valor do campo nome do ficheiro de logging.
     *
     * @return nome do ficheiro
     */
    public String getFName() {
        return (fName);
    }

    /**
     * Obtenção do valor do campo número de iterações do ciclo de vida dos
     * clientes.
     *
     * @return número de iterações do ciclo de vida dos clientes
     */
    public int getNIter() {
        return (nIter);
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da
     * identificação de cada campo e valor respectivo
     */
    @Override
    public String toString() {
        return ("Tipo = " + msgType
                + "\nId Thief = " + thiefId
                + "\nId AP = " + apId
                + "\nId Room = " + roomId
                + "\nNome Fic. Logging = " + fName
                + "\nN. de Iteracoes = " + nIter);
    }
}
