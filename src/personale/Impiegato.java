package personale;

import java.util.ArrayList;
import java.util.List;

public class Impiegato extends Personale{
    private IncaricoImpiegato incaricoImpiegato = null;
    List<IncaricoImpiegato> incarichiSvolti = new ArrayList<>();

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
