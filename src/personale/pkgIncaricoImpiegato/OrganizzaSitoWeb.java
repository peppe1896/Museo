package personale.pkgIncaricoImpiegato;

import personale.IncaricoImpiegato;

public class OrganizzaSitoWeb extends IncaricoImpiegato {
    @Override
    public boolean svolgiIncarico() {
        System.out.print("Sito web pronto.-");
        return true;
    }
}
