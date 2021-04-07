package personale.pkgIncaricoImpiegato;

import personale.IncaricoImpiegato;

public class CreaPubblicità extends IncaricoImpiegato {

    @Override
    public boolean svolgiIncarico() {
        System.out.print("Creata pubblicità-");
        return true;
    }
}
