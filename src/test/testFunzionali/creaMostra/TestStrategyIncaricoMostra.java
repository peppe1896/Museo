package test.testFunzionali.creaMostra;


import museo.strutturaMuseo.Mostra;
import museo.strutturaMuseo.Museo;
import museo.strutturaMuseo.Suggerimento;
import opera.GestoreOpere;
import opera.Opera;
import org.junit.jupiter.api.*;
import museo.personaleMuseo.amministratore.Amministratore;
import museo.personaleMuseo.amministratore.IncaricoMostra;
import visitatore.Visitatore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Se questo test fallisce è perché alcune opere che si stanno cercando di prendere
 */
public class TestStrategyIncaricoMostra {
    private Museo museo;
    private Amministratore amministratore;
    private GestoreOpere gestoreOpere;
    private IncaricoMostra incaricoMostra;

    @BeforeEach
    public void setTest(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
        gestoreOpere = museo.getGestoreOpere();
    }

    /**
     * In questo test mi aspetto di non creare istanze di IncaricoMostra per via della Strategia ZeroBudgetStrategy
     * (quindi di avere null come risultato di createIncaricoMostra di Amministratore).
     */
    @Test
    @DisplayName("Test strategy bilancio zero")
    public void testStrategyNoMoves(){
        museo.setBilancio(amministratore, 0);
        IncaricoMostra incaricoMostra = amministratore.azioneAmministratore();
        assertNull(incaricoMostra);
    }

    /**
     * Questo test crea un IncaricoMostra con la LowBudgetStrategy se il bilancio è 150. Quindi creo una mostra con
     * delle opere che sono di proprietà del museo.
     */
    @Test
    @DisplayName("Test strategy bilancio basso")
    public void testStrategyLowBal(){
        amministratore.setAmministratoreAutomatico(false);
        museo.setBilancio(amministratore, 150);
        IncaricoMostra incaricoMostra = amministratore.forceStrategyExecution(1);
        assertNotNull(incaricoMostra);
        List<Opera> opereMostra = incaricoMostra.getOpereMostra();
        for(Opera o:opereMostra)
            assertSame(o.getProprietario(), museo);
    }

    /**
     * Verifico che con un alto budget riesco a noleggiare dei quadri anche da altri musei.
     * Visto che questa strategy prevede 4 Opere da musei che non sia questo, allora asserisco
     * {@code true} se in questo ArrayList ho esattamente 4 opere di musei esterni.
     */
    @Test
    @DisplayName("Test strategy bilancio sostanzioso")
    public void testStrategyHighBal(){
        amministratore.setAmministratoreAutomatico(false);
        museo.setBilancio(amministratore,3000);
        IncaricoMostra incaricoMostra = amministratore.forceStrategyExecution(2,5,true);
        List<Opera> opere = incaricoMostra.getOpereMostra();
        int count = 0;
        for(Opera o: opere){
            if(o.getProprietario() != museo)
                count++;
        }
        assertEquals(count, 4);
    }

    /**
     * Ci sono due Opere TestA e TestB presenti dentro le opere. Aggiungendo più suggerimenti per le due opere mi
     * aspetto che l'Amministratore crei un IncaricoMostra con all'interno le due opere.
     * Uso questo test per creare l'IncaricoMostra per il testKillStrategy
     */
    @Test
    @DisplayName("Test strategy automatica con alto numero di suggerimenti")
    public void testStrategyAuto(){
        amministratore.setAmministratoreAutomatico(false);
        museo.setBilancio(amministratore, 500);
        Visitatore v = new Visitatore(100);
        Opera opera1 = gestoreOpere.getOperaNome("TestY");
        Opera opera2 = gestoreOpere.getOperaNome("TestZ");
        for(int i=0;i<5;i++) {
            museo.registraSuggerimento(new Suggerimento(opera1, v));
            museo.registraSuggerimento(new Suggerimento(opera2, v));
        }
        incaricoMostra = amministratore.forceStrategyExecution(3);
        assertTrue(incaricoMostra.getOpereMostra().contains(opera1));
        assertTrue(incaricoMostra.getOpereMostra().contains(opera2));
    }

    /**
     * Test che prova la KillStrategy. Mi aspetto che con la KillStrategy ci siano delle Mostre chiuse
     * dentro Amministratore.
     */
    @Test
    @DisplayName("Test della strategy che annulla una Mostra in corso di realizzazione o di svolgimento")
    public void testKillStrategy(){
        testStrategyAuto();
        Mostra m = incaricoMostra.getMostra();
        assertFalse(m.isTerminata());
        amministratore.forceStrategyExecution(0);
        assertTrue(m.isTerminata());
    }

    @AfterEach
    public void clear(){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        gestoreOpere.resetAllOpere();
    }
}
