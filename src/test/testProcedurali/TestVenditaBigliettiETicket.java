package test.testProcedurali;

import strutturaMuseo.museo.Mostra;
import strutturaMuseo.museo.Museo;

import opera.GestoreOpere;
import org.junit.jupiter.api.*;
import strutturaMuseo.personaleMuseo.NoMoneyException;
import strutturaMuseo.personaleMuseo.amministratore.Amministratore;
import visitatore.Visitatore;
import visitatore.VisitatoreReg;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Con questa classe di Test verifico che si possano vendere i biglietti per le mostre che sono state
 * create. Preciso che le Mostre a cui i Visitatori accedono sono create in automatico tramite le
 * strategie automatiche della classe Amministratore, che grazie al dialogo che ha con il Museo, riesce a capire
 * quando deve creare una nuova Mostra. Può anche Distruggere una Mostra avviata o in fase di Avvio, ma questo
 * è mostrato in altri Test.
 */
public class TestVenditaBigliettiETicket {
    private Museo museo;
    private Amministratore amministratore;

    @BeforeEach
    public void init(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
    }

    /**
     * Mi aspetto che non si possa pagare con un visitatore che non ha budget, e invece uno
     * che ne ha può prendere il biglietto di ingresso al museo
     */
    @Test
    @DisplayName("Test no Budget")
    public void testVendiBigliettoNoBudget(){
        try {
            museo.vendiBigliettoMuseo(new Visitatore());
        }catch (NoMoneyException e) {
            assertTrue(true);
        }
    }

    /**
     * Il biglietto di ingresso costa 10. Uso l'Eccezione  NoMoneyException per capire se il test fallisce.
     */
    @Test
    @DisplayName("Test with Budget")
    public void testVendiBigliettoBudget(){
        assertTrue(museo.vendiBigliettoMuseo(new Visitatore(100)));
    }

    /**
     * Se l'Amministratore vede un budget del Museo di più di 5000, allora attiva una strategia con solo
     * Sale fisiche
     */
    @Test
    @DisplayName("Test Vendita ticket per mostra")
    public void testVenditaTicketFisici(){
        museo.setBilancio(amministratore, 6000);
        int oldBilancio = museo.getBilancio(amministratore);
        Visitatore[] visitatori = new Visitatore[60];
        for(int i=0;i< visitatori.length;i++){
            visitatori[i] = new Visitatore(100);
            museo.registraVisitatore(visitatori[i]);
        }
        assertTrue(museo.getMostre().size() != 0);
        Object mostre[] = museo.getMostre().toArray();
        Mostra m = ((Mostra)mostre[0]);
        assertEquals(m.getPostiFisiciRimasti(), m.getPostiRimasti());
        int postiFisiciRimasti = m.getPostiFisiciRimasti();
        for(int i=0;i<postiFisiciRimasti + 10;i++) {
            if(i<postiFisiciRimasti)
                assertTrue(museo.vendiTicketMostraFisica(visitatori[i], m));
            else
                assertFalse(museo.vendiTicketMostraFisica(visitatori[i], m));
        }
        assertTrue(m.isTerminata());
        assertTrue(oldBilancio < museo.getBilancio(amministratore));
    }

    /**
     * Se l'Amministratore vede un budget tra inferiore ai 5000 invece fa una PersonalStrategy che prevede anche le
     * sale Virtuali.
     */
    @Test
    @DisplayName("Test Vendita Ticket mostre miste, quindi su Sale fisiche e Virtuali")
    public void testSaleMiste(){
        museo.setBilancio(amministratore, 6000);
        Visitatore[] visitatori = new Visitatore[60];
        for(int i=0;i< visitatori.length;i++){
            visitatori[i] = new Visitatore(200);
            museo.vendiBigliettoMuseo(visitatori[i]);
        }
        assertTrue(museo.getMostre().size() != 0);
        Object mostre[] = museo.getMostre().toArray();
        Mostra m = ((Mostra)mostre[0]);
        assertEquals((m.getPostiFisiciRimasti() + m.getPostiVirtualiRimasti()), m.getPostiRimasti());
        for(Map.Entry<String, VisitatoreReg> entry: museo.getUtentiRegistrati().entrySet()) {
            museo.vendiTicketMostraVirtuale(entry.getValue(), m, true);
        }
        assertTrue(m.isTerminata());
    }

    @AfterEach
    public void clear(){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        gestoreOpere.resetAllOpere();
    }
}
