package strutturaMuseo.organizzazione.organizzatore;

import strutturaMuseo.organizzazione.Impiegato;

public class CreaPubblicità extends IncaricoImpiegato implements ImpostaIncarico{
    CreaPubblicità(){}
    CreaPubblicità(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }

    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto creando la pubblicità..");
            Thread.sleep(500);
            System.out.println("\tPubblicità creata");
        }catch (InterruptedException e){}
        return true;
    }

    public final void setIncaricoTo(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }
}
