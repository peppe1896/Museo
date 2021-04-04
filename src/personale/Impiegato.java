package personale;

public class Impiegato extends Personale{
    private IncaricoImpiegato incaricoImpiegato = null;

    public void setIncaricoImpiegato(IncaricoImpiegato incaricoImpiegato){
        this.incaricoImpiegato = incaricoImpiegato;
    }

    public void svolgiIncaricoAssegnato(){
        incaricoImpiegato.svolgiIncarico();
        incaricoImpiegato.setDone();
    }
}
