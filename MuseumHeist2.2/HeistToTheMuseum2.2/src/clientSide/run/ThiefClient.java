package clientSide.run;

import static auxiliary.constants.Heist.*;
import clientSide.AssaultThief;
import genclass.*;

/**
 * Client for AssaultThief
 */
public class ThiefClient {

    /**
     * Programa principal.
     *
     */
    public static void main(String[] args) {
        //int nIter;                                           // número de iterações do ciclo de vida dos clientes

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Heist to the Museum - AssaultThieves\n");
        //GenericIO.writeString("Numero de iterações? ");
        //nIter = N_ITER;
        //GenericIO.writeString("Nome do ficheiro de logging? ");
        //GenericIO.writeString("Nome do sistema computacional onde está o servidor? ");
        //serverHostName = InetAddress.getLocalHost().getHostName();

        /* Criação dos threads barbeiro e cliente */
        AssaultThief thieves[] = new AssaultThief[THIEVES_NUMBER];

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            thieves[i] = new AssaultThief(i);
        }

        /* Arranque da simulação */
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            thieves[i].start();
        }

        /* Aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            try {
                thieves[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O thief " + i + " terminou.");
        }
        GenericIO.writelnString();

    }
}
