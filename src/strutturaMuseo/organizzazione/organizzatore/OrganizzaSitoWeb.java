package strutturaMuseo.organizzazione.organizzatore;

import strutturaMuseo.organizzazione.Impiegato;

public class OrganizzaSitoWeb  extends IncaricoImpiegato implements ImpostaIncarico {
    OrganizzaSitoWeb(){}
    OrganizzaSitoWeb(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }
    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto organizzanod il portale online..");
            Thread.sleep(1000);
            System.out.println("\tPortale pronto");
        }catch (InterruptedException e){}
        return true;
    }
    public final void setIncaricoTo(Impiegato assegnato){
        this.impiegatoAssegnato = assegnato;
    }
}
