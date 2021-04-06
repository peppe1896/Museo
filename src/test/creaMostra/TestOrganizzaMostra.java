package test.creaMostra;

import museo.Museo;
import org.junit.jupiter.api.*;
import personale.pkgIncaricoMostra.Amministratore;
import personale.pkgIncaricoMostra.IncaricoMostra;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrganizzaMostra {
    private static Museo museo;
    private static Amministratore amministratore;
    private static IncaricoMostra incaricoMostra;
    private static IncaricoMostra incaricoMostra2;
    private static IncaricoMostra incaricoMostra3;
    private static IncaricoMostra incaricoMostra4;

    @BeforeAll
    public static void initTest(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
        museo.setBilancio(amministratore, 1500);
    }

    /**
     * L'incarico assegnato a un organizzatore è appunto organizzare una mostra. Quindi chiamando il metodo
     * svolgiIncarico mi aspetto di trovare una Mostra nel Museo.
     */
    @Test
    @DisplayName("Test creazione una Mostra")
    public void testSingolaMostra(){
        incaricoMostra.getOrganizzatore().svolgiIncaricoAssegnato();
        assertEquals(museo.getMostre().size(), 1);
    }

    @Test
    @DisplayName("Test creazione Mostre con singola Strategia")
    public void testMultiMostreSingolaStrategia(){
        incaricoMostra2.getOrganizzatore().svolgiIncaricoAssegnato();
        incaricoMostra3.getOrganizzatore().svolgiIncaricoAssegnato();
        incaricoMostra4.getOrganizzatore().svolgiIncaricoAssegnato();
        assertEquals(museo.getMostre().size(), 4);
    }

}
