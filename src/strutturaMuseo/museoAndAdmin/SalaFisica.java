package strutturaMuseo.museoAndAdmin;

import opera.Opera;
import strutturaMuseo.organizzazione.organizzatore.Sala;

public final class SalaFisica extends Sala {
    private Opera o = null;

    SalaFisica(int postiSala){
        super();
        this.postiSala = postiSala;
    }

    public void inserisciOpera(Opera o){
        this.o = o;
    }
    public Opera getOperaContenuta(){
        return o;
    }
    public void rimuoviOpera(){
        this.o = null;
    }
}
