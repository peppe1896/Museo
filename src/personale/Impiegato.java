package personale;

public class Impiegato extends Personale{
    private IncaricoImpiegato incaricoImpiegato = null;

    public void setIncaricoImpiegato(IncaricoImpiegato incaricoImpiegato){
        this.incaricoImpiegato = incaricoImpiegato;
    }

    public void svolgiIncaricoAssegnato(){
        incaricoImpiegato.svolgiIncarico();
        incaricoImpiegato.setDone();
        //TODO forse dovrebbe essere qui quello che resetta l'incaricoImpiegato a null
        // in questo momento è l'organizzatore a resettare l'incarico.
    }
}
