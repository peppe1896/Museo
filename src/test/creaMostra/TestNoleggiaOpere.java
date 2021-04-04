package test.creaMostra;

import museo.Museo;
import opera.GestoreOpere;
import opera.Opera;
import org.junit.jupiter.api.*;
import personale.pkgIncaricoMostra.Amministratore;

import static org.junit.jupiter.api.Assertions.*;

public class TestNoleggiaOpere {
    private Museo museo;
    private GestoreOpere gestoreOpere;
    private Opera operaDaAffittare;
    private Opera operaDaRestituire;
    private Amministratore amministratore;

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

    @Test
    @DisplayName("Test verifica effettiva acquisizione")
    public void testAffittoOpera(){
        museo.setBilancio(amministratore, 500);
        assertFalse(operaDaAffittare.isBusy());
        assertEquals(operaDaAffittare.getProprietario(), operaDaAffittare.getAffittuario());
        gestoreOpere.affittaOperaAMuseo(operaDaAffittare, museo);
        assertEquals(operaDaAffittare.getAffittuario(), museo);

    }
}
