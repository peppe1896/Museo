package strutturaMuseo.organizzazione.organizzatore;

import opera.Opera;

public abstract class Sala {
    protected int postiSala;
    private boolean busy = false;

    void setBusy(){
        busy = true;
    }

    public boolean isBusy(){
        return busy;
    }

    public int getPostiSala(){
        return postiSala;
    }

    void freeSala(){
        this.busy = false;
    }

    public void inserisciOpera(Opera o){
        System.err.println("Non puoi mettere un'opera in una sala virtuale");
    }
    public void rimuoviOpera(){
        System.err.println("Non puoi togliere un'opera dalla sala virtuale");
    }
    public void associaSalaFisica(Sala sala){
        System.err.println("Non puoi associare una sala fisica a un'altra sala fisica");
    }
    public void dissociaSalaFisica(){
        System.err.println("Non ci sono sale fisiche associate a una sala fisica");
    }
}
