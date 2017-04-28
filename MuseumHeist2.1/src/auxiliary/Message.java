package auxiliary;

import clientSide.*;
import java.io.*;
import interfaces.*;
import serverSide.AssaultParty;
import serverSide.ControlCollectionSite;
import serverSide.Logger;
import serverSide.Museum;
import serverSide.Room;

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
    public static final int GET_N_THIEVES_IN_CS = 5;

    /**
     * Preparar assault party (operação pedida pelo cliente)
     *
     * @serialField PREPAREAP
     */
    public static final int PREPARE_AP = 6;

    /**
     * Enviar assault party (operação pedida pelo cliente)
     *
     * @serialField SENDAP
     */
    public static final int SEND_AP = 7;

    /**
     * MasterThief espera pela chegada dos thiefs (operação pedida pelo cliente)
     *
     * @serialField TAKEREST
     */
    public static final int TAKE_REST = 8;

    /**
     * MasterThief colecta os quadros (operação pedida pelo cliente)
     *
     * @serialField COLLCANV
     */
    public static final int COLLECT_CANVAS = 9;

    /**
     * Somatório dos resultados (operação pedida pelo cliente)
     *
     * @serialField SUMUPRES
     */
    public static final int SUM_UP_RESULTS = 10;

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
    public static final int PREPARE_EXCURSION = 12;

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
     * Operação realizada com sucesso com envio de um valor inteiro (resposta
     * enviada pelo servidor)
     *
     * @serialField ACK_INT
     */
    public static final int ACK_INT = 21;

    /**
     * Operação realizada com sucesso com envio de um array de valores inteiros
     * (resposta enviada pelo servidor)
     *
     * @serialField ACK_INT
     */
    public static final int ACK_INT_ARRAY = 22;

    /**
     * Operação realizada com sucesso com envio de um valor booleano (resposta
     * enviada pelo servidor)
     *
     * @serialField ACK
     */
    public static final int ACK_BOOL = 23;

    /**
     * Buscar numero de quadros
     *
     * @serialField GETPAINTN
     */
    public static final int GET_N_PAINTING = 25;

    /**
     * Adicionar thief ao assault party
     *
     * @serialField ADD_THIEF
     */
    public static final int ADD_THIEF = 26;

    /**
     * Obter o index do thief na assault party
     *
     * @serialField GET_INDEX_AP
     */
    public static final int GET_INDEX_AP = 27;

    /**
     * Fazer set ao room da assault party
     *
     * @serialField SET_ROOM
     */
    public static final int SET_ROOM = 28;

    /**
     * Obter array de thieves na assault party
     *
     * @serialField GET_PARTY_THIEVES
     */
    public static final int GET_PARTY_THIEVES = 29;

    /**
     * Obter array do maximum displacement dos thiefs na asasault party
     *
     * @serialField GET_PARTY_THIEVES_MAX_DISP
     */
    public static final int GET_PARTY_THIEVES_MAX_DISP = 30;

    /**
     * Obter array das posiçoes dos thieves da assault party
     *
     * @serialField GET_PARTY_THIEVES_POS
     */
    public static final int GET_PARTY_THIEVES_POS = 31;

    /**
     * Obter room ID para onde a assault party foi
     *
     * @serialField GET_ROOM_ID
     */
    public static final int GET_ROOM_ID = 32;

    /**
     * Definir o primeiro thief a fazer crawl
     *
     * @serialField SET_FIRST_TO_CRAWL
     */
    public static final int SET_FIRST_TO_CRAWL = 33;

    /**
     * Definir thief na assault party
     *
     * @serialField SET_PARTY_THIEVES
     */
    public static final int SET_PARTY_THIEVES = 34;

    /**
     * Definir próxima assault party a ser enviada
     *
     * @serialField SET_NEXT_PARTY
     */
    public static final int SET_NEXT_PARTY = 35;

    /**
     * Definir proxima room para a assault party
     *
     * @serialField SET_NEXT_ROOM
     */
    public static final int SET_NEXT_ROOM = 36;

    /**
     * Obter próxima assault party a ser enviada
     *
     * @serialField GET_NEXT_PARTY
     */
    public static final int GET_NEXT_PARTY = 37;

    /**
     * Obter proxima room para a assault party
     *
     * @serialField GET_NEXT_ROOM
     */
    public static final int GET_NEXT_ROOM = 38;

    /**
     * Obter se o thief está na party
     *
     * @serialField IN_PARTY
     */
    public static final int IN_PARTY = 39;

    /**
     * Definir valor da variável ready para true
     *
     * @serialField IS_READY
     */
    public static final int SET_READY = 40;

    /**
     * Obter proxima assault party vazia
     *
     * @serialField NEXT_EMPTY_PARTY
     */
    public static final int NEXT_EMPTY_PARTY = 41;

    /**
     * Obter proxima room vazia
     *
     * @serialField NEXT_EMPTY_ROOM
     */
    public static final int NEXT_EMPTY_ROOM = 42;

    /**
     * Report do estado final
     *
     * @serialField REPORT_FINAL_STATUS
     */
    public static final int REPORT_FINAL_STATUS = 43;

    /**
     * Report do estado inicial
     *
     * @serialField REPORT_INITIAL_STATUS
     */
    public static final int REPORT_INITIAL_STATUS = 44;

    /**
     * Report do estado atual
     *
     * @serialField REPORT_STATUS
     */
    public static final int REPORT_STATUS = 45;

    /**
     * Definir assault party
     *
     * @serialField SET_ASSAULT_PARTY
     */
    public static final int SET_ASSAULT_PARTY = 46;

    /**
     * Definir Assault thief
     *
     * @serialField SET_ASSAULT_THIEF
     */
    public static final int SET_ASSAULT_THIEF = 47;

    /**
     * Definir estado da master thief
     *
     * @serialField SET_MASTER_STATE
     */
    public static final int SET_MASTER_STATE = 48;

    /**
     * Definir o museum
     *
     * @serialField SET_MUSEUM
     */
    public static final int SET_MUSEUM = 49;

    /**
     * Definir numero de pinturas
     *
     * @serialField SET_N_PAINTINGS
     */
    public static final int SET_N_PAINTINGS = 50;
    
    /**
     * Obter Concentration Site
     *
     * @serialField GETCS
     */
    public static final int GETCS = 51;
    public static final int GETLOG = 52;
    public static final int GETMUSEUM = 53;
    public static final int GETAP = 54;
    public static final int GETCCS = 55;


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
     * Valor tipo int[] na resposta do servidor
     *
     * @serialField ackintarray
     */
    private int[] ackintarray;

    /**
     * Valor tipo boolean na resposta do servidor
     *
     * @serialField ackbool
     */
    private boolean ackbool;

    /**
     * Valor tipo int para ser transportado na mensagem
     *
     * @serialField value
     */
    private int value;

    /**
     * Array de elementos de uma assault party
     *
     * @serialField elements
     */
    private int[] elements;

    /**
     * Array da posição dos elementos de uma assault party
     *
     * @serialField positions
     */
    private int[] positions;

    /**
     * Array de roomsy
     *
     * @serialField rooms
     */
    private Room[] rooms;

    /**
     * Thief
     *
     * @serialField thief
     */
    private AssaultThief thief;
    
    /**
     * MasterThief
     *
     * @serialField mthief
     */
    private MasterThief mthief;
    
    /**
     * Assault Parties
     *
     * @serialField ap
     */
    private IAssaultParty ap;
    
    /**
     * ConcentrationSite
     *
     * @serialField cs
     */
    private IConcentrationSite cs;
    
    /**
     * ControlCollectionSite
     *
     * @serialField ccs
     */
    private IControlCollectionSite ccs;
    
    /**
     * Logger
     *
     * @serialField log
     */
    private ILogger log;
    
    /**
     * Museum
     *
     * @serialField mthief
     */
    private IMuseum museum;

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
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     */
    public Message(int type) {
        msgType = type;
    }

    /**
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     * @param value valor inteiro
     */
    public Message(int type, int value) {
        msgType = type;
        if ((msgType == AMINEEDED) || (msgType == PREPARE_EXCURSION) || (msgType == CRAWLIN)
                || (msgType == ROLLCANVAS) || (msgType == REVDIR)
                || (msgType == CRAWLOUT) || (msgType == HANDCANVAS)) {
            thiefId = value;
        }
        if (msgType == ACK_INT) {
            this.value = value;
        }
    }

    /**
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     * @param array array de valores inteiros
     */
    public Message(int type, int[] array) {
        msgType = type;
        if (msgType == ACK_INT_ARRAY) {
            ackintarray = array;
        }
    }

    /**
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     * @param apId
     * @param elements
     * @param positions
     * @param roomId
     */
    public Message(int type, int apId, int[] elements, int[] positions, int roomId) {
        msgType = type;
        this.apId = apId;
        this.roomId = roomId;
        this.elements = new int[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
        this.positions = new int[positions.length];
        System.arraycopy(positions, 0, this.positions, 0, positions.length);
    }

    /**
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     * @param value valor booleano
     */
    public Message(int type, boolean value) {
        msgType = type;
        if (msgType == ACK_BOOL) {
            ackbool = value;
        }
        
    }

    /**
     * Instanciação de uma mensagem
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

    public Message(int msgType, AssaultThief thief) {
        this.msgType = msgType;
        this.thief = thief;
    }
    
    public Message(int msgType, MasterThief mthief) {
        this.msgType = msgType;
        this.mthief = mthief;
    }
    public Message(int msgType, MasterThief mthief, int value) {
        this.msgType = msgType;
        this.mthief = mthief;
        this.value = value;
    }

    public Message(int msgType, IConcentrationSite cs) {
        this.msgType = msgType;
        this.cs = cs;
    }

    public Message(int msgType, AssaultParty ap) {
        this.msgType = msgType;
        this.ap = ap;
    }

    public Message(int msgType, ControlCollectionSite ccs) {
        this.msgType = msgType;
        this.ccs = ccs;
    }

    public Message(int msgType, Logger log) {
        this.msgType = msgType;
        this.log = log;
    }

    public Message(int msgType, Museum museum) {
        this.msgType = msgType;
        this.museum = museum;
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
     * Obtenção do valor do campo value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Obtenção do valor do campo elements
     *
     * @return elements
     */
    public int[] getElements() {
        return elements;
    }

    /**
     * Obtenção do valor do campo positions
     *
     * @return positions
     */
    public int[] getPositions() {
        return positions;
    }

    /**
     * Obtenção do valor do campo rooms
     *
     * @return rooms
     */
    public Room[] getRooms() {
        return rooms;
    }

    /**
     * Obtenção do valor do campo ackintarray
     *
     * @return ackintarray
     */
    public int[] getAckintarray() {
        return ackintarray;
    }

    /**
     * Obtenção do valor do campo ackbool
     *
     * @return ackbool
     */
    public boolean getAckbool() {
        return ackbool;
    }
    
    /**
     * Obtenção do valor do campo thief
     *
     * @return thief
     */
    public AssaultThief getThief() {
        return thief;
    }
    
    /**
     * Obtenção do valor do campo mthief
     *
     * @return mthief
     */
    public MasterThief getMThief() {
        return mthief;
    }

    public IAssaultParty getAp() {
        return ap;
    }

    public IConcentrationSite getCs() {
        return cs;
    }

    public IControlCollectionSite getCcs() {
        return ccs;
    }

    public ILogger getLog() {
        return log;
    }

    public IMuseum getMuseum() {
        return museum;
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
