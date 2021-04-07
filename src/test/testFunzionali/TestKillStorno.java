package test.testFunzionali;

import museo.Mostra;
import museo.Museo;
import opera.GestoreOpere;
import org.junit.jupiter.api.*;
import personale.pkgIncaricoMostra.Amministratore;
import personale.pkgIncaricoMostra.IncaricoMostra;
import visitatore.Visitatore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestKillStorno {
    private Museo museo;
    private Amministratore amministratore;
    private Mostra mostra;
    private IncaricoMostra incaricoMostra;
    private IncaricoMostra incaricoMostra1;

    @BeforeEach
    public void init(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
    }

    /**
     * Blocco l'Amministratore e faccio arrivare il load factor più in alto della soglia massima di sopportazione.
     * A quel punto riattivo l'Amministratore e verifico che lui capisca che siamo in una situazione in cui vadano
     * annullate delle Mostre. Con forceStrategyExecution forzo la creazione di un IncaricoMostra e inizializzo
     * l'operazione di creazione di una Mostra per merito di un Organizzatore. Infine riattivo l'Amministratore
     * il metodo update.
     */
    @Test
    @DisplayName("Test kill di mostre aperte")
    public void testKillMostreAperteHihPayload(){
        amministratore.setAmministratoreAutomatico(false);
        museo.setBilancio(amministratore, 6000);
        incaricoMostra = amministratore.forceStrategyExecution(2, 20, true);
        incaricoMostra = amministratore.forceStrategyExecution(2, 20, true);
        incaricoMostra = amministratore.forceStrategyExecution(2, 20, true);
        incaricoMostra = amministratore.forceStrategyExecution(2, 20, true);
        assertTrue(amministratore.getLoadFactorSale() > 0.9);
        mostra = incaricoMostra.getMostra();
        amministratore.update(null, null);
        assertTrue(amministratore.getLoadFactorSale() <= 0.9);
    }

    /**
     * Sfrutto le variabili di classe create dal test precedente. In particolare l'IncaricoMostra per far vedere che è
     * un incarico Killabile, e la Mostra a lui collegata. Faccio entrare in quella Mostra qualche Visitatore,
     * non abbastanza da farla chiudere. Dopo avvio la procedura di chiusura forzata come nel test precedente e
     * faccio vedere come la quantità di denaro dei visitatori che sono entrati in Mostra non è quella Originale,
     * ma è quella stornata dall'Organizzatore della Mostra: il costo
     * del biglietto meno una trattenuta del 40%. La Strategia del test precedente ci assicura che
     * la Mostra creata è solo Fisica, quindi basta un Visitatore normale per acquistare il Ticket.
     */
    @Test
    @DisplayName("Test kill di Mostre aperte con visitatori")
    public void testKillMostreStornoDenaro(){
        amministratore.setAmministratoreAutomatico(false);
        museo.setBilancio(amministratore, 6000);
        incaricoMostra1 = amministratore.forceStrategyExecution(2, 20, true);
        incaricoMostra = amministratore.forceStrategyExecution(2, 20, true);
        incaricoMostra = amministratore.forceStrategyExecution(2, 20, true);
        incaricoMostra = amministratore.forceStrategyExecution(2, 20, true);
        assertTrue(amministratore.getLoadFactorSale() > 0.9);

        mostra = incaricoMostra1.getMostra();
        int postiMostra = mostra.getPostiRimasti();
        int costoIngressoMostra = mostra.getCostoBiglietto();
        double temp = costoIngressoMostra;
        temp -= temp*0.4f;
        int costoMenoTrattenuta = (int)temp;
        Visitatore[] visitatori = new Visitatore[10];
        for(int i=0;i<visitatori.length;i++){
            visitatori[i] = new Visitatore(costoIngressoMostra);
            museo.vendiTicketMostraFisica(visitatori[i], mostra);
        }
        assertEquals(0, visitatori[0].getBilancio());
        amministratore.update(null, null);
        assertEquals(costoMenoTrattenuta, visitatori[0].getBilancio());
    }

    @AfterEach
    public void clear(){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        gestoreOpere.resetAllOpere();
    }
}
