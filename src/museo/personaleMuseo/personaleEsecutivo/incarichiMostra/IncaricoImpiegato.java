package museo.personaleMuseo.personaleEsecutivo.incarichiMostra;

import museo.personaleMuseo.Incarico;

/**
 * Sono gli incarichi che vengono assegnati agli impiegati. Questi vengono assegnati agli Impiegati dagli Organizzatori
 * delle varie Mostre.
 */
public abstract class IncaricoImpiegato implements Incarico {
    boolean done = false;

    public void setDone(){
        this.done = true;
    }
    public boolean hasDone(){
        return done;
    }
}