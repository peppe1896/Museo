package personale;

import java.util.ArrayList;
import java.util.List;

/**
 * Svolge l'incarico che gli viene assegnato da un Organizzatore per qualche Mostra. Gli Incarichi svolti vengono
 * salvati dentro una lista.
 */
public class Impiegato extends Personale{
    private IncaricoImpiegato incaricoImpiegato = null;
    private List<IncaricoImpiegato> incarichiSvolti = new ArrayList<>();

    public void setIncaricoImpiegato(IncaricoImpiegato incaricoImpiegato){
        this.incaricoImpiegato = incaricoImpiegato;
    }

    public void svolgiIncaricoAssegnato(){
        incaricoImpiegato.svolgiIncarico();
        incaricoImpiegato.setDone();
        incarichiSvolti.add(incaricoImpiegato);
        incaricoImpiegato = null;
    }
}
