package personale.pkgIncaricoImpiegato;

import personale.IncaricoImpiegato;

public class CreaPubblicità extends IncaricoImpiegato {

    @Override
    public boolean svolgiIncarico() {
        System.out.println("Creata pubblicità");
        return true;
    }
}
