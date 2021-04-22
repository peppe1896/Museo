package strutturaMuseo.organizzazione.organizzatore;

import strutturaMuseo.Incarico;
import strutturaMuseo.organizzazione.Impiegato;

/**
 * Sono gli incarichi che vengono assegnati agli impiegati. Questi vengono assegnati agli Impiegati dagli Organizzatori
 * delle varie Mostre.
 */
public abstract class IncaricoImpiegato implements Incarico {
    private boolean done = false;
    protected Impiegato impiegatoAssegnato = null;

    public void setDone(Impiegato i){
        if(i== impiegatoAssegnato)
            this.done = true;
    }
    public boolean hasDone(){
        return done;
    }
}
