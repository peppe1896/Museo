package strutturaMuseo.personaleMuseo.personaleEsecutivo.incarichiMostra;

import strutturaMuseo.personaleMuseo.personaleEsecutivo.IncaricoImpiegato;

public class OrganizzaSitoWeb extends IncaricoImpiegato {
    OrganizzaSitoWeb(){}
    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto predisponendo il portale online..");
            Thread.sleep(1000);
            System.out.println("\tPortale pronto");
        }catch (InterruptedException e){}
        return true;
    }
}
