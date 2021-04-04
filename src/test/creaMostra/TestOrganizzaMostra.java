package test.creaMostra;

import museo.Museo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import personale.pkgIncaricoMostra.Amministratore;
import personale.pkgIncaricoMostra.IncaricoMostra;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestOrganizzaMostra {
    private static Museo museo;
    private static Amministratore amministratore;
    private static IncaricoMostra incaricoMostra;

    @BeforeAll
    public static void initTest(){
        museo = new Museo();
        amministratore = new Amministratore(museo);
        museo.setBilancio(amministratore, 1500);
        incaricoMostra = amministratore.createIncaricoMostra();
        System.out.println("");
    }
    @Test
    public void test(){
        incaricoMostra.getOrganizzatore().svolgiIncaricoAssegnato();
        assertTrue(museo.getMostre().size()==1);
    }

}
