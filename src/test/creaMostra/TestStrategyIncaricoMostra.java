package test.creaMostra;

import museo.Museo;
import museo.Suggerimento;
import opera.GestoreOpere;
import opera.Opera;
import org.junit.jupiter.api.*;
import personale.pkgIncaricoMostra.Amministratore;
import personale.pkgIncaricoMostra.IncaricoMostra;
import personale.pkgIncaricoMostra.LowBudgetStrategy;
import personale.pkgIncaricoMostra.ZeroBudgetStrategy;
import visitatore.Visitatore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testStrategyZeroBal(){
        museo.setBilancio(amministratore, 0);
        IncaricoMostra incaricoMostra = amministratore.createIncaricoMostra();
        assertNull(incaricoMostra);
        assertTrue(amministratore.getStrategy() instanceof ZeroBudgetStrategy);
    }

    /**
     * Questo test crea un IncaricoMostra con la LowBudgetStrategy se il bilancio è 150. Quindi creo una mostra con
     * delle opere che sono di proprietà del museo.
     */
    @Test
    @DisplayName("Test strategy bilancio basso")
    public void testStrategyLowBal(){
        museo.setBilancio(amministratore, 150);
        IncaricoMostra incaricoMostra = amministratore.createIncaricoMostra();
        assertNotNull(incaricoMostra);
        assertTrue(amministratore.getStrategy() instanceof LowBudgetStrategy);
        List<Opera> opereMostra = incaricoMostra.getOpereMostra();
        for(Opera o:opereMostra)
            assertSame(o.getProprietario(),museo);
    }

    /**
     * Verifico che con un alto budget riesco a noleggiare dei quadri anche da altri musei.
     * Visto che questa strategy prevede 4 Opere da musei che non sia questo, allora asserisco
     * {@code true} se in questo ArrayList ho esattamente 4 opere di musei esterni.
     */
    @Test
    @DisplayName("Test strategy bilancio sostanzioso")
    public void testStrategyHighBal(){
        museo.setBilancio(amministratore,300);
        IncaricoMostra incaricoMostra = amministratore.createIncaricoMostra();
        assertNotNull(incaricoMostra);
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
     */
    @Test
    @DisplayName("Test strategy automatica con alto numero di suggerimenti")
    public void testStrategyAuto(){
        Visitatore v = new Visitatore();
        amministratore.loadSuggerimentiPerOpera(this);
        Opera opera1 = gestoreOpere.getOperaNome("TestA");
        Opera opera2 = gestoreOpere.getOperaNome("TestB");
        for(int i=0;i<5;i++)
            museo.registraSuggerimento(new Suggerimento(opera1,v));
        for(int j=0;j<5;j++)
            museo.registraSuggerimento(new Suggerimento(opera2,v));
        museo.setBilancio(amministratore,5000);
        incaricoMostra = amministratore.createIncaricoMostra();
        assertTrue(incaricoMostra.getOpereMostra().contains(opera1));
        assertTrue(incaricoMostra.getOpereMostra().contains(opera2));
    }

    @Test
    @DisplayName("Test della strategy che annulla una Mostra in corso di realizzazione o di svolimento")
    public void testKillStrategy(){
        testStrategyAuto(); // creo, tramite strategy precedente, un IncaricoMostra
        museo.setBilancio(amministratore, 50000);
        incaricoMostra = amministratore.createIncaricoMostra();
        for(Opera o: incaricoMostra.getOpereMostra())
            assertFalse(o.isBusy()); // mi aspetto che le opere che avevo affittato siano tornate di nuovo libere
        assertFalse(incaricoMostra.isKillable()); //vuol dire che non è killabile, cioè che è già stato killato.
    }
}
