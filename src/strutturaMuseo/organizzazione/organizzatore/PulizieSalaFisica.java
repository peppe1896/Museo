package strutturaMuseo.organizzazione.organizzatore;

import strutturaMuseo.organizzazione.Impiegato;

public class PulizieSalaFisica extends IncaricoImpiegato implements ImpostaIncarico{
    PulizieSalaFisica(){}
    PulizieSalaFisica(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }
    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto pulendo le sale richieste..");
            Thread.sleep(500);
            System.out.println("\tSale pulite");
        }catch (InterruptedException e){}
        return true;
    }
    public final void setIncaricoTo(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }
}
