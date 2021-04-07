package test.creaMostra;

import museo.Museo;
import org.junit.jupiter.api.*;
import personale.pkgIncaricoMostra.Amministratore;
import personale.pkgIncaricoMostra.IncaricoMostra;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrganizzaMostra {
    private static Museo museo;
    private static Amministratore amministratore;

    @BeforeAll
    public static void initTest(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
        museo.setBilancio(amministratore, 1500);
    }

    /**
     * L'incarico assegnato a un organizzatore è appunto organizzare una mostra. Quindi chiamando il metodo
     * svolgiIncarico mi aspetto di trovare una Mostra nel Museo. Questo è chiamato in forward dal metodo
     * forceStrategyExecution
     */
    @Test
    @DisplayName("Test creazione una Mostra")
    public void testSingolaMostra(){
        amministratore.setAmministratoreAutomatico(false);
        amministratore.forceStrategyExecution(2, 5, false);
        assertEquals(museo.getMostre().size(), 1);
    }

    @Test
    @DisplayName("Test creazione Mostre con singola Strategia")
    public void testMultiMostreSingolaStrategia(){
        amministratore.setAmministratoreAutomatico(false);
        amministratore.forceStrategyExecution(2, 5, false);
        amministratore.forceStrategyExecution(2, 5, false);
        amministratore.forceStrategyExecution(2, 5, false);
        amministratore.forceStrategyExecution(2, 5, false);
        assertEquals(museo.getMostre().size(), 4);
    }

}
