package museo;

import visitatore.Visitatore;

public class Biglietto {
    private Visitatore acquirente;

    /**
     * Il biglietto viene creato solo dal museo: questo è assicurato dal costruttore package protected.
     * Potrebbe essere meglio un Factory?
     * @param visitatore è colui che acquista questo biglietto. Il visitatore non ha un riferimento al biglietto.
     */
    Biglietto(Visitatore visitatore){
        this.acquirente = visitatore;
    }
}
