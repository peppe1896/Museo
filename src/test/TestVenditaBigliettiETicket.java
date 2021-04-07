package test;

import museo.Mostra;
import museo.Museo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personale.NoMoneyException;
import personale.pkgIncaricoMostra.Amministratore;
import visitatore.Visitatore;
import visitatore.VisitatoreReg;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testo dai casi base della semplice vendita per vedere se funziona il sistema di pagamento ingresso.
 * Provo come i Visitatori possano registrarsi e come possano scegliere una preferenza sui posti se sono registrati.
 * Verifico anche che le Mostre che sono state riempite siano state chiuse in automatico.
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
        try {
            assertTrue(museo.vendiBigliettoMuseo(new Visitatore(100)));
        }catch (NoMoneyException e){
            assertFalse(true);
        }
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
}
