package personale.pkgIncaricoImpiegato;

import personale.IncaricoImpiegato;

public class spostareOpera extends IncaricoImpiegato {

    @Override
    public boolean svolgiIncarico() {
        System.out.print("Opera spostata-");
        return true;
    }
}
