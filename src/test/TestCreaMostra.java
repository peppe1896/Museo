package test;

import museo.Museo;
import museo.Suggerimento;
import opera.Opera;
import org.junit.jupiter.api.*;
import personale.Amministratore;
import personale.IncaricoMostra;
import personale.LowBudgetStrategy;
import personale.ZeroBudgetStrategy;
import visitatore.Visitatore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreaMostra {
    private Museo museo;
    private Amministratore amministratore;

    @BeforeEach
    public void setTest(){
        museo = new Museo();
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
     * Dentro questo test creo un paio di suggerimenti per due opere, che attiva la AutomaticStrategy,
     * che prevede di preparare un IncaricoMostra con 3 opere del museo e 2 suggerite.
     */
    @Test
    @DisplayName("Test strategy automatica con alto numero di suggerimenti")
    public void testStrategyAuto(){
        Visitatore v = new Visitatore();
        Opera opera1 = new Opera("TestA", "Giuseppe", null, 50);
        Opera opera2 = new Opera("TestB", "Giuseppe", null, 50);
        museo.getCatalogoOpere().add(opera1);
        museo.getCatalogoOpere().add(opera2);
        amministratore = new Amministratore(museo);
        for(int i=0;i<5;i++)
            museo.registraSuggerimento(new Suggerimento(opera1,v));
        for(int i=0;i<5;i++)
            museo.registraSuggerimento(new Suggerimento(opera2,v));
        museo.setBilancio(amministratore,5000);
        IncaricoMostra incaricoMostra = amministratore.createIncaricoMostra();
        assertTrue(incaricoMostra.getOpereMostra().contains(opera1));
        assertTrue(incaricoMostra.getOpereMostra().contains(opera2));
    }
}
