package test.testProcedurali;

import strutturaMuseo.museoAndAdmin.Museo;
import opera.GestoreOpere;
import org.junit.jupiter.api.*;
import strutturaMuseo.museoAndAdmin.Amministratore;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrganizzaMostra {
    private Museo museo;
    private Amministratore amministratore;

    @BeforeEach
    public void initTest(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
        amministratore.setAmministratoreAutomatico(false);
        museo.setBilancio(1500);
    }

    /**
     * L'incarico assegnato a un organizzatore è appunto organizzare una mostra. Quindi chiamando il metodo
     * svolgiIncarico mi aspetto di trovare una Mostra nel Museo. Questo è chiamato in forward dal metodo
     * forceStrategyExecution.
     */
    @Test
    @DisplayName("Test creazione una Mostra")
    public void testSingolaMostra(){
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

    @AfterEach
    public void clear(){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        gestoreOpere.resetAllOpere();
    }
}
