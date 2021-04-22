package strutturaMuseo.organizzazione.organizzatore;

import strutturaMuseo.organizzazione.Impiegato;

public class SpostareOpera extends IncaricoImpiegato implements ImpostaIncarico{
    SpostareOpera(){}
    SpostareOpera(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }

    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto spostando l'opera..");
            Thread.sleep(100);
            System.out.println("\tOpera Spostata");
        }catch (InterruptedException e){}
        return true;
    }

    public final void setIncaricoTo(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }
}
