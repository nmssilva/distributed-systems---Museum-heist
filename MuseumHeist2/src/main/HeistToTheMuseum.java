package main;

import serverSide.ConcentrationSite;
import serverSide.AssaultParty;
import serverSide.ControlCollectionSite;
import serverSide.Logger;
import serverSide.Museum;
import clientSide.AssaultThief;
import clientSide.MasterThief;
import java.rmi.RemoteException;
import genclass.*;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class HeistToTheMuseum {

    /**
     *
     * The main program
     *
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws RemoteException, InterruptedException {
        int runCount = 0;
        int maxRun = 5;

        while (runCount < maxRun) { //executar maxRun vezes
            runCount++;
            System.out.println("RUN #" + runCount);

            // Inicialização
            Logger log = new Logger();
            Museum museum = new Museum(log);
            AssaultParty[] assparties = new AssaultParty[2];

            for (int i = 0; i < 2; i++) {
                assparties[i] = new AssaultParty(i, museum, log);
            }

            ControlCollectionSite ccs = new ControlCollectionSite(assparties, log);
            ConcentrationSite cs = new ConcentrationSite(ccs, assparties, log);

            MasterThief masterThief = new MasterThief(ccs, cs);
            AssaultThief thiefs[] = new AssaultThief[6];

            log.reportInitialStatus();

            masterThief.start();

            for (int i = 0; i < 6; i++) {
                thiefs[i] = new AssaultThief(i, cs, ccs, assparties, museum);
                thiefs[i].start();
            }

            /* aguardar o fim da simulação */
            GenericIO.writelnString();
            try {
                masterThief.join();
                System.out.println("Master Thief has finished!");
            } catch (InterruptedException e) {
            }

            GenericIO.writelnString();
            for (int i = 0; i < 6; i++) {
                try {
                    thiefs[i].join();
                } catch (InterruptedException e) {
                }
                GenericIO.writelnString("Thief " + i + " has finished.");
            }

            log.reportFinalStatus();

            GenericIO.writelnString();
        }
    }
}
