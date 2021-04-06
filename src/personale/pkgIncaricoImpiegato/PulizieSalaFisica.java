package personale.pkgIncaricoImpiegato;

import personale.IncaricoImpiegato;

public class PulizieSalaFisica extends IncaricoImpiegato {
    @Override
    public boolean svolgiIncarico() {
        System.out.println("Pulizie fatte");
        return true;
    }
}
