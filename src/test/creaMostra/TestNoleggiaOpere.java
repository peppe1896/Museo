package test.creaMostra;

import museo.Museo;
import opera.GestoreOpere;
import opera.Opera;
import org.junit.jupiter.api.*;
import personale.pkgIncaricoMostra.Amministratore;
import personale.pkgIncaricoMostra.IncaricoMostra;

import static org.junit.jupiter.api.Assertions.*;

public class TestNoleggiaOpere {
    private Museo museo;
    private GestoreOpere gestoreOpere;
    private Opera operaDaAffittare;
    private Opera operaDaRestituire;
    private Amministratore amministratore;
    private IncaricoMostra incaricoMostra;

    /**
     * testA è un'opera presente nel museo che è di un proprietario null, e cioè è un'opera affittabile.
     *
     * testSuggerimento è un'opera (riciclata dal TestSuggerimento) che è di proprietà di museo.
     */
    @BeforeEach
    public void initializeTest(){
        museo = new Museo();
        gestoreOpere = museo.getGestoreOpere();
        operaDaAffittare = gestoreOpere.getOperaNome("TestA");
        operaDaRestituire = gestoreOpere.getOperaNome("TestSuggerimento");
        amministratore = new Amministratore(museo);
    }


    /**
     * Verifico che tutte le Opere previste da un IncaricoMostra siano state affittate dal museo.
     */
    @Test
    @DisplayName("Test verifica noleggio opere di un IncaricoMostra")
    public void test(){
        amministratore.setAmministratoreAutomatico(false);
        museo.setBilancio(amministratore, 50000);
        incaricoMostra = amministratore.forceStrategyExecution(2,14,false);
        for(Opera o: incaricoMostra.getOpereMostra())
            assertTrue(o.getAffittuario() == museo);
    }
}
