package test;

import museo.Museo;
import museo.Suggerimento;
import museo.Suggeritore;
import opera.Opera;
import org.junit.jupiter.api.*;
import visitatore.Visitatore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSuggerimento {
    private Museo m;

    @BeforeEach
    public void createMuseo(){
        m = new Museo();
    }

    /**
     * Creo una opera di test, e aggiungo il suggerimento di questa opera di test.
     * Se il test va a buon fine, è presente il suggerimento per questa opera di test.
     */

    @Test
    @DisplayName("Test aggiunta suggerimento")
    public void testAggiuntaSuggerimento(){
        Visitatore v = new Visitatore(100);
        Opera operaTest = new Opera("Test", "Giuseppe", m);
        m.registraSuggerimento(new Suggerimento(operaTest, v));
        boolean as = false;
        for(Suggerimento o:m.getSuggerimenti(v))
            if(o.getSuggerimento().getNome().equals("Test"))
                as = true;
        assertTrue(as);
    }
}
