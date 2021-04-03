package test;

import museo.Museo;
import museo.Suggerimento;
import opera.Opera;
import org.junit.jupiter.api.*;
import personale.Amministratore;
import visitatore.Visitatore;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestSuggerimento {
    private static Museo museo;
    private static Amministratore amministratore;
    private static Opera opera;
    private static Visitatore visitatore;
    private static LinkedHashSet<Opera> opereBase;
    private static Suggerimento suggerimento;

    @BeforeAll
    public static void initializeOpereBase(){
        opereBase = new LinkedHashSet<>();
        museo = new Museo();
        opereBase.add(new Opera("Gioconda", "Leonardo Da Vinci", museo));
        opereBase.add(new Opera("Notte Stellata", "V. Van Gogh", museo));
        opereBase.add(new Opera("Giocasdonda", "Leonardsadso Da Vinci", museo));
        opereBase.add(new Opera("Gisadasoconda", "Leonardo asdDa Vinci", museo));
    }

    @BeforeEach
    public void initializeOperaVisitAmmin(){
        opera = new Opera("Test", "Giuseppe", museo);
        visitatore = new Visitatore(100);
        museo.addOpera(opera);
        suggerimento = new Suggerimento(opera, visitatore);
        amministratore = new Amministratore(museo);
    }

    /**
     * Creo una opera di test, e aggiungo il suggerimento di questa opera di test.
     * Se il test va a buon fine, è presente il suggerimento per questa opera di test.
     */
    //@Nested
    //@DisplayName("Test vari per testare il meccanismo di update")
    @Test
    @DisplayName("Test aggiunta suggerimento")
    public void testAggiuntaSuggerimento(){
        museo.registraSuggerimento(suggerimento);
        boolean assertion = false;
        for(Suggerimento o: museo.getSuggerimenti(visitatore))
            if(o.getSuggerimento().getNome().equals("Test"))
                assertion = true;
        assertTrue(assertion);
    }

    /**
     * Testo il metodo update di Amministratore, che è il metodo
     * di observer. Questo si aggiorna quando viene chiamato il metodo
     * registraSuggerimento di Museo. L'Amministratore è creato dopo il museo e l'opera, così
     * il costruttore di Amministratore costruisce l'oggetto amministratore aggiungendo nella mappa
     * di questo amministratore anche l'Opera di test.
     * //TODO: forse è importante che ci siano dei metodi dentro Amministratore che aggiornino quella
     * struttura dati mappa per tenere aggiornata.
     */
    @Test
    @DisplayName("Test Update Amministratore")
    public void testUpdateAmministratore() {
        LinkedHashMap<Opera, Integer> hashMapAmministratore = amministratore.getSuggerimentiPerOpera();
        /*
        Mi assicuro che non ci sia il suggerimento prima di aggiungerlo tramite il metodo
        registraSuggerimento() di Museo. Visto che è stato appena inizializzato, ho 0 in tutte le
        i Values della LinkedHashMap dell'amministratore
        */
        assertTrue(hashMapAmministratore.get(suggerimento.getSuggerimento()) == 0);
        museo.registraSuggerimento(suggerimento);
        /*
        Dopo che chiamo questo metodo mi aspetto che l'amministratore sia a conoscenza di questo suggerimento
         */
        System.out.println("");
        assertTrue(hashMapAmministratore.get(suggerimento.getSuggerimento()) == 1);
    }

    @AfterEach
    public void resetPartial(){
        museo.remOpera(opera);
        opera = null;
        suggerimento = null;
        amministratore = null;
    }
}
