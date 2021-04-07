package test.testProcedurali;

import museo.strutturaMuseo.Museo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import museo.personaleMuseo.amministratore.Amministratore;

import static org.junit.jupiter.api.Assertions.*;

public class TestObserver {
    private Amministratore amministratore;
    private Museo museo;
    private int numeroMostreCreate = 0;

    @BeforeEach
    public void setTestMain(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
    }

    @BeforeEach
    public void setTest(){
        numeroMostreCreate = museo.getMostre().size();
        assertEquals(numeroMostreCreate, 0);
    }

    /**
     * Mi aspetto di vedere che con un bilancio più alto l'Oberver (l'Amministratore) crei almeno una Mostra
     */
    @Test
    @DisplayName("Test numero Mostre con bilancio alto")
    public void testMinNumMostre() throws Exception {
        museo.addBilancio(amministratore, 3000);
        numeroMostreCreate = museo.getMostre().size();
        assertTrue(numeroMostreCreate != 0);
    }

    @Test
    @DisplayName("Test più musei")
    public void test() {
    }

}
