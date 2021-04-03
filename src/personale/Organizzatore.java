package personale;

import museo.Museo;

public class Organizzatore extends Personale{
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
}
