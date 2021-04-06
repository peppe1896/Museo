package test;

import museo.Museo;
import opera.GestoreOpere;
import opera.Opera;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personale.pkgIncaricoMostra.Amministratore;

/**
 * Test classe GestoreOpere
 */

public class TestGestoreOpere {
    private static Museo museo = new Museo();
    private static GestoreOpere gestoreOpere = museo.getGestoreOpere();
    private static Amministratore amministratore = new Amministratore(museo);

    /**
     * Provo ad affittare l'opera TestA, il cui proprietario è null.
     */
    @Test
    @DisplayName("Test Affitta Opere")
    public void testAffittoOpereWithMoney(){
        Opera o = gestoreOpere.getOperaNome("TestA");
        Museo utilizzatore = o.getAffittuario();
        //gestoreOpere.affittaOperaAMuseo(o,  museo);
        //assertTrue(true);
    }
}
