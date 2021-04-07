package personale.pkgIncaricoImpiegato;

import personale.IncaricoImpiegato;

public class PulizieSalaFisica extends IncaricoImpiegato {
    @Override
    public boolean svolgiIncarico() {
        System.out.print("Pulizie fatte-");
        return true;
    }
}
