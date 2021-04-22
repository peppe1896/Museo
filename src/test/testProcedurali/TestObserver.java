package test.testProcedurali;

import strutturaMuseo.organizzazione.organizzatore.Mostra;
import strutturaMuseo.museoAndAdmin.Museo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import strutturaMuseo.museoAndAdmin.Amministratore;
import strutturaMuseo.museoAndAdmin.IncaricoMostra;
import visitatore.Visitatore;

import static org.junit.jupiter.api.Assertions.*;

public class TestObserver {
    private Amministratore amministratore;
    private Museo museo;
    private int numeroMostreCreate = 0;

    @BeforeEach
    public void setTestMain(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
    }

    @BeforeEach
    public void setTest(){
        numeroMostreCreate = museo.getMostre().size();
        assertEquals(numeroMostreCreate, 0);
    }

    /**
     * Mi aspetto di vedere che con un bilancio più alto l'Oberver (l'Amministratore) crei almeno una Mostra
     */
    @Test
    @DisplayName("Test numero Mostre con bilancio alto")
    public void testMinNumMostre() throws Exception {
        museo.addBilancio(3000);
        numeroMostreCreate = museo.getMostre().size();
        assertTrue(numeroMostreCreate != 0);
    }

    @Test
    @DisplayName("Test Create-Sell-Destroy-Repeat")
    public void testFinal(){
        museo.addBilancio(5000);
        System.out.println("Mi aspetto di vedere 2 mostre.");
        assertEquals(2, museo.getMostre().size());
        Mostra m = museo.getMostre().iterator().next();
        System.out.println("Posti pre acquisto: "+ m.getPostiRimasti());
        int postiPre = m.getPostiRimasti();
        Visitatore[] visitors = new Visitatore[800];
        for(int j = 0;j<visitors.length;j++)
            visitors[j] = new Visitatore(1000);
        try {
            for (int i = 0;i < 6;i++){
                museo.vendiTicketMostraFisica(visitors[i], m);
                Thread.sleep(5);
            }
            System.out.println("Posti dopo l'acquisto: "+m.getPostiRimasti());
            assertTrue(postiPre != m.getPostiRimasti());
            Thread.sleep(100);
            System.out.println("Aggiungo una mostra di tante opere d'arte, che sovraccarica le Sale.");
            System.out.println("Mi aspetto che l'amministratore chiuda la più vecchia delle mostre.");
            IncaricoMostra incaMo = amministratore.forceStrategyExecution(2, 20, true);
            System.out.println("Se qui è true vuol dire che è terminata: "+ m.isTerminata());
            assertTrue(m.isTerminata());
            assertEquals(2, museo.getMostre().size());
            m = museo.getMostre().iterator().next();
            System.out.println("m è una mostra virtuale? "+m.isVirtual());
            for(int i = 0;i<visitors.length;i++)
                museo.registraVisitatore(visitors[i]);
            System.out.println("Aggiungo tanti visitatori a quella mostra. In questo modo verrà chiusa.");
            for(int j=0;j<visitors.length;j++)
                museo.vendiTicketMostraVirtuale(visitors[j],m, false);
            assertEquals(2, museo.getMostre().size());
            assertEquals(2, museo.getMostre().size());
            System.out.println("Questa seconda mostra è attiva? "+ !m.isTerminata());
            assertTrue(m.isTerminata());
            m = incaMo.getMostra();
            for(int i=0;i<visitors.length;i++)
                museo.vendiTicketMostraVirtuale(visitors[i], m, true);
            System.out.println("Questa ultima mostra è ancora attiva? "+ !m.isTerminata());
            assertTrue(m.isTerminata());
            assertEquals(2, museo.getMostre().size());
        }catch (InterruptedException e){}
    }
}
