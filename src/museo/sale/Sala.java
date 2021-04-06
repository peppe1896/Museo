package museo.sale;

public abstract class Sala {
    protected int postiSala;
    private boolean busy = false;

    public void setBusy(){
        busy = true;
    }

    public boolean isBusy(){
        return busy;
    }

    public int getPostiSala(){
        return postiSala;
    }

    public void freeSala(){
        this.busy = false;
    }
}
