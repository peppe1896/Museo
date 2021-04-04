package personale;

import museo.Museo;
import personale.strategy.IncaricoMostra;

import java.util.Observable;
import java.util.Observer;

/**
 * Organizzatore sviluppa un IncaricoMostra fornito da un amministratore. se fosse richiesto dall'Amministratore,
 * questa mostra deve essere annullata rilasciando tutte le opere, e questa richiesta è fatta usando l'observer.
 */

public class Organizzatore extends Personale implements Observer {
    private Museo museo;
    private IncaricoMostra incaricoAttuale;

    public Organizzatore(Museo museo){
        this.museo = museo;
    }

    public void setIncaricoAttuale(IncaricoMostra incaricoMostra){
        incaricoAttuale = incaricoMostra;
    }

    public void organizza(){

    }

    private void chiudiMostra(){

    }

    @Override
    public void update(Observable o, Object arg) {
        chiudiMostra();
    }
}
