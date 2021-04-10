package strutturaMuseo.personaleMuseo.personaleEsecutivo.incarichiMostra;

import strutturaMuseo.personaleMuseo.personaleEsecutivo.IncaricoImpiegato;

public class CreaPubblicità extends IncaricoImpiegato {

    CreaPubblicità(){}

    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto creando la pubblicità..");
            Thread.sleep(500);
            System.out.println("\tPubblicità creata");
        }catch (InterruptedException e){}
        return true;
    }
}
