package strutturaMuseo.organizzazione.organizzatore;

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

}
