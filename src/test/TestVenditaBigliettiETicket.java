package test;

import museo.Mostra;
import museo.Museo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personale.pkgIncaricoMostra.Amministratore;
import visitatore.Visitatore;

import static org.junit.jupiter.api.Assertions.*;

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
        assertFalse(museo.vendiBigliettoMuseo(new Visitatore()));
    }

    /**
     * Il biglietto di ingresso costa 10, quindi se metto 9 non deve farmi entrare.
     */
    @Test
    @DisplayName("Test with Budget")
    public void testVendiBigliettoBudget(){
        assertTrue(museo.vendiBigliettoMuseo(new Visitatore(100)));
        assertFalse(museo.vendiBigliettoMuseo(new Visitatore(9)));

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
        }
        assertTrue(museo.getMostre().size() != 0);
        Object mostre[] = museo.getMostre().toArray();
        Mostra m = ((Mostra)mostre[0]);
        assertEquals(m.getPostiFisiciRimasti(), m.getPostiRimasti());
        for(int i=0;i<m.getPostiFisiciRimasti();i++) {
            museo.vendiTicketMostraFisica(visitatori[i], m);
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
        museo.setBilancio(amministratore, 4000);
        Visitatore[] visitatori = new Visitatore[60];
        for(int i=0;i< visitatori.length;i++){
            visitatori[i] = new Visitatore(200);
        }
        assertTrue(museo.getMostre().size() != 0);
        Object mostre[] = museo.getMostre().toArray();
        Mostra m = ((Mostra)mostre[0]);
        assertEquals(m.getPostiFisiciRimasti(), m.getPostiRimasti());
        for(int i=0;i<m.getPostiFisiciRimasti();i++) {
            museo.vendiTicketMostraFisica(visitatori[i], m);
        }
        assertTrue(m.isTerminata());

    }
}
