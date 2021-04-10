package strutturaMuseo.personaleMuseo.personaleEsecutivo.incarichiMostra;

import strutturaMuseo.personaleMuseo.personaleEsecutivo.IncaricoImpiegato;

public class SpostareOpera extends IncaricoImpiegato {
    SpostareOpera(){}

    @Override
    public boolean svolgiIncarico() {
        try{
            System.out.println("Sto spostando l'opera..");
            Thread.sleep(500);
            System.out.println("\tOpera Spostata");
        }catch (InterruptedException e){}
        return true;
    }
}
