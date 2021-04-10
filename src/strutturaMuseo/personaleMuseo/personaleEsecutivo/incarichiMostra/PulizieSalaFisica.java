package strutturaMuseo.personaleMuseo.personaleEsecutivo.incarichiMostra;

import strutturaMuseo.personaleMuseo.personaleEsecutivo.IncaricoImpiegato;

public class PulizieSalaFisica extends IncaricoImpiegato {
    PulizieSalaFisica(){}
    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto pulendo le sale richieste..");
            Thread.sleep(500);
            System.out.println("\tSale pulite");
        }catch (InterruptedException e){}
        return true;
    }
}
