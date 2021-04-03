package test;

import museo.Museo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import visitatore.Visitatore;

import static org.junit.jupiter.api.Assertions.*;

public class TestVendita {
    private Museo m;

    @BeforeEach
    public void createMuseo(){
        m = new Museo();
    }

    /**
     * Mi aspetto che non si possa pagare con un visitatore che non ha budget, e invece uno
     * che ne ha può prendere il biglietto di ingresso al museo
     */
    @Test
    @DisplayName("Test no Budget")
    public void testVendiBigliettoNoBudget(){
        assertFalse(m.vendiBigliettoMuseo(new Visitatore()));
    }

    @Test
    @DisplayName("Test with Budget")
    public void testVendiBigliettoBudget(){
        assertTrue(m.vendiBigliettoMuseo(new Visitatore(100)));
    }
}
